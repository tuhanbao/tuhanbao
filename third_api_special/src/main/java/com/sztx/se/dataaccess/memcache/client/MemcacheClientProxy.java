package com.sztx.se.dataaccess.memcache.client;

public class MemcacheClientProxy {

	private String memcacheSourceKey;

	private XMemcachedClientProxy XMemcachedClientProxy;

	private boolean isDefault;

	public String getMemcacheSourceKey() {
		return memcacheSourceKey;
	}

	public void setMemcacheSourceKey(String memcacheSourceKey) {
		this.memcacheSourceKey = memcacheSourceKey;
	}

	public XMemcachedClientProxy getXMemcachedClientProxy() {
		return XMemcachedClientProxy;
	}

	public void setXMemcachedClientProxy(XMemcachedClientProxy xMemcachedClientProxy) {
		XMemcachedClientProxy = xMemcachedClientProxy;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

}
