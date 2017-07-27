package com.sztx.se.core.mq.producer;

/**
 * 消息发送器
 * 
 * @author zhihongp
 * 
 */
public interface MqMessageSender {

	/**
	 * 发送消息(使用默认的exchange和默认的routingKey, 默认UTF-8编码，默认消息持久化)
	 * 
	 * @param message
	 * @return messageId(失败返回null)
	 */
	String sendMessage(final Object message);

	/**
	 * 发送消息到指定的exchange，指定的routingKey
	 * (当routingKey为null时相当于fanout到所有监听此exchange的queue中, 默认UTF-8编码，默认消息持久化)
	 * 
	 * @param exchange
	 * @param routingKey
	 * @param message
	 * @return messageId(失败返回null)
	 */
	String sendMessage(String exchange, String routingKey, final Object message);

	/**
	 * 发送消息到指定的exchange，然后fanout到所有监听此exchange的queue中(默认UTF-8编码，默认消息持久化)
	 * 
	 * @param exchange
	 * @param message
	 * @return messageId(失败返回null)
	 */
	String sendMessage(String exchange, final Object message);

}
