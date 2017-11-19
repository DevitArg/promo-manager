package com.devit.promomanager.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Lucas.Godoy on 13/11/17.
 */
@Component
public class MongoTemplateProvider {

	@Autowired
	private MongoConfigurationProperties mongoConfigurationProperties;

	@Autowired
	private List<MongoTemplateFactory> mongoTemplateFactories;

	public MongoTemplate getMongoTemplate() throws Exception {
		MongoTemplateFactory mongoTemplateFactory = mongoTemplateFactories
				.parallelStream()
				.filter(factory -> factory.supports(mongoConfigurationProperties.getType()))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);

		return mongoTemplateFactory.mongoTemplate();
	}
}
