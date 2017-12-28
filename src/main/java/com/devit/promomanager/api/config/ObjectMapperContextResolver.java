package com.devit.promomanager.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * @author Lucas.Godoy on 8/12/17.
 */
@Provider
@Configuration
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
	}

}
