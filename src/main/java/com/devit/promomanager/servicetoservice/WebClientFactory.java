package com.devit.promomanager.servicetoservice;

public interface WebClientFactory<T> {

	T get(Class<T> clientType, String serviceName);

}
