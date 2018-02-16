package com.devit.promomanager.servicetoservice;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WebClientFactoryImpl<T> implements WebClientFactory<T> {

	@Autowired
	private LoadBalancerClient loadBalancer;

	@Override
	public T get(Class<T> clientType, String serviceName) {
		Optional<URI> uriOptional = getURIForService(serviceName);
		if (!uriOptional.isPresent()) {
			throw new IllegalStateException(
					String.format("The client for the requested app: %s is not available for some reason"));
		}
		return (T) JAXRSClientFactory.create(uriOptional.get().toString(), clientType, getJacksonJsonProviders());
	}

	public Optional<URI> getURIForService(String serviceName) {
		ServiceInstance instance = loadBalancer.choose(serviceName);

		if (instance == null)
			return Optional.empty();

		String scheme = instance.isSecure() ? "https" : "http";

		return Optional.of(UriComponentsBuilder.newInstance()
				.scheme(scheme)
				.host(instance.getHost())
				.port(instance.getPort()).build().toUri()
		);
	}

	public List<JacksonJsonProvider> getJacksonJsonProviders() {
		JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider();
		List<JacksonJsonProvider> jacksonJsonProviderList = new ArrayList<>();
		jacksonJsonProviderList.add(jacksonJsonProvider);
		return jacksonJsonProviderList;
	}
}
