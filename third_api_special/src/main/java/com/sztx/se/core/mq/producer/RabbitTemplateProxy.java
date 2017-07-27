package com.sztx.se.core.mq.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitTemplateProxy {
	
	/**
	 * 目前全局broker统一管理, 暂不支持动态化
	 */
	private String mqProducerKey = "mq";
	
	private RabbitTemplate rabbitTemplate;

	private boolean isDefault;

	public RabbitTemplate getRabbitTemplate() {
		return rabbitTemplate;
	}

	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public String getMqProducerKey() {
		return mqProducerKey;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

}
