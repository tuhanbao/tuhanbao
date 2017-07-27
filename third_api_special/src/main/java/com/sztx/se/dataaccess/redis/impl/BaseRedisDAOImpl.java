package com.sztx.se.dataaccess.redis.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.sztx.se.dataaccess.redis.BaseRedisDAO;
import com.sztx.se.dataaccess.redis.serialize.RedisSerializer;
import com.sztx.se.dataaccess.redis.source.DynamicRedisSource;

/**
 * 
 * @author zhihongp
 * 
 */
public abstract class BaseRedisDAOImpl implements BaseRedisDAO {

	@Autowired(required = false)
	protected DynamicRedisSource redisTemplate;

	@Override
	public byte[] serialize(Object obj) {
		return RedisSerializer.serialize(obj);
	}

	@Override
	public <T> byte[] serialize(T obj, org.springframework.data.redis.serializer.RedisSerializer<T> redisSerializer) {
		return RedisSerializer.serialize(obj, redisSerializer);
	}

	@Override
	public <T> T deserialize(Class<T> type, byte[] bytes) {
		return RedisSerializer.deserialize(type, bytes);
	}

	@Override
	public <T> T deserialize(Class<T> type, byte[] bytes, org.springframework.data.redis.serializer.RedisSerializer<T> redisSerializer) {
		return RedisSerializer.deserialize(type, bytes, redisSerializer);
	}

}
