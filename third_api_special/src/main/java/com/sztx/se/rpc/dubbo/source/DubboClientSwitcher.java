package com.sztx.se.rpc.dubbo.source;

public class DubboClientSwitcher {

	private static final ThreadLocal<String> dubboClientContextHolder = new ThreadLocal<String>();

	public static void setDubboClientTypeInContext(String dubboClientType) {
		dubboClientContextHolder.set(dubboClientType);
	}

	public static String getDubboClientType() {
		String dubboClientType = (String) dubboClientContextHolder.get();
		return dubboClientType;
	}

	public static String DubboClientType() {
		String dubboClientType = (String) dubboClientContextHolder.get();
		return dubboClientType;
	}

	public static void clearDubboClientType() {
		dubboClientContextHolder.remove();
	}

}
