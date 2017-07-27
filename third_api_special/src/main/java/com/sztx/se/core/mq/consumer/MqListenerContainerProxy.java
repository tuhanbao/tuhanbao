package com.sztx.se.core.mq.consumer;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

/**
 * 
 * @author zhihongp
 * 
 */
public class MqListenerContainerProxy {

	/**
	 * 目前全局broker统一管理, 暂不支持动态化
	 */
	private String mqListenerKey = "mq";

	private SimpleMessageListenerContainer messageListenerContainer;

	private boolean autoStartup = true;

	public String getMqListenerKey() {
		return mqListenerKey;
	}

	public SimpleMessageListenerContainer getMessageListenerContainer() {
		return messageListenerContainer;
	}

	public void setMessageListenerContainer(SimpleMessageListenerContainer messageListenerContainer) {
		this.messageListenerContainer = messageListenerContainer;
	}

	public boolean getAutoStartup() {
		return autoStartup;
	}

	public void setAutoStartup(boolean autoStartup) {
		this.autoStartup = autoStartup;
	}

	@Override
	public String toString() {
		return "MqListenerContainerProxy [mqListenerKey=" + mqListenerKey + ", messageListenerContainer=" + messageListenerContainer + ", autoStartup="
				+ autoStartup + "]";
	}

}
