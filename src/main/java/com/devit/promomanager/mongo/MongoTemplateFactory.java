package com.devit.promomanager.mongo;

import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author Lucas.Godoy on 13/11/17.
 */
public interface MongoTemplateFactory {

	boolean supports(MongoType mongoType);

	MongoTemplate mongoTemplate() throws Exception;

}
