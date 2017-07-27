package com.sztx.se.dataaccess.mysql.source.interceptor;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.sztx.se.dataaccess.mysql.source.DataSourceSwitcher;
import com.sztx.se.dataaccess.mysql.source.DynamicDataSource;
import com.sztx.se.dataaccess.mysql.util.DataSourceUtil;

/**
 * 
 * @author zhihongp
 * 
 */
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) })
public class RwsInterceptor implements Interceptor {

	private static boolean readWriteSeparateFlag;

	public static void setReadWriteSeparateFlag(boolean readWriteSeparateFlag) {
		RwsInterceptor.readWriteSeparateFlag = readWriteSeparateFlag;
	}

	public Object intercept(Invocation invocation) throws Throwable {
		if (readWriteSeparateFlag) {
			MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
			DynamicDataSource dynamicDataSource = (DynamicDataSource) mappedStatement.getConfiguration().getEnvironment().getDataSource();
			String currentDataSource = DataSourceUtil.getCurrentDataSource(dynamicDataSource);

			if (SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType()) && DataSourceUtil.getReadWriteSeparate()
					&& !DataSourceUtil.isSlaveDataSource(currentDataSource)) {
				String slaveDataSource = DataSourceUtil.getSlaveDataSourceByMasterDataSource(currentDataSource);

				if (slaveDataSource != null && !slaveDataSource.equals(currentDataSource)) {
					DataSourceSwitcher.setDataSourceTypeInContext(slaveDataSource);
					Object obj = invocation.proceed();
					DataSourceSwitcher.setDataSourceTypeInContext(currentDataSource);
					return obj;
				}
			}
		}

		Object obj = invocation.proceed();
		return obj;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
	}

}