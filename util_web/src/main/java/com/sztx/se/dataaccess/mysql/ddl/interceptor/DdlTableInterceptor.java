package com.sztx.se.dataaccess.mysql.ddl.interceptor;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.springframework.util.StringUtils;

import com.sztx.se.dataaccess.mysql.ddl.DdlConfig;
import com.sztx.se.dataaccess.mysql.ddl.DdlTable;
import com.sztx.se.dataaccess.mysql.source.interceptor.SqlLogInterceptor;
import com.sztx.se.dataaccess.mysql.util.DataSourceUtil;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.ReflectUtil;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.base.util.other.ConsistenHashUtil;
import com.tuhanbao.web.db.SqlParserUtil;

/**
 * 动态分库分表拦截器
 * 
 * @author zhihongp
 * 
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class DdlTableInterceptor extends DdlInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if (ddlFlag) {
			RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
			BoundSql boundSql = handler.getBoundSql();
			String interceptSql = boundSql.getSql();
			List<String> tableList = getTableList(interceptSql);
			boolean ddlFlag = getDdlFlag(interceptSql, tableList);

			if (ddlFlag) {
				StatementHandler delegate = (StatementHandler) ReflectUtil.getFieldValue(handler, "delegate");
				MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getFieldValue(delegate, "mappedStatement");
				String standardSql = SqlParserUtil.handleSql(interceptSql, mappedStatement, boundSql);
				Map<String, String> ddlSqlMap = getDdlSqlMap(standardSql, interceptSql, tableList);
				String ddlSql = ddlSqlMap.get("ddlSql");
				String ddlStandardSql = ddlSqlMap.get("ddlStandardSql");

				if (!StringUtil.isEmpty(ddlSql)) {
					ReflectUtil.setFieldValue(boundSql, "sql", ddlSql);
					SqlLogInterceptor.setExecuteSql(ddlStandardSql);
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

	/**
	 * 获取ddl的db
	 * 
	 * @param sql
	 * @return
	 */
	private Map<String, String> getDdlSqlMap(String sql, String interceptSql, List<String> tableList) {
		// Pattern pattern = Pattern.compile(tablePattern,
		// Pattern.CASE_INSENSITIVE);
		// Matcher matcher = pattern.matcher(interceptSql);
		// StringBuffer sb = new StringBuffer();
		Map<String, String> ddlSqlMap = new HashMap<String, String>();
		String ddlSql = interceptSql;
		String ddlStandardSql = sql;
		Iterator<String> iterator = tableList.iterator();
		Set<String> ddlTableSet = DataSourceUtil.ddlConfigMap.keySet();

		while (iterator.hasNext()) {
			String table = iterator.next();

			if (ddlTableSet.contains(table)) {
				DdlConfig ddlConfig = DataSourceUtil.ddlConfigMap.get(table);

				if (ddlConfig == null) {
					continue;
				}

				String column = ddlConfig.getColumn();

				if (StringUtils.isEmpty(column)) {
					continue;
				}

				ConsistenHashUtil<DdlTable> consistenHashUtil = DataSourceUtil.consistenHashUtilMap.get(table);

				if (consistenHashUtil != null) {
					Object columnValue = null;

					try {
						columnValue = getColumnValue(sql, ddlConfig.getColumn());
					} catch (Exception e) {
						LogManager.error("Get columnValue error", e);
					}

					if (columnValue != null) {
						DdlTable ddlTable = consistenHashUtil.get(columnValue);
						String ddlTableName = ddlTable.getDdlTableName();
						String regex1 = "\\s+" + table + "\\s+";
						String regex2 = "\\s+" + table + "\\.";
						ddlSql = ddlSql.replaceAll(regex1, " " + ddlTableName + " ");
						ddlSql = ddlSql.replaceAll(regex2, " " + ddlTableName + ".");
						ddlStandardSql = ddlStandardSql.replaceAll(regex1, " " + ddlTableName + " ");
						ddlStandardSql = ddlStandardSql.replaceAll(regex2, " " + ddlTableName + ".");
					}
				}
			}
		}

		ddlSqlMap.put("ddlSql", ddlSql);
		ddlSqlMap.put("ddlStandardSql", ddlStandardSql);
		return ddlSqlMap;
	}

}
