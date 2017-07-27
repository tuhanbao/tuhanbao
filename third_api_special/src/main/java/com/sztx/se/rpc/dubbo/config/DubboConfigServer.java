package com.sztx.se.rpc.dubbo.config;

/**
 * 
 * @author zhihongp
 *
 */
public class DubboConfigServer {
	
	/**
	 * dubbo协议的全局端口，如果没有设置则使用每一个服务暴露的端口
	 */
	public static Integer dubboPort;
	
	/**
	 * 目前全局zk统一管理，暂不支持动态化
	 */
	private String configServerKey = "dubbo";

	private String applicationName;

	private String registryAddress;

	private String registryUsername;

	private String registryPassword;

	private String registryFile;

	private String monitor;

	private boolean isDefault;

	public String getConfigServerKey() {
		return configServerKey;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getRegistryAddress() {
		return registryAddress;
	}

	public void setRegistryAddress(String registryAddress) {
		this.registryAddress = registryAddress;
	}

	public String getRegistryUsername() {
		return registryUsername;
	}

	public void setRegistryUsername(String registryUsername) {
		this.registryUsername = registryUsername;
	}

	public String getRegistryPassword() {
		return registryPassword;
	}

	public void setRegistryPassword(String registryPassword) {
		this.registryPassword = registryPassword;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getRegistryFile() {
		return registryFile;
	}

	public void setRegistryFile(String registryFile) {
		this.registryFile = registryFile;
	}

	public String getMonitor() {
		return monitor;
	}

	public void setMonitor(String monitor) {
		this.monitor = monitor;
	}

	@Override
	public String toString() {
		return "DubboConfigServer [configServerKey=" + configServerKey + ", applicationName=" + applicationName + ", registryAddress=" + registryAddress
				+ ", registryUsername=" + registryUsername + ", registryPassword=" + registryPassword + ", registryFile=" + registryFile + ", monitor="
				+ monitor + ", isDefault=" + isDefault + "]";
	}

}