package com.devit.promomanager.mongo.instance;

import com.devit.promomanager.mongo.MongoConfigurationProperties;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.UnknownHostException;

/**
 * @author Lucas.Godoy on 13/11/17.
 */
@Component
public class MongoFactoryProvider {

	@Autowired
	private MongoConfigurationProperties mongoConfigurationProperties;

	public SimpleMongoDbFactory getMongoFactory() throws UnknownHostException {
		MongoClientOptions.Builder optionsBuilder = MongoClientOptions.builder();
		optionsBuilder.connectTimeout(mongoConfigurationProperties.getConnectTimeout());
		optionsBuilder.socketTimeout(mongoConfigurationProperties.getSocketTimeout());
		optionsBuilder.serverSelectionTimeout(mongoConfigurationProperties.getSocketTimeout());
		if (!StringUtils.isEmpty(mongoConfigurationProperties.getReplicaSetName())) {
			optionsBuilder.requiredReplicaSetName(mongoConfigurationProperties.getReplicaSetName());
		}
		MongoClientURI mongoClientURI = new MongoClientURI(mongoConfigurationProperties.getUri(), optionsBuilder);
		return new SimpleMongoDbFactory(mongoClientURI);
	}

}
