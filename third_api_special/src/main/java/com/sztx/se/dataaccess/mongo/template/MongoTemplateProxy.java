package com.sztx.se.dataaccess.mongo.template;

import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoTemplateProxy {

	private String mongoSourceKey;

	private MongoTemplate mongoTemplate;

	private boolean isDefault;

	public String getMongoSourceKey() {
		return mongoSourceKey;
	}

	public void setMongoSourceKey(String mongoSourceKey) {
		this.mongoSourceKey = mongoSourceKey;
	}

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

}
