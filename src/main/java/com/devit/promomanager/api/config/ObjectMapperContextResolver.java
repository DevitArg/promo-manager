package com.devit.promomanager.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

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
