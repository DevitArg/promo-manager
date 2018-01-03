package com.devit.promomanager.mongo.retry.generic;

import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * @author Lucas.Godoy on 14/12/17.
 */
public interface IRetryTemplateConfiguration {

	RetryPolicy retryPolicy();

	BackOffPolicy backOffPolicy();

	RetryTemplate retryTemplate();

	default RetryTemplate retryTemplate(RetryPolicy retryPolicy, BackOffPolicy backOffPolicy) {
		RetryTemplate retryTemplate = new RetryTemplate();
		retryTemplate.setRetryPolicy(retryPolicy);
		retryTemplate.setBackOffPolicy(backOffPolicy);
		return retryTemplate;
	}
}
