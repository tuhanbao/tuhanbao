package com.sztx.se.dataaccess.mysql.ddl.interceptor;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.StringUtils;

import com.sztx.se.dataaccess.mysql.ddl.DdlConfig;
import com.sztx.se.dataaccess.mysql.ddl.DdlTable;
import com.sztx.se.dataaccess.mysql.source.DataSourceSwitcher;
import com.sztx.se.dataaccess.mysql.source.DynamicDataSource;
import com.sztx.se.dataaccess.mysql.util.DataSourceUtil;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.base.util.other.ConsistenHashUtil;
import com.tuhanbao.web.db.SqlParserUtil;

/**
 * 
 * @author zhihongp
 * 
 */
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) })
public class DdlDbInterceptor extends DdlInterceptor implements Interceptor {

	public Object intercept(Invocation invocation) throws Throwable {
		if (ddlFlag) {
			MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
			Object parameter = invocation.getArgs()[1];
			DynamicDataSource dynamicDataSource = (DynamicDataSource) mappedStatement.getConfiguration().getEnvironment().getDataSource();
			BoundSql boundSql = mappedStatement.getBoundSql(parameter);
			String interceptSql = boundSql.getSql();
			List<String> tableList = getTableList(interceptSql);
			boolean ddlFlag = getDdlFlag(interceptSql, tableList);
			String ddlDataSource = null;

			if (ddlFlag) {
				String standardSql = SqlParserUtil.handleSql(interceptSql, mappedStatement, boundSql);
				String currentDataSource = DataSourceUtil.getCurrentDataSource(dynamicDataSource);
				boolean isSlaveDataSource = DataSourceUtil.isSlaveDataSource(currentDataSource);
				ddlDataSource = getDdlDataSource(standardSql, interceptSql, tableList, currentDataSource, isSlaveDataSource);
				
				if (ddlDataSource != null && !ddlDataSource.equals(currentDataSource)) {
					boolean currentReadWriteSeparate = DataSourceUtil.getCurrentReadWriteSeparate();
					
					if (currentReadWriteSeparate) {
						DataSourceSwitcher.setDataSourceTypeInContext(ddlDataSource);
					} else {
						DataSourceSwitcher.setDataSourceTypeForceMaster(ddlDataSource);
					}
					
					Object obj = invocation.proceed();
					
					if (currentReadWriteSeparate) {
						DataSourceSwitcher.setDataSourceTypeInContext(currentDataSource);
					} else {
						DataSourceSwitcher.setDataSourceTypeForceMaster(currentDataSource);
					}

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

	private String getDdlDataSource(String sql, String interceptSql, List<String> tableList, String currentDataSource, boolean isSlaveDataSource) {
		String ddlDb = null;
		String dataSource = null;
		Iterator<String> iterator = tableList.iterator();
		Set<String> ddlTableSet = DataSourceUtil.ddlConfigMap.keySet();

		while (iterator.hasNext() && ddlDb == null) {
			String table = iterator.next();

			if (ddlTableSet.contains(table)) {
				DdlConfig ddlConfig = DataSourceUtil.ddlConfigMap.get(table);

				if (ddlConfig == null) {
					continue;
				}

				String column = ddlConfig.getColumn();

				if (StringUtil.isEmpty(column)) {
					ddlDb = ddlConfig.getDb();
				} else {
					String db = ddlConfig.getDb();
					Integer dbNum = ddlConfig.getDbNum();

					if (StringUtil.isEmpty(db) && (dbNum == null || dbNum == 1)) {
						continue;
					} else if (dbNum == null || dbNum == 1) {
						ddlDb = db;
						continue;
					}

					ConsistenHashUtil<DdlTable> consistenHashUtil = DataSourceUtil.consistenHashUtilMap.get(table);

					if (consistenHashUtil != null) {
						Object columnValue = null;

						try {
							columnValue = getColumnValue(sql, column);
						} catch (Exception e) {
							LogManager.error("Get columnValue error", e);
						}

						if (columnValue != null) {
							DdlTable ddlTable = consistenHashUtil.get(columnValue);

							if (ddlTable != null) {
								Integer ddlDbNum = ddlTable.getDdlDbNum();

								if (ddlDbNum != null && ddlDbNum != 0) {
									if (StringUtil.isEmpty(db)) {
										String currentDb = DataSourceUtil.getCurrentDb(currentDataSource);

										if (!StringUtils.isEmpty(currentDb)) {
											ddlDb = currentDb + "_" + ddlDbNum;
										}
									} else {
										ddlDb = db + "_" + ddlDbNum;
									}
								}
							}
						}
					}
				}
			}
		}

		if (!StringUtils.isEmpty(ddlDb)) {
			dataSource = DataSourceUtil.getDataSourceByDb(ddlDb, isSlaveDataSource);
		}

		return dataSource;
	}

}