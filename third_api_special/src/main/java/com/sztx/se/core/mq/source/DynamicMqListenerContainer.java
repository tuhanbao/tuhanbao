package com.sztx.se.core.mq.source;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

import com.sztx.se.core.mq.config.MqConstants;
import com.sztx.se.core.mq.consumer.MqListenerContainerProxy;
import com.sztx.se.core.mq.consumer.MqMessageListener;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.config.ConfigPattern;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.ReflectUtil;
import com.tuhanbao.base.util.objutil.StringUtil;

/**
 * 动态MqListenerContainer
 * 
 * @author zhihongp
 * 
 */
public class DynamicMqListenerContainer {

	private Map<String, MqListenerContainerProxy> targetMqListenerContainerProxy;

	/**
	 * 日志开关，默认为false不打开
	 */
	private boolean openLog = false;

	public void setTargetMqListenerContainerProxyMap(Map<String, MqListenerContainerProxy> targetMqListenerContainerProxy) {
		this.targetMqListenerContainerProxy = targetMqListenerContainerProxy;
	}

	public void setOpenLog(boolean openLog) {
		this.openLog = openLog;
	}

	public void afterPropertiesSet() {
		Set<Entry<String, MqListenerContainerProxy>> set = targetMqListenerContainerProxy.entrySet();

		for (Map.Entry<String, MqListenerContainerProxy> entry : set) {
			String key = entry.getKey();
			MqListenerContainerProxy mqListenerContainerProxy = entry.getValue();

			if (mqListenerContainerProxy != null) {
				SimpleMessageListenerContainer messageListenerContainer = mqListenerContainerProxy.getMessageListenerContainer();
				MessageListenerAdapter messageListenerAdapter = (MessageListenerAdapter) messageListenerContainer.getMessageListener();
				MqMessageListener mqMessageListener = (MqMessageListener) ReflectUtil.getFieldValue(messageListenerAdapter, "delegate");
				String queue = mqMessageListener.getQueue();
				String newQueue = getNewQueue(queue);
				mqMessageListener.setQueue(newQueue);
				String newKey = getNewKey(key, newQueue);

				if (StringUtil.isEmpty(newQueue)) {
					throw new MyException("Mq consumer " + newKey + " queue must not be null");
				}

				List<Queue> queues = new ArrayList<Queue>();
				queues.add(new Queue(newQueue, true, false, false));
				messageListenerContainer.setQueues(queues.toArray((new Queue[queues.size()])));
				boolean autoStartup = mqListenerContainerProxy.getAutoStartup();
				messageListenerContainer.setAutoStartup(autoStartup);
				startMqListener(newKey, mqListenerContainerProxy);
			}
		}
	}

	public void initMqLog() {
		MqMessageListener.setOpenLog(openLog);
	}

	private void startMqListener(String key, MqListenerContainerProxy mqListenerContainerProxy) {
		SimpleMessageListenerContainer messageListenerContainer = mqListenerContainerProxy.getMessageListenerContainer();

		if (messageListenerContainer != null) {
			boolean autoStartup = messageListenerContainer.isAutoStartup();

			if (autoStartup) {
				messageListenerContainer.start();
				LogManager.info("mq消费者[" + key + "] 自动启动");
			}
		}
	}

	private String getNewQueue(String queue) {
		String newQueue = queue;

		if (!StringUtil.isEmpty(queue)) {
			if (ConfigManager.getCurrentConfigPattern() == ConfigPattern.PRE) {
				newQueue = queue + MqConstants.MQ_PRE_FLAG;
			} else if (ConfigManager.getCurrentConfigPattern() == ConfigPattern.GRAY) {
				newQueue = queue + MqConstants.MQ_GRAY_FLAG;
			}
		}

		return newQueue;
	}

	private String getNewKey(String key, String newQueue) {
		String newKey = key;

		if (!StringUtil.isEmpty(key)) {
			String[] keyArray = key.split(":");

			if (keyArray.length > 2) {
				newKey = keyArray[0] + ":" + keyArray[1] + ":" + newQueue;
			}
		}

		return newKey;
	}
}
