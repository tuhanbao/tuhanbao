package com.sztx.se.dataaccess.hbase.template;

import org.springframework.data.hadoop.hbase.HbaseTemplate;


/**
 * Hbase的操作模板代理
 * 
 * @author zhihongp
 * 
 */
public class HbaseTemplateProxy {

	private String hbaseSourceKey;

	private HbaseTemplate hbaseTemplate;

	private boolean isDefault;

	public String getHbaseSourceKey() {
		return hbaseSourceKey;
	}

	public void setHbaseSourceKey(String hbaseSourceKey) {
		this.hbaseSourceKey = hbaseSourceKey;
	}

	public HbaseTemplate getHbaseTemplate() {
		return hbaseTemplate;
	}

	public void setHbaseTemplate(HbaseTemplate hbaseTemplate) {
		this.hbaseTemplate = hbaseTemplate;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

}
