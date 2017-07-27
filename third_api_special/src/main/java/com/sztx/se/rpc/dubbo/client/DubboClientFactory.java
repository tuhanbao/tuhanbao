package com.sztx.se.rpc.dubbo.client;

/**
 * dubbo客户端工厂
 * 
 * @author zhihongp
 *
 */
public interface DubboClientFactory {

	/**
	 * 获取某个dubbo消费端
	 * 
	 * @param beanId dubbo客户端对应的beanId
	 * @return dubbo客户端
	 */
	<T> T getDubboClient(String beanId);

	/**
	 * 获取某个dubbo消费端
	 * 
	 * @param dubboClient dubbo客户端对应的bean
	 * @return dubbo客户端
	 */
	<T> T getDubboClient(DubboClient dubboClient);

}
