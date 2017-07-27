package com.sztx.se.dataaccess.mysql.client;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;

import com.tuhanbao.base.util.exception.MyException;

public class InterceptorUtil {

	private static SqlSessionFactoryBean sqlSessionFactoryBean;

	public static void setSqlSessionFactoryBean(SqlSessionFactoryBean sqlSessionFactoryBean) {
		if (InterceptorUtil.sqlSessionFactoryBean == null) {
			InterceptorUtil.sqlSessionFactoryBean = sqlSessionFactoryBean;
		}
	}

	public static void dynamicAddInterceptor(String typeClass) {
		try {
			SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
			Configuration configuration = sqlSessionFactory.getConfiguration();
			TypeAliasRegistry typeAliasRegistry = configuration.getTypeAliasRegistry();
			Interceptor interceptor = (Interceptor) resolveClass(typeClass, typeAliasRegistry).newInstance();
			configuration.addInterceptor(interceptor);
		} catch (Exception e) {
			throw MyException.getMyException(e);
		}
	}

	private static Class<?> resolveClass(String alias, TypeAliasRegistry typeAliasRegistry) {
		if (alias == null)
			return null;
		try {
			return resolveAlias(alias, typeAliasRegistry);
		} catch (Exception e) {
			throw MyException.getMyException(e);
		}
	}

	private static Class<?> resolveAlias(String alias, TypeAliasRegistry typeAliasRegistry) {
		return typeAliasRegistry.resolveAlias(alias);
	}
}
