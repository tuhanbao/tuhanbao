package com.sztx.se.rpc.dubbo.source;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sztx.se.core.context.SpringContextHolder;
import com.sztx.se.rpc.dubbo.client.DubboClient;
import com.sztx.se.rpc.dubbo.config.DubboConfigServer;

/**
 * 动态创建DubboClient的工具类
 * 
 * @author zhihongp
 * 
 */
public class DynamicCreateDubboClientManager {

	private DynamicDubboClient dynamicDubboClient;

	private List<DubboClient> dubboClientList;

	private List<DubboConfigServer> dubboConfigServerList;

	public void setDynamicDubboClient(DynamicDubboClient dynamicDubboClient) {
		this.dynamicDubboClient = dynamicDubboClient;
	}

	public void setDubboClientList(List<DubboClient> dubboClientList) {
		this.dubboClientList = dubboClientList;
	}

	public void setDubboConfigServerList(List<DubboConfigServer> dubboConfigServerList) {
		this.dubboConfigServerList = dubboConfigServerList;
	}

	/**
	 * 初始化dubbo的服务消费者
	 * 
	 * @param applicationContext
	 */
	public void initCreateDubboClient() {
		registerDubboClient();
	}

	/**
	 * 
	 * 
	 */
	private void registerDubboClient() {
		List<DubboClient> dubboClientList = new ArrayList<DubboClient>();
		List<DubboConfigServer> dubboConfigServerList = new ArrayList<DubboConfigServer>();
		DubboConfigServer defaultTargetDubboConfigServer = null;

		if (this.dubboClientList == null || this.dubboClientList.isEmpty()) {
			Map<String, DubboClient> dubboClientMap = SpringContextHolder.applicationContext.getBeansOfType(DubboClient.class);

			if (dubboClientMap != null && !dubboClientMap.isEmpty()) {
				for (Entry<String, DubboClient> en : dubboClientMap.entrySet()) {
					dubboClientList.add(en.getValue());
				}
			}
		} else {
			dubboClientList = this.dubboClientList;
		}

		if (this.dubboConfigServerList == null || this.dubboConfigServerList.isEmpty()) {
			Map<String, DubboConfigServer> dubboConfigServerMap = SpringContextHolder.applicationContext.getBeansOfType(DubboConfigServer.class);

			if (dubboConfigServerMap != null && !dubboConfigServerMap.isEmpty()) {
				for (Entry<String, DubboConfigServer> en : dubboConfigServerMap.entrySet()) {
					dubboConfigServerList.add(en.getValue());
				}
			}
		} else {
			dubboConfigServerList = this.dubboConfigServerList;
		}

		for (DubboConfigServer dubboConfigServer : dubboConfigServerList) {
			boolean isDefault = dubboConfigServer.getIsDefault();

			if (isDefault) {
				defaultTargetDubboConfigServer = dubboConfigServer;
			}
		}

		dynamicDubboClient.setTargetDubboClientList(dubboClientList);
		dynamicDubboClient.setDefaultTargetDubboConfigServer(defaultTargetDubboConfigServer);
		dynamicDubboClient.initDubboLog();
		dynamicDubboClient.afterPropertiesSet();
	}

}