package com.sztx.se.dataaccess.redis;

/**
 * 
 * @author zhihongp
 * 
 */
public interface BaseRedisDAO {

	/**
	 * 序列化對象
	 * 
	 * @param obj
	 * @return
	 */
	byte[] serialize(Object obj);

	/**
	 * 使用指定序列化器序列化对象
	 * 
	 * @param obj
	 * @param redisSerializer
	 * @return
	 */
	<T> byte[] serialize(T obj, org.springframework.data.redis.serializer.RedisSerializer<T> redisSerializer);

	/**
	 * 反序列化对象
	 * 
	 * @param type
	 * @param bytes
	 * @return
	 */
	<T> T deserialize(Class<T> type, byte[] bytes);

	/**
	 * 使用指定序列化器反序列化对象
	 * 
	 * @param type
	 * @param bytes
	 * @param redisSerializer
	 * @return
	 */
	<T> T deserialize(Class<T> type, byte[] bytes, org.springframework.data.redis.serializer.RedisSerializer<T> redisSerializer);

}
