package com.devit.promomanager.mongo.embedded;

import com.devit.checkmein.configuration.mongo.MongoTemplateFactory;
import com.devit.checkmein.configuration.mongo.MongoType;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Lucas.Godoy on 13/11/17.
 */
@Component
public class EmbeddedMongoTemplateFactory implements MongoTemplateFactory {

	@Override
	public boolean supports(MongoType mongoType) {
		return MongoType.EMBEDDED.equals(mongoType);
	}

	@Override
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(EmbeddedMongoConfiguration.getMongoClient(), "test");
	}
}
