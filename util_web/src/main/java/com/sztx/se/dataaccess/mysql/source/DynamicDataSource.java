package com.sztx.se.dataaccess.mysql.source;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.sztx.se.dataaccess.mysql.client.InterceptorUtil;
import com.sztx.se.dataaccess.mysql.config.DynamicDb;
import com.sztx.se.dataaccess.mysql.ddl.DdlConfig;
import com.sztx.se.dataaccess.mysql.ddl.DdlTable;
import com.sztx.se.dataaccess.mysql.ddl.interceptor.DdlInterceptor;
import com.sztx.se.dataaccess.mysql.source.interceptor.RwsInterceptor;
import com.sztx.se.dataaccess.mysql.source.interceptor.SqlLogInterceptor;
import com.sztx.se.dataaccess.mysql.util.DataSourceUtil;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.other.ConsistenHashUtil;

/**
 * 动态数据源
 * 
 * @author zhihongp
 * 
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	private static final String DDL_DB_CLASS = "com.sztx.se.dataaccess.mysql.ddl.interceptor.DdlDbInterceptor";

	private static final String DDL_TABLE_CLASS = "com.sztx.se.dataaccess.mysql.ddl.interceptor.DdlTableInterceptor";

	private static final String RSW_SEPARATE_CLASS = "com.sztx.se.dataaccess.mysql.source.interceptor.RwsInterceptor";

	private static final String SQL_LOG_CLASS = "com.sztx.se.dataaccess.mysql.source.interceptor.SqlLogInterceptor";

	private static final String DEFAULT_DDL_FILE = "ddl-config.xml";

	private static Map<String, String> dataSourceDbMap;

	private static Map<String, DynamicDb> dbDataSourceMap;

	private Map<Object, Object> dataSourceMap;

	private String defaultTargetDataSourceKey;

	private String ddlFile;

	private boolean ddlFlag;

	/**
	 * 读写分离开关，默认为false(true-从库读取，false-主库读取)
	 */
	private boolean readWriteSeparateFlag = false;

	/**
	 * 日志开关，默认为false不打开
	 */
	private boolean openLog = false;

	/**
	 * 日志最大长度，如果不传则默认1000，传-1则不限制日志打印长度
	 */
	private int logLength;

	private String ignorePattern;

	private long slowLimit = 1000l;

	public Map<String, String> getDataSourceDbMap() {
		return dataSourceDbMap;
	}

	public void setDataSourceDbMap(Map<String, String> dataSourceDbMap) {
		DynamicDataSource.dataSourceDbMap = dataSourceDbMap;
	}

	public Map<String, DynamicDb> getDbDataSourceMap() {
		return dbDataSourceMap;
	}

	public void setDbDataSourceMap(Map<String, DynamicDb> dbDataSourceMap) {
		DynamicDataSource.dbDataSourceMap = dbDataSourceMap;
	}

	public void setDefaultTargetDataSourceKey(String defaultTargetDataSourceKey) {
		this.defaultTargetDataSourceKey = defaultTargetDataSourceKey;
	}

	public void setDataSourceMap(Map<Object, Object> dataSourceMap) {
		this.dataSourceMap = dataSourceMap;
	}

	public void setDdlFile(String ddlFile) {
		this.ddlFile = ddlFile;
	}

	public void setDdlFlag(boolean ddlFlag) {
		this.ddlFlag = ddlFlag;
	}

	public void setReadWriteSeparateFlag(boolean readWriteSeparateFlag) {
		this.readWriteSeparateFlag = readWriteSeparateFlag;
	}

	public void setOpenLog(boolean openLog) {
		this.openLog = openLog;
	}

	public void setLogLength(int logLength) {
		this.logLength = logLength;
	}

	public void setIgnorePattern(String ignorePattern) {
		this.ignorePattern = ignorePattern;
	}

	public void setSlowLimit(long slowLimit) {
		this.slowLimit = slowLimit;
	}

	@Override
	protected Object determineCurrentLookupKey() {
		return getCurrentDataSource();
	}

	/**
	 * 
	 * @return
	 */
	public String getCurrentDataSource() {
		String dataSource = DataSourceSwitcher.getDataSourceType();

		if (dataSource == null) {
			dataSource = defaultTargetDataSourceKey;
		}

		if (dataSource == null) {
			throw new MyException("Can not find a dataSource.");
		}

		return dataSource;
	}

	public Set<String> getDataSources() {
		return dataSourceDbMap.keySet();
	}

	public DataSource getDataSource(String dataSourceKey) {
		DataSource dataSource = (DataSource) dataSourceMap.get(dataSourceKey);
		return dataSource;
	}

	public void initSqlLog() {
		if (openLog) {
			SqlLogInterceptor.setOpenLog(openLog);
			SqlLogInterceptor.setSlowLimit(slowLimit);
			SqlLogInterceptor.setIgnorePattern(ignorePattern);
			
			//临时解决下编译错误
//			InterceptorUtil.setSqlSessionFactoryBean(SpringContextHolder.applicationContext.getBean(SqlSessionFactoryBean.class));
			
			InterceptorUtil.dynamicAddInterceptor(SQL_LOG_CLASS);
		}
	}

	public void initRswConfig() {
		if (readWriteSeparateFlag) {
			try {
				RwsInterceptor.setReadWriteSeparateFlag(readWriteSeparateFlag);
				//临时解决下编译错误
//				InterceptorUtil.setSqlSessionFactoryBean(SpringContextHolder.applicationContext.getBean(SqlSessionFactoryBean.class));
				
				InterceptorUtil.dynamicAddInterceptor(RSW_SEPARATE_CLASS);
			} catch (Exception e) {
				throw MyException.getMyException(e);
			}
		}
	}

	/**
	 * 初始化ddl配置，使用一致性Hash算法
	 * 
	 * @throws Exception
	 */
	public void initDdlConfig() {
		if (ddlFlag) {
			try {
				DdlInterceptor.setDdlFlag(ddlFlag);
				
				//临时解决下编译错误
//				InterceptorUtil.setSqlSessionFactoryBean(SpringContextHolder.applicationContext.getBean(SqlSessionFactoryBean.class));
				
				InterceptorUtil.dynamicAddInterceptor(DDL_DB_CLASS);
				InterceptorUtil.dynamicAddInterceptor(DDL_TABLE_CLASS);
				String ddlFile = StringUtils.isNotBlank(this.ddlFile) ? this.ddlFile : DEFAULT_DDL_FILE;
				URL url = Thread.currentThread().getContextClassLoader().getResource(ddlFile);

				if (url != null) {
					SAXReader saxReader = new SAXReader();
					Document document = null;

					if (isJarUrl(url)) {
						InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(ddlFile);
						document = saxReader.read(is);
					} else {
						document = saxReader.read(url);
					}

					Element root = document.getRootElement();
					Element tables = root.element("tables");
					List<?> nodes = tables.elements("table");
					Iterator<?> iterator = nodes.iterator();

					if (DataSourceUtil.ddlConfigMap == null) {
						DataSourceUtil.ddlConfigMap = new HashMap<String, DdlConfig>();
					}

					if (DataSourceUtil.dataSourceDbMap == null || DataSourceUtil.dataSourceDbMap.isEmpty() || DataSourceUtil.dbDataSourceMap == null
							|| DataSourceUtil.dbDataSourceMap.isEmpty()) {
						buildDataSourceDbMap();
					}

					List<DdlTable> ddlTableList = null;

					while (iterator.hasNext()) {
						ddlTableList = new ArrayList<DdlTable>();
						Element e = (Element) iterator.next();
						String table = e.attributeValue("table").trim();
						String column = e.attributeValue("column") == null ? null : e.attributeValue("column").trim();
						Integer tableNum = e.attributeValue("tableNum") == null ? null : Integer.valueOf(e.attributeValue("tableNum").trim());
						String db = e.attributeValue("db") == null ? null : e.attributeValue("db").trim();
						Integer dbNum = e.attributeValue("dbNum") == null ? null : Integer.valueOf(e.attributeValue("dbNum").trim());
						DataSourceUtil.ddlConfigMap.put(table, new DdlConfig(table, column, tableNum, db, dbNum));

						if (tableNum != null && tableNum > 1) {
							for (int i = 1; i <= tableNum; i++) {
								if (dbNum == null || dbNum == 1) {
									ddlTableList.add(new DdlTable(table + "_" + (i - 1), 0));
								} else {
									if (dbNum > tableNum) {
										throw new MyException("Ddl config error, tableNum should max than dbNum");
									}

									ddlTableList.add(new DdlTable(table + "_" + (i - 1), ((i - 1) / (tableNum / dbNum) + 1) - 1));
								}
							}

							if (DataSourceUtil.consistenHashUtilMap == null) {
								DataSourceUtil.consistenHashUtilMap = new HashMap<String, ConsistenHashUtil<DdlTable>>();
							}

							DataSourceUtil.consistenHashUtilMap.put(table, new ConsistenHashUtil<DdlTable>(ddlTableList));
						}
					}

					DataSourceUtil.tablePattern = DataSourceUtil.getTablePattern(DataSourceUtil.ddlConfigMap.keySet());
				}
			} catch (Exception e) {
				throw MyException.getMyException(e);
			}
		}
	}

	private boolean isJarUrl(URL url) {
		if (url.toString().indexOf("jar:file") != -1 || url.toString().indexOf("jar!") != -1) {
			return true;
		} else {
			return false;
		}
	}

	// private void buildDdlMap() {
	// DdlInterceptor.dataSourceDbMap = getDataSourceDbMap();
	// DdlInterceptor.dbDataSourceMap = getDbDataSourceMap();
	// }

	private void buildDataSourceDbMap() {
		DataSourceUtil.dataSourceDbMap = getDataSourceDbMap();
		DataSourceUtil.dbDataSourceMap = getDbDataSourceMap();
	}
}
