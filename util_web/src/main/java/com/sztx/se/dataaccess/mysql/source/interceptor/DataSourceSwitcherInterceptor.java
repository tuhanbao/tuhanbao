package com.sztx.se.dataaccess.mysql.source.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sztx.se.dataaccess.mysql.annotation.DynamicDataSource;
import com.sztx.se.dataaccess.mysql.source.DataSourceSwitcher;
import com.sztx.se.dataaccess.mysql.util.DataSourceUtil;

/**
 * 动态数据源拦截器
 * 
 * @author zhihongp
 *
 */
@Aspect
@Component
public class DataSourceSwitcherInterceptor {

	@Autowired(required = false)
	private com.sztx.se.dataaccess.mysql.source.DynamicDataSource dynamicDataSource;

	@Pointcut("@annotation(com.sztx.se.dataaccess.mysql.annotation.DynamicDataSource)")
	private void pointcutDynamicDataSource() {
	}

	@Around(value = "pointcutDynamicDataSource()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		if (this.dynamicDataSource == null) {
			return pjp.proceed();
		}

		DynamicDataSource dynamicDataSource = getLogAnnotation(pjp, DynamicDataSource.class);

		if (dynamicDataSource == null) {
			return pjp.proceed();
		}

		String currentDataSource = DataSourceUtil.getCurrentDataSource(this.dynamicDataSource);
		boolean currentReadWriteSeparate = DataSourceUtil.getCurrentReadWriteSeparate();
		String dataSourceType = dynamicDataSource.dataSourceType();
		boolean forceMaster = dynamicDataSource.forceMaster();
		// String serviceMethod = pjp.getTarget().getClass().getName() + "." +
		// pjp.getSignature().getName();

		if (forceMaster) {
			DataSourceSwitcher.setDataSourceTypeForceMaster(dataSourceType);
		} else {
			DataSourceSwitcher.setDataSourceTypeInContext(dataSourceType);
		}

		Object ret = pjp.proceed();

		if (currentReadWriteSeparate) {
			DataSourceSwitcher.setDataSourceTypeInContext(currentDataSource);
		} else {
			DataSourceSwitcher.setDataSourceTypeForceMaster(currentDataSource);
		}

		return ret;
	}  
	
	public static <T extends Annotation> T getLogAnnotation(JoinPoint jp, Class<T> type) {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        Method targetMethod = methodSignature.getMethod();

        return targetMethod.getAnnotation(type);
    }

}
