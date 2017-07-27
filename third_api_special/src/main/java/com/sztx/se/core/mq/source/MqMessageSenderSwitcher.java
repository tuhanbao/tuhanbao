package com.sztx.se.core.mq.source;

public class MqMessageSenderSwitcher {

	private static final ThreadLocal<String> mqMessageSenderContextHolder = new ThreadLocal<String>();

	public static void setMqMessageSenderTypeInContext(String mqMessageSenderContext) {
		mqMessageSenderContextHolder.set(mqMessageSenderContext);
	}

	public static String getMqMessageSenderType() {
		String mqMessageSenderType = (String) mqMessageSenderContextHolder.get();
		return mqMessageSenderType;
	}

	public static String getMqMessageSenderTypeFromContext() {
		String mqMessageSenderType = (String) mqMessageSenderContextHolder.get();
		return mqMessageSenderType;
	}

	public static void clearMqMessageSenderContextType() {
		mqMessageSenderContextHolder.remove();
	}

}
