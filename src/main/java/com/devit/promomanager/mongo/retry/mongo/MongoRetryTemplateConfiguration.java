package com.devit.promomanager.mongo.retry.mongo;

import com.devit.promomanager.mongo.MongoConfigurationProperties;
import com.devit.promomanager.mongo.retry.generic.IRetryTemplateConfiguration;
import com.mongodb.MongoSocketException;
import com.mongodb.MongoTimeoutException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lucas.Godoy on 14/12/17.
 */
@Configuration
public class MongoRetryTemplateConfiguration implements IRetryTemplateConfiguration {

	private final MongoConfigurationProperties mongoConfigurationProperties;

	public MongoRetryTemplateConfiguration(MongoConfigurationProperties mongoConfigurationProperties) {
		this.mongoConfigurationProperties = mongoConfigurationProperties;
	}

	@Override
	public RetryPolicy retryPolicy() {
		final ExceptionClassifierRetryPolicy exceptionClassifierRetryPolicy = new ExceptionClassifierRetryPolicy();
		exceptionClassifierRetryPolicy.setPolicyMap(retryPolicyExceptionMap());
		return exceptionClassifierRetryPolicy;
	}

	@Override
	public BackOffPolicy backOffPolicy() {
		final FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
		fixedBackOffPolicy.setBackOffPeriod(mongoConfigurationProperties.getRetryProperties().getRetryBackOffTime());
		return fixedBackOffPolicy;
	}

	public Map<Class<? extends Throwable>, RetryPolicy> retryPolicyExceptionMap() {
		final SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(mongoConfigurationProperties.getRetryProperties().getRetryMaxAttempts());

		final Map<Class<? extends Throwable>, RetryPolicy> retryPolicyExceptionMap = new HashMap<>();
		retryPolicyExceptionMap.put(NonTransientDataAccessException.class, retryPolicy);
		retryPolicyExceptionMap.put(MongoSocketException.class, retryPolicy);
		retryPolicyExceptionMap.put(MongoTimeoutException.class, retryPolicy);
		return retryPolicyExceptionMap;
	}

	@Bean
	@Override
	public RetryTemplate retryTemplate() {
		return retryTemplate(retryPolicy(), backOffPolicy());
	}

}
