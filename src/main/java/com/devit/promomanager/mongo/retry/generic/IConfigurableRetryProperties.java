package com.devit.promomanager.mongo.retry.generic;

/**
 * @author Lucas.Godoy on 21/12/17.
 */
public interface IConfigurableRetryProperties {

	RetryProperties getRetryProperties();

	void setRetryProperties(RetryProperties retryProperties);

}
