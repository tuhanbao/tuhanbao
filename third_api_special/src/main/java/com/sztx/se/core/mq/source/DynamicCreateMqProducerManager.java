package com.sztx.se.core.mq.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sztx.se.core.context.SpringContextHolder;
import com.sztx.se.core.mq.producer.RabbitTemplateProxy;

/**
 * 
 * @author zhihongp
 * 
 */
public class DynamicCreateMqProducerManager {

	private DynamicMqMessageSender dynamicMqMessageSender;

	private List<RabbitTemplateProxy> rabbitTemplateProxyList;

	public void setRabbitTemplateProxyList(List<RabbitTemplateProxy> rabbitTemplateProxyList) {
		this.rabbitTemplateProxyList = rabbitTemplateProxyList;
	}

	public void setDynamicMqMessageSender(DynamicMqMessageSender dynamicMqMessageSender) {
		this.dynamicMqMessageSender = dynamicMqMessageSender;
	}

	/**
	 * 初始化mq
	 * 
	 * @param applicationContext
	 */
	public void initCreateMqProducer() {
		registerMqProducer();
	}

	/**
	 * 
	 */
	private void registerMqProducer() {
		Map<String, RabbitTemplateProxy> targetRabbitTemplates = new HashMap<String, RabbitTemplateProxy>();
		List<RabbitTemplateProxy> rabbitTemplateProxyList = new ArrayList<RabbitTemplateProxy>();
		RabbitTemplateProxy defaultTargetRabbitTemplate = null;

		if (this.rabbitTemplateProxyList == null || this.rabbitTemplateProxyList.isEmpty()) {
			Map<String, RabbitTemplateProxy> rabbitTemplateProxyMap = SpringContextHolder.applicationContext.getBeansOfType(RabbitTemplateProxy.class);

			if (rabbitTemplateProxyMap != null && !rabbitTemplateProxyMap.isEmpty()) {
				for (Entry<String, RabbitTemplateProxy> en : rabbitTemplateProxyMap.entrySet()) {
					rabbitTemplateProxyList.add(en.getValue());
				}
			}
		} else {
			rabbitTemplateProxyList = this.rabbitTemplateProxyList;
		}

		for (RabbitTemplateProxy rabbitTemplateProxy : rabbitTemplateProxyList) {
			String mqProducerKey = rabbitTemplateProxy.getMqProducerKey();
			targetRabbitTemplates.put(mqProducerKey, rabbitTemplateProxy);
			boolean isDefault = rabbitTemplateProxy.getIsDefault();

			if (isDefault) {
				defaultTargetRabbitTemplate = rabbitTemplateProxy;
			}
		}

		dynamicMqMessageSender.setTargetRabbitTemplates(targetRabbitTemplates);
		dynamicMqMessageSender.setDefaultTargetRabbitTemplate(defaultTargetRabbitTemplate);
		dynamicMqMessageSender.initMqLog();
		dynamicMqMessageSender.afterPropertiesSet();
	}
}
