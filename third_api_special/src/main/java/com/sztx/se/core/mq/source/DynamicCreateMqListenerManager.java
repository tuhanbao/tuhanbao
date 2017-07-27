package com.sztx.se.core.mq.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

import com.sztx.se.core.context.SpringContextHolder;
import com.sztx.se.core.mq.consumer.MqListenerContainerProxy;
import com.sztx.se.core.mq.consumer.MqMessageListener;
import com.tuhanbao.base.util.objutil.ReflectUtil;
import com.tuhanbao.base.util.objutil.StringUtil;

/**
 * 
 * @author zhihongp
 * 
 */
public class DynamicCreateMqListenerManager {

	private DynamicMqListenerContainer dynamicMqListenerContainer;

	private List<MqListenerContainerProxy> mqListenerContainerProxyList;

	public void setDynamicMqListenerContainer(DynamicMqListenerContainer dynamicMqListenerContainer) {
		this.dynamicMqListenerContainer = dynamicMqListenerContainer;
	}

	public void setMqListenerContainerProxyList(List<MqListenerContainerProxy> mqListenerContainerProxyList) {
		this.mqListenerContainerProxyList = mqListenerContainerProxyList;
	}

	/**
	 * 初始化mq
	 * 
	 * @param applicationContext
	 */
	public void initCreateMqListener() {
		registerMqListener();
	}

	/**
	 * 
	 */
	private void registerMqListener() {
		List<MqListenerContainerProxy> mqListenerContainerProxyList = new ArrayList<MqListenerContainerProxy>();

		if (this.mqListenerContainerProxyList == null || this.mqListenerContainerProxyList.isEmpty()) {
			Map<String, MqListenerContainerProxy> mqListenerContainerProxyMap = SpringContextHolder.applicationContext
					.getBeansOfType(MqListenerContainerProxy.class);

			if (mqListenerContainerProxyMap != null && !mqListenerContainerProxyMap.isEmpty()) {
				for (Entry<String, MqListenerContainerProxy> en : mqListenerContainerProxyMap.entrySet()) {
					mqListenerContainerProxyList.add(en.getValue());
				}
			}
		} else {
			mqListenerContainerProxyList = this.mqListenerContainerProxyList;
		}

		Map<String, MqListenerContainerProxy> targetMqListenerContainerProxyMap = new HashMap<String, MqListenerContainerProxy>();
		for (MqListenerContainerProxy mqListenerContainerProxy : mqListenerContainerProxyList) {
			SimpleMessageListenerContainer messageListenerContainer = mqListenerContainerProxy.getMessageListenerContainer();
			MessageListenerAdapter messageListenerAdapter = (MessageListenerAdapter) messageListenerContainer.getMessageListener();
			MqMessageListener mqMessageListener = (MqMessageListener) ReflectUtil.getFieldValue(messageListenerAdapter, "delegate");
			String consumerName = mqMessageListener.getClass().getCanonicalName();
			String queueName = mqMessageListener.getQueue();
			String key = null;
			
			if (StringUtil.isEmpty(mqMessageListener.getQueue())) {
				key = mqListenerContainerProxy.getMqListenerKey() + ":" + consumerName;
			} else {
				key = mqListenerContainerProxy.getMqListenerKey() + ":" + consumerName + ":" + queueName;
			}

			targetMqListenerContainerProxyMap.put(key, mqListenerContainerProxy);
		}

		dynamicMqListenerContainer.setTargetMqListenerContainerProxyMap(targetMqListenerContainerProxyMap);
		dynamicMqListenerContainer.initMqLog();
		dynamicMqListenerContainer.afterPropertiesSet();
	}

}
