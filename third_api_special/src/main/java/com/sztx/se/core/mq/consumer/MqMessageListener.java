package com.sztx.se.core.mq.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tuhanbao.base.util.io.IOUtil;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.TimeUtil;

/**
 * 消息处理器
 * 
 * @author zhihongp
 * 
 * @param <T>
 */
public abstract class MqMessageListener implements MessageListener {

	private static boolean openLog;

	private String queue;

	public static void setOpenLog(boolean openLog) {
		MqMessageListener.openLog = openLog;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public abstract Object handleMessage(String messageId, String messageContent, String exchange, String routingKey, String queue);

	@Override
	public void onMessage(Message message) {
		if (openLog) {
			long startTime = System.currentTimeMillis();
			long endTime = 0;
			Object obj = null;
			String messageContent = "";
			String exchange = "";
			String routingKey = "";
			String messageId = "";

			try {
				MessageProperties messageProperties = message.getMessageProperties();
				messageId = messageProperties.getMessageId();
				messageContent = new String(message.getBody(), IOUtil.DEFAULT_CHARSET);
				exchange = messageProperties.getReceivedExchange();
				routingKey = messageProperties.getReceivedRoutingKey();
				obj = handleMessage(messageId, messageContent, exchange, routingKey, queue);
			} catch (Throwable t) {
				obj = t.getClass().getCanonicalName() + ":" + t.getMessage();
				LogManager.error(t);
			} finally {
				String mqResult = "";

				if (obj != null) {
					mqResult = JSON.toJSONStringWithDateFormat(obj, TimeUtil.MAX_LONG_DATE_FORMAT_STR, SerializerFeature.DisableCircularReferenceDetect);
				}

				endTime = (endTime == 0 ? System.currentTimeMillis() : endTime);
				// 打印日志
				String messageLog = getMessageLog(messageId, messageContent, exchange, routingKey, queue, mqResult, startTime, endTime);
				LogManager.info(messageLog);
			}
		} else {
			try {
				MessageProperties messageProperties = message.getMessageProperties();
				String messageId = messageProperties.getMessageId();
				String messageContent = new String(message.getBody(), IOUtil.DEFAULT_CHARSET);
				String exchange = messageProperties.getReceivedExchange();
				String routingKey = messageProperties.getReceivedRoutingKey();
				handleMessage(messageId, messageContent, exchange, routingKey, queue);
			} catch (Throwable t) {
				LogManager.error(t);
			}
		}
	}

	private String getMessageLog(String messageId, String messageContent, String exchange, String routingKey, String queue, String mqResult, long startTime,
			long endTime) {
		long cost = endTime - startTime;
		String startTimeStr = TimeUtil.getTimeStr(TimeUtil.MAX_LONG_DATE_FORMAT_STR, startTime);
		String endTimeStr = TimeUtil.getTimeStr(TimeUtil.MAX_LONG_DATE_FORMAT_STR, endTime);
		return String
				.format("[RabbitMqConsumer] Receive mq message, messageId:%s|messageContent:%s|exchange:%s|routingKey:%s|queue:%s|result:%s|[start:%s, end:%s, cost:%dms]",
						messageId, messageContent, exchange, routingKey, queue, mqResult, startTimeStr, endTimeStr, cost);
	}

}