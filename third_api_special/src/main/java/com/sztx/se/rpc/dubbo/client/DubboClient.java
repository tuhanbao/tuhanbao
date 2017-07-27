package com.sztx.se.rpc.dubbo.client;

import org.springframework.beans.factory.BeanNameAware;

import com.sztx.se.rpc.dubbo.config.DubboConfigServer;

/**
 * Dubbo客户端
 * 
 * @author zhihongp
 *
 */
public class DubboClient implements BeanNameAware {

	/**
	 * 接口唯一标示(如果用spring管理则为bean的id或者name,
	 * 如果不用spring管理直接new对象的话则为该对象的toString()方法返回的字符串)
	 */
	private String beanId;

	/**
	 * 接口名
	 */
	private String interfaceName;

	/**
	 * 接口版本号
	 */
	private String version;

	/**
	 * 接口协议
	 */
	private String protocol;

	/**
	 * 服务消费超时时间，默认30秒
	 */
	private int timeout = 30000;

	/**
	 * 服务消费者的重试次数，超时或未响应都会触发吗，默认加上第一次一共3次(如果不需要重试请设为0)
	 */
	private int retries = 2;

	/**
	 * 注册配置中心
	 */
	private DubboConfigServer dubboConfigServer;

	/**
	 * 是否类加载校验，默认true（true-类加载阶段获取，false-第一次使用client时获取）
	 */
	private boolean isCheck = true;

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public Class<?> getInterfaceClass() {
		Class<?> interfaceClass = null;

		try {
			interfaceClass = Class.forName(interfaceName, true, Thread.currentThread().getContextClassLoader());
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}

		return interfaceClass;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public DubboConfigServer getDubboConfigServer() {
		return dubboConfigServer;
	}

	public void setDubboConfigServer(DubboConfigServer dubboConfigServer) {
		this.dubboConfigServer = dubboConfigServer;
	}

	public boolean getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public String getBeanId() {
		return beanId;
	}

	@Override
	public void setBeanName(String name) {
		this.beanId = name;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	@Override
	public String toString() {
		return "DubboClient [beanId=" + beanId + ", interfaceName=" + interfaceName + ", version=" + version + ", protocol=" + protocol + ", timeout="
				+ timeout + ", retries=" + retries + ", dubboConfigServer=" + dubboConfigServer + ", isCheck=" + isCheck + "]";
	}

}
