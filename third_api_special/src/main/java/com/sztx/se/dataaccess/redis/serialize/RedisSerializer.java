package com.sztx.se.dataaccess.redis.serialize;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sztx.se.dataaccess.redis.annotation.RedisJsonField;
import com.tuhanbao.base.util.objutil.ReflectUtil;

public class RedisSerializer {

	private static final String DEFAULT_CODE = "UTF-8";;

	/**
	 * 序列化字符串(使用StringRedisSerializer，建议redis的key都使用此序列化)
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] serializeStr(String str) {
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer(getDefaultCharset());
		return stringRedisSerializer.serialize(str);
	}

	/**
	 * 序列化(默认使用fastjson)
	 * 
	 * @param obj
	 * @return
	 */
	public static byte[] serialize(Object obj) {
		PropertyFilter filter = getPropertyFilter();
		return toJSONBytesWithFilter(obj, filter, SerializerFeature.SortField);
	}

	/**
	 * 使用指定序列化器序列化(redisSerializer为空则用默认序列化器序列化)
	 * 
	 * @param <T>
	 * 
	 * @param obj
	 * @param redisSerializer
	 * @return
	 */
	public static <T> byte[] serialize(T obj, org.springframework.data.redis.serializer.RedisSerializer<T> redisSerializer) {
		if (redisSerializer == null) {
			return serialize(obj);
		} else {
			return redisSerializer.serialize(obj);
		}
	}
	
	/**
	 * 反序列化字符串(使用StringRedisSerializer，建议redis的key都使用此反序列化)
	 * @param bytes
	 * @return
	 */
	public static String deserializeStr(byte[] bytes) {
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer(getDefaultCharset());
		return stringRedisSerializer.deserialize(bytes);
	}

	/**
	 * 反序列化(默认使用fastjson)
	 * 
	 * @param type
	 * @param bytes
	 * @return
	 */
	public static <T> T deserialize(Class<T> type, byte[] bytes) {
		if (bytes == null) {
			return null;
		} else {
			return JSON.parseObject(bytes, type, Feature.SortFeidFastMatch);
		}
	}

	/**
	 * 使用指定序列化器反序列化(redisSerializer为空则用默认序列化器反序列化)
	 * 
	 * @param type
	 * @param bytes
	 * @return
	 */
	public static <T> T deserialize(Class<T> type, byte[] bytes, org.springframework.data.redis.serializer.RedisSerializer<T> redisSerializer) {
		if (redisSerializer == null) {
			return deserialize(type, bytes);
		} else {
			if (bytes == null) {
				return null;
			} else {
				return redisSerializer.deserialize(bytes);
			}
		}
	}

	private static Charset getDefaultCharset() {
		Charset charset = Charset.forName(DEFAULT_CODE);
		return charset;
	}

	private static final byte[] toJSONBytesWithFilter(Object object, PropertyFilter filter, SerializerFeature... features) {
		SerializeWriter out = new SerializeWriter();

		try {
			JSONSerializer serializer = new JSONSerializer(out);
			serializer.getPropertyFilters().add(filter);

			for (com.alibaba.fastjson.serializer.SerializerFeature feature : features) {
				serializer.config(feature, true);
			}

			serializer.write(object);
			return out.toBytes(DEFAULT_CODE);
		} finally {
			out.close();
		}
	}

	private static PropertyFilter getPropertyFilter() {
		PropertyFilter filter = new PropertyFilter() {
			@Override
			public boolean apply(Object object, String name, Object value) {
				Field field = ReflectUtil.getField(object, name);

				if (field == null) {
					return true;
				}

				RedisJsonField cacheJsonField = field.getAnnotation(RedisJsonField.class);

				if (cacheJsonField == null) {
					return true;
				} else {
					return cacheJsonField.serialize();
				}
			}
		};

		return filter;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer(getDefaultCharset());
		
		String a = "abc321";
		//byte[] b = RedisSerializer.serialize("abc321");
		//byte[] b = RedisSerializer.serialize(a, stringRedisSerializer);
		byte[] b = RedisSerializer.serialize(a, jdkSerializationRedisSerializer);
		
		//String c = RedisSerializer.deserialize(String.class, b);
		//String c = RedisSerializer.deserialize(String.class, b, stringRedisSerializer);
		String c = (String) RedisSerializer.deserialize(Object.class, b, jdkSerializationRedisSerializer);
		
		System.out.println(a);
		System.out.println(c);
	}
}
