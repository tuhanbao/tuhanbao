package com.sztx.se.dataaccess.redis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Documented
public @interface RedisJsonField {

	/**
	 * 是否序列化, 默认为true
	 * 
	 * @return
	 */
	boolean serialize() default true;

}