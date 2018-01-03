package com.devit.promomanager.mongo;

import com.devit.promomanager.mongo.retry.generic.IConfigurableRetryProperties;
import com.devit.promomanager.mongo.retry.generic.RetryProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lucas.Godoy on 13/11/17.
 */
@Configuration
@ConfigurationProperties(prefix = "mongo.config")
@Getter
@Setter
public class MongoConfigurationProperties implements IConfigurableRetryProperties {

	private String uri = "";
	private String replicaSetName = "";
	private MongoType type = MongoType.INSTANCE;
	private Integer connectTimeout = 3000;
	private Integer socketTimeout = 3000;
	private Integer serverTimeout = 3000;
	@NestedConfigurationProperty
	private RetryProperties retryProperties;

}
