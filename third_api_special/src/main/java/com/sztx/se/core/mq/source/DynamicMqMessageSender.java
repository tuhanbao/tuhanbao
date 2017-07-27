package com.sztx.se.core.mq.source;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sztx.se.core.mq.config.MqConstants;
import com.sztx.se.core.mq.producer.MqMessageSender;
import com.sztx.se.core.mq.producer.RabbitTemplateProxy;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.config.ConfigPattern;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.io.IOUtil;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.ReflectUtil;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.base.util.objutil.TimeUtil;
import com.tuhanbao.base.util.objutil.UUIDUtil;

public class DynamicMqMessageSender implements MqMessageSender {

	private static final String SUCCESS = "success";

	private static final MessageDeliveryMode DEFAULT_MESSAGE_DELIVERY = MessageDeliveryMode.PERSISTENT;

	private RabbitTemplateProxy defaultTargetRabbitTemplate;

	private Map<String, RabbitTemplateProxy> targetRabbitTemplates;

	/**
	 * 日志开关，默认为false不打开
	 */
	private boolean openLog = false;

	/**
	 * 日志最大长度，如果不传则默认1000，传-1则不限制日志打印长度
	 */
	private int logLength;

	public void setDefaultTargetRabbitTemplate(RabbitTemplateProxy defaultTargetRabbitTemplate) {
		this.defaultTargetRabbitTemplate = defaultTargetRabbitTemplate;
	}

	public void setTargetRabbitTemplates(Map<String, RabbitTemplateProxy> targetRabbitTemplates) {
		this.targetRabbitTemplates = targetRabbitTemplates;
	}

	public void setOpenLog(boolean openLog) {
		this.openLog = openLog;
	}

	public void setLogLength(int logLength) {
		this.logLength = logLength;
	}

	@Override
	public String sendMessage(final Object messageObj) {
		return sendMessage(null, null, messageObj, true);
	}

	@Override
	public String sendMessage(String exchange, final Object messageObj) {
		return sendMessage(exchange, null, messageObj, false);
	}

	@Override
	public String sendMessage(String exchange, String routingKey, final Object messageObj) {
		return sendMessage(exchange, routingKey, messageObj, false);
	}

	public void afterPropertiesSet() {
		Set<Entry<String, RabbitTemplateProxy>> set = targetRabbitTemplates.entrySet();

		for (Map.Entry<String, RabbitTemplateProxy> entry : set) {
			RabbitTemplateProxy rabbitTemplateProxy = entry.getValue();

			if (rabbitTemplateProxy != null) {
				RabbitTemplate rabbitTemplate = rabbitTemplateProxy.getRabbitTemplate();
				String exchange = (String) ReflectUtil.getFieldValue(rabbitTemplate, "exchange");
				String newExchange = getNewExchange(exchange);
				rabbitTemplate.setExchange(newExchange);

				rabbitTemplate.setConfirmCallback(new ConfirmCallback() {
					@Override
					public void confirm(CorrelationData correlationData, boolean ack) {
						String correlationDataId = correlationData.getId();

						if (ack) {
							LogManager.info("Mq message " + correlationDataId + " send success");
						} else {
						    LogManager.info("Mq message " + correlationDataId + " send failure");
						}
					}
				});
			}
		}
	}

	public void initMqLog() {
		this.setOpenLog(openLog);
		this.setLogLength(logLength);
	}

	private String sendMessage(String exchange, String routingKey, final Object messageObj, boolean useDefault) {
		RabbitTemplate rabbitTemplate = getRabbitTemplate();

		if (rabbitTemplate == null) {
			rabbitTemplate = defaultTargetRabbitTemplate.getRabbitTemplate();
		}

		if (rabbitTemplate == null) {
			throw new MyException("Can not get a rabbitTemplate!");
		}

		if (useDefault) {
			exchange = (String) ReflectUtil.getFieldValue(rabbitTemplate, "exchange");
			routingKey = (String) ReflectUtil.getFieldValue(rabbitTemplate, "routingKey");
		}

		if (exchange == null) {
			throw new MyException("Mq exchange must not null!");
		}

		String newExchange = getNewExchange(exchange);
		rabbitTemplate.setExchange(newExchange);
		boolean flag = false;
		final String messageId = UUIDUtil.getJustNumUUID();

		if (openLog) {
			long startTime = System.currentTimeMillis();
			long endTime = 0;
			String messageContent = "";
			Object obj = null;

			try {
				messageContent = JSON.toJSONStringWithDateFormat(messageObj, TimeUtil.MAX_LONG_DATE_FORMAT_STR,
						SerializerFeature.DisableCircularReferenceDetect);
				CorrelationData correlationData = new CorrelationData(messageId);
				rabbitTemplate.convertAndSend(newExchange, routingKey, messageObj, new MessagePostProcessor() {
					@Override
					public Message postProcessMessage(Message message) throws AmqpException {
						MessageProperties messageProperties = message.getMessageProperties();
						messageProperties.setMessageId(messageId);
						messageProperties.setContentEncoding(IOUtil.DEFAULT_CHARSET);
						messageProperties.setDeliveryMode(DEFAULT_MESSAGE_DELIVERY);
						return message;
					}
				}, correlationData);

				obj = SUCCESS;
				flag = true;
			} catch (Throwable t) {
				obj = t.getClass().getCanonicalName() + ":" + t.getMessage();
				LogManager.error(t);
				throw t;
			} finally {
				String mqResult = "";
				String messageIdStr = "";

				if (obj != null) {
					mqResult = JSON.toJSONStringWithDateFormat(obj, TimeUtil.MAX_LONG_DATE_FORMAT_STR, SerializerFeature.DisableCircularReferenceDetect);
				}

				if (flag) {
					messageIdStr = messageId;
				}

				endTime = (endTime == 0 ? System.currentTimeMillis() : endTime);
				// 打印日志
				String messageLog = getMessageLog(messageIdStr, messageContent, newExchange, routingKey, mqResult, startTime, endTime);

				LogManager.info(messageLog);
			}
		} else {
			CorrelationData correlationData = new CorrelationData(messageId);
			rabbitTemplate.convertAndSend(newExchange, routingKey, messageObj, new MessagePostProcessor() {
				@Override
				public Message postProcessMessage(Message message) throws AmqpException {
					MessageProperties messageProperties = message.getMessageProperties();
					messageProperties.setMessageId(messageId);
					messageProperties.setContentEncoding(IOUtil.DEFAULT_CHARSET);
					messageProperties.setDeliveryMode(DEFAULT_MESSAGE_DELIVERY);
					return message;
				}
			}, correlationData);

			flag = true;
		}

		if (flag) {
			return messageId;
		} else {
			return null;
		}
	}

	private String getMessageLog(String messageId, String messageContent, String exchange, String routingKey, String mqResult, long startTime, long endTime) {
		long cost = endTime - startTime;
		String startTimeStr = TimeUtil.getTimeStr(TimeUtil.MAX_LONG_DATE_FORMAT_STR, startTime);
		String endTimeStr = TimeUtil.getTimeStr(TimeUtil.MAX_LONG_DATE_FORMAT_STR, endTime);
		return String.format(
				"[RabbitMqProducer] Send mq message, messageId:%s|exchange:%s|routingKey:%s|messageContent:%s|result:%s|[start:%s, end:%s, cost:%dms]",
				messageId, exchange, routingKey, messageContent, mqResult, startTimeStr, endTimeStr, cost);
	}

	private RabbitTemplate getRabbitTemplate() {
		String mqMessageSenderType = MqMessageSenderSwitcher.getMqMessageSenderType();
		RabbitTemplate rabbitTemplate = null;

		if (!StringUtil.isEmpty(mqMessageSenderType)) {
			RabbitTemplateProxy rabbitTemplateProxy = targetRabbitTemplates.get(mqMessageSenderType);

			if (rabbitTemplateProxy != null) {
				rabbitTemplate = rabbitTemplateProxy.getRabbitTemplate();
			}
		}

		if (rabbitTemplate == null) {
			rabbitTemplate = defaultTargetRabbitTemplate.getRabbitTemplate();
		}

		if (rabbitTemplate == null) {
			throw new MyException("Can not get a rabbitTemplate!");
		}

		return rabbitTemplate;
	}

	private String getNewExchange(String exchange) {
		String newExchange = exchange;

		if (!StringUtil.isEmpty(exchange)) {
		    if (ConfigManager.getCurrentConfigPattern() == ConfigPattern.PRE) {
				newExchange = exchange + MqConstants.MQ_PRE_FLAG;
			} else if (ConfigManager.getCurrentConfigPattern() == ConfigPattern.GRAY) {
				newExchange = exchange + MqConstants.MQ_GRAY_FLAG;
			}
		}

		return newExchange;
	}
}
