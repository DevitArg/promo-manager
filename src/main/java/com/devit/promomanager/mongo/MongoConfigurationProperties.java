package com.devit.promomanager.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lucas.Godoy on 13/11/17.
 */
@Configuration
@ConfigurationProperties(prefix = "mongo.config")
@Getter
@Setter
public class MongoConfigurationProperties {

	private String uri;
	private String replicaSetName;
	private MongoType type;

}
