package com.devit.promomanager.api.config;

import com.devit.promomanager.api.PromoCrudApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.ext.ContextProvider;
import org.apache.cxf.jaxrs.validation.JAXRSBeanValidationFeature;
import org.apache.cxf.jaxrs.validation.JAXRSBeanValidationInvoker;
import org.apache.cxf.jaxrs.validation.JAXRSParameterNameProvider;
import org.apache.cxf.validation.BeanValidationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Arrays;
import java.util.List;

/**
 * @author Lucas.Godoy on 5/12/17.
 */
@Configuration
public class CXFConfiguration {

	@Autowired
	private Bus bus;

	@Autowired(required = false)
	private List<ExceptionMapper<?>> exceptionMappers;

	@Autowired(required = false)
	private List<ContainerRequestFilter> containerRequestFilters;

	@Autowired(required = false)
	private List<ContextProvider> contextProviders;

	@Autowired
	private List<ContextResolver> contextResolvers;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PromoCrudApi promoCrudApi;

	@Bean
	public Server rsServer() {
		JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
		endpoint.setBus(bus);
		endpoint.setAddress("/");
		endpoint.setFeatures(Arrays.asList(validationFeature()));
		endpoint.setInvoker(validationInvoker());
		setServiceBeans(endpoint);
		setProviders(endpoint);
		return endpoint.create();

	}

	@Bean
	public JacksonJsonProvider jacksonJsonProvider() {
		return new JacksonJsonProvider(objectMapper);
	}

	private void setServiceBeans(JAXRSServerFactoryBean endpoint) {
		endpoint.setServiceBean(promoCrudApi);
	}

	private void setProviders(JAXRSServerFactoryBean endpoint) {
		endpoint.setProvider(jacksonJsonProvider());
		endpoint.setProviders(exceptionMappers);
		endpoint.setProviders(containerRequestFilters);
		endpoint.setProviders(contextProviders);
		endpoint.setProviders(contextResolvers);
	}

	@Bean
	public JAXRSParameterNameProvider parameterNameProvider() {
		return new JAXRSParameterNameProvider();
	}

	@Bean
	public BeanValidationProvider validationProvider() {
		return new BeanValidationProvider(parameterNameProvider());
	}

	@Bean
	public JAXRSBeanValidationInvoker validationInvoker() {
		final JAXRSBeanValidationInvoker validationInvoker = new JAXRSBeanValidationInvoker();
		validationInvoker.setProvider(validationProvider());
		return validationInvoker;
	}

	@Bean
	public JAXRSBeanValidationFeature validationFeature() {
		final JAXRSBeanValidationFeature feature = new JAXRSBeanValidationFeature();
		feature.setProvider(validationProvider());
		return feature;
	}

}
