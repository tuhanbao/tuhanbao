package com.sztx.se.dataaccess.mysql.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态数据源注解，必须使用在事务打开之前，事务打开后无法切换非当前事务正在使用之外的数据源
 * 
 * @author zhihongp
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DynamicDataSource {

	/**
	 * 数据源
	 * 
	 * @return
	 */
	String dataSourceType();

	/**
	 * 是否强制走主库, 即本次线程生命周期内读请求都走主库,
	 * 默认为false(即使读写分离开关readWriteSeparateFlag为true读请求也会强制走主库)
	 * 
	 * @return
	 */
	boolean forceMaster() default false;

}
