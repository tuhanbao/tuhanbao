package com.sztx.se.core.tbschedule.config;

import com.taobao.pamirs.schedule.strategy.TBScheduleManagerFactory;

/**
 * tbschedule调度器
 * 
 * @author zhihongp
 *
 */
public class ScheduleConfigServer {

	/**
	 * 调度器key, 目前全局zk管理, 暂不支持动态化
	 */
	private String configServerKey = "tbschedule";

	/**
	 * 应用名(又名rootPath, 需全局唯一)
	 */
	private String applicationName;

	/**
	 * 调度地址(使用zookeeper调度，包括ip:port)
	 */
	private String serverAddress;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * session超时时间
	 */
	private int timeout = 30000;

	/**
	 * 是否校验父节点
	 */
	private boolean isCheckParentPath = true;

	/**
	 * 是否启动调度器(默认true)
	 */
	private boolean autoStartup = true;

	/**
	 * 是否默认
	 */
	private boolean isDefault;

	private TBScheduleManagerFactory scheduleManagerFactory;

	public String getConfigServerKey() {
		return configServerKey;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public boolean getAutoStartup() {
		return autoStartup;
	}

	public void setAutoStartup(boolean autoStartup) {
		this.autoStartup = autoStartup;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public boolean getIsCheckParentPath() {
		return isCheckParentPath;
	}

	public void setIsCheckParentPath(boolean isCheckParentPath) {
		this.isCheckParentPath = isCheckParentPath;
	}

	public TBScheduleManagerFactory getScheduleManagerFactory() {
		return scheduleManagerFactory;
	}

	public void setScheduleManagerFactory(TBScheduleManagerFactory scheduleManagerFactory) {
		this.scheduleManagerFactory = scheduleManagerFactory;
	}

	@Override
	public String toString() {
		return "ScheduleConfigServer [configServerKey=" + configServerKey + ", applicationName=" + applicationName + ", serverAddress=" + serverAddress
				+ ", username=" + username + ", password=" + password + ", timeout=" + timeout + ", isCheckParentPath=" + isCheckParentPath + ", autoStartup="
				+ autoStartup + ", isDefault=" + isDefault + ", scheduleManagerFactory=" + scheduleManagerFactory + "]";
	}

}
