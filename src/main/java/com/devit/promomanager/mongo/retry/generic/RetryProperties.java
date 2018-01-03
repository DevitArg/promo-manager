package com.devit.promomanager.mongo.retry.generic;

import org.springframework.context.annotation.Configuration;

/**
 * @author Lucas.Godoy on 14/12/17.
 */
@Configuration
public class RetryProperties {

	private Long retryBackOffTime;
	private Integer retryMaxAttempts;

	public Integer getRetryMaxAttempts() {
		return retryMaxAttempts;
	}

	public void setRetryMaxAttempts(Integer retryMaxAttempts) {
		this.retryMaxAttempts = retryMaxAttempts;
	}

	public Long getRetryBackOffTime() {
		return retryBackOffTime;
	}

	public void setRetryBackOffTime(Long retryBackOffTime) {
		this.retryBackOffTime = retryBackOffTime;
	}

}
