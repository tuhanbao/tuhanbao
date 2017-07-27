package com.sztx.se.dataaccess.redis.template;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis的操作模板代理
 * 
 * @author zhihongp
 * 
 */
public class RedisTemplateProxy {

	private String redisSourceKey;

	private List<RedisTemplate<Serializable, Serializable>> redisTemplateList;

	private boolean isDefault;

	public String getRedisSourceKey() {
		return redisSourceKey;
	}

	public void setRedisSourceKey(String redisSourceKey) {
		this.redisSourceKey = redisSourceKey;
	}

	public List<RedisTemplate<Serializable, Serializable>> getRedisTemplateList() {
		return redisTemplateList;
	}

	public void setRedisTemplateList(List<RedisTemplate<Serializable, Serializable>> redisTemplateList) {
		this.redisTemplateList = redisTemplateList;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public String toString() {
		return "RedisTemplateProxy [redisSourceKey=" + redisSourceKey + ", redisTemplateList=" + redisTemplateList + ", isDefault=" + isDefault + "]";
	}

}
