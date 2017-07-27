package com.sztx.se.core.quartz.config;

import java.util.Properties;

import org.quartz.impl.StdSchedulerFactory;

/**
 * 
 * @author zhihongp
 *
 */
public class QuartzConfigServer {

	/**
	 * 目前全局数据库管理，暂不支持动态化
	 */
	private String configServerKey = "quartz";

	/**
	 * 应用名(quart任务前缀, 需全局唯一)
	 */
	private String applicationName;

	private Properties quartzProperties;

	private StdSchedulerFactory stdSchedulerFactory;

	/**
	 * 目前全局数据库管理，暂不支持动态化
	 */
	private String dataSourceKey = "quartz";

	private boolean autoStartup = true;

	private boolean isDefault;

	public String getConfigServerKey() {
		return configServerKey;
	}

	public Properties getQuartzProperties() {
		return quartzProperties;
	}

	public void setQuartzProperties(Properties quartzProperties) {
		this.quartzProperties = quartzProperties;
	}

	public boolean getAutoStartup() {
		return autoStartup;
	}

	public void setAutoStartup(boolean autoStartup) {
		this.autoStartup = autoStartup;
	}

	public StdSchedulerFactory getStdSchedulerFactory() {
		return stdSchedulerFactory;
	}

	public void setStdSchedulerFactory(StdSchedulerFactory stdSchedulerFactory) {
		this.stdSchedulerFactory = stdSchedulerFactory;
	}

	public String getDataSourceKey() {
		return dataSourceKey;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	@Override
	public String toString() {
		return "QuartzConfigServer [configServerKey=" + configServerKey + ", applicationName=" + applicationName + ", quartzProperties=" + quartzProperties
				+ ", stdSchedulerFactory=" + stdSchedulerFactory + ", dataSourceKey=" + dataSourceKey + ", autoStartup=" + autoStartup + ", isDefault="
				+ isDefault + "]";
	}

}
