package com.sztx.se.dataaccess.hbase.serialize;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sztx.se.dataaccess.hbase.annotation.HbaseJsonField;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.objutil.ReflectUtil;

public class HbaseSerializer {

	private static final String DEFAULT_CODE = "UTF-8";;

	/**
	 * 序列化(默认使用fastjson)
	 * 
	 * @param obj
	 * @return
	 */
	public static byte[] serialize(Object obj) {
		if (obj instanceof String) {
			return (obj == null ? null : ((String) obj).getBytes(getDefaultCharset()));
		} else {
			PropertyFilter filter = getPropertyFilter();
			return toJSONBytesWithFilter(obj, filter, SerializerFeature.SortField);
		}
	}

	/**
	 * 使用指定序列化器序列化(redisSerializer为空则用默认序列化器序列化)
	 * 
	 * @param obj
	 * @param redisSerializer
	 * @return
	 */
	public static byte[] serialize(Object obj, org.springframework.data.redis.serializer.RedisSerializer<Object> redisSerializer) {
		if (redisSerializer == null) {
			return serialize(obj);
		} else {
			if (obj instanceof String) {
				return (obj == null ? null : ((String) obj).getBytes(getDefaultCharset()));
			} else {
				return redisSerializer.serialize(obj);
			}
		}
	}

	/**
	 * 反序列化(默认使用fastjson)
	 * 
	 * @param type
	 * @param bytes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(Class<T> type, byte[] bytes) {
		if (type == String.class) {
			return (T) (bytes == null ? null : new String(bytes, getDefaultCharset()));
		} else {
			if (bytes == null) {
				return null;
			} else {
				return JSON.parseObject(bytes, type, Feature.SortFeidFastMatch);
			}
		}
	}

	/**
	 * 反序列化(默认使用fastjson)
	 * 
	 * @param type
	 * @param bytes
	 * @return
	 */
	public static <T> List<T> deserializeArray(Class<T> type, byte[] bytes) {
		try {
			return (List<T>) JSON.parseArray(new String(bytes, "UTF-8"), type);
		} catch (UnsupportedEncodingException e) {
			throw new MyException("DeserializeArray error");
		}
	}

	/**
	 * 使用指定序列化器反序列化(redisSerializer为空则用默认序列化器反序列化)
	 * 
	 * @param type
	 * @param bytes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(Class<T> type, byte[] bytes, org.springframework.data.redis.serializer.RedisSerializer<Object> redisSerializer) {
		if (redisSerializer == null) {
			return deserialize(type, bytes);
		} else {
			if (type == String.class) {
				return (T) (Object)(bytes == null ? null : new String(bytes, getDefaultCharset()));
			} else {
				if (bytes == null) {
					return null;
				} else {
					return (T) redisSerializer.deserialize(bytes);
				}
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
			return out.toBytes("UTF-8");
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

				HbaseJsonField cacheJsonField = field.getAnnotation(HbaseJsonField.class);

				if (cacheJsonField == null) {
					return true;
				} else {
					return cacheJsonField.serialize();
				}
			}
		};

		return filter;
	}

}
