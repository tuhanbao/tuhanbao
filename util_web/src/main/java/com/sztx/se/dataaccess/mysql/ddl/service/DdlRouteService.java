package com.sztx.se.dataaccess.mysql.ddl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sztx.se.dataaccess.mysql.ddl.DdlConfig;
import com.sztx.se.dataaccess.mysql.ddl.DdlRoute;
import com.sztx.se.dataaccess.mysql.ddl.DdlTable;
import com.sztx.se.dataaccess.mysql.source.DynamicDataSource;
import com.sztx.se.dataaccess.mysql.util.DataSourceUtil;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.base.util.other.ConsistenHashUtil;

/**
 * 
 * @author zhihongp
 *
 */
@Service("ddlRouteService")
public class DdlRouteService {

	@Autowired(required = false)
	private DynamicDataSource dynamicDataSource;

	public List<DdlConfig> getDdlConfigList() {
		List<DdlConfig> ddlConfigList = new ArrayList<DdlConfig>();

		for (Entry<String, DdlConfig> en : DataSourceUtil.ddlConfigMap.entrySet()) {
			DdlConfig ddlConfig = en.getValue();
			ddlConfigList.add(ddlConfig);
		}

		return ddlConfigList;
	}

	public DdlRoute getDdlRoute(String table, String columnValue) {
		DdlConfig ddlConfig = DataSourceUtil.ddlConfigMap.get(table);

		if (ddlConfig == null) {
			return null;
		}

		String column = ddlConfig.getColumn();
		String ddlDbName = null;
		String ddlTableName = null;

		if (StringUtils.isBlank(column)) {
			ddlDbName = ddlConfig.getDb();
		} else {
			String db = ddlConfig.getDb();
			Integer dbNum = ddlConfig.getDbNum();

			if (StringUtils.isBlank(db) && (dbNum == null || dbNum == 1)) {
				ddlDbName = DataSourceUtil.getCurrentDb(dynamicDataSource);
			} else if (dbNum == null || dbNum == 1) {
				ddlDbName = db;
			}

			ConsistenHashUtil<DdlTable> consistenHashUtil = DataSourceUtil.consistenHashUtilMap.get(table);

			if (consistenHashUtil != null) {
				if (columnValue != null) {
					DdlTable ddlTable = consistenHashUtil.get(columnValue);

					if (ddlTable != null) {
						Integer ddlDbNum = ddlTable.getDdlDbNum();

						if (ddlDbNum != null && ddlDbNum != 0) {
							if (StringUtils.isBlank(db)) {
								String currentDb = DataSourceUtil.getCurrentDb(dynamicDataSource);

								if (StringUtils.isNotBlank(currentDb)) {
									ddlDbName = currentDb + "_" + ddlDbNum;
								}
							} else {
								ddlDbName = db + "_" + ddlDbNum;
							}
						}
					}

					ddlTableName = ddlTable.getDdlTableName();
				}
			}
		}

		if (StringUtil.isEmpty(ddlDbName)) {
			ddlDbName = DataSourceUtil.getCurrentDb(dynamicDataSource);
		}

		return new DdlRoute(ddlDbName, ddlTableName, column, columnValue);
	}

}
