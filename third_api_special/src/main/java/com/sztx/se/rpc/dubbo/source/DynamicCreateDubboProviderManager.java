package com.sztx.se.rpc.dubbo.source;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sztx.se.core.context.SpringContextHolder;
import com.sztx.se.rpc.dubbo.config.DubboConfigServer;
import com.sztx.se.rpc.dubbo.provider.DubboService;

/**
 * 动态创建DubboProvider的工具类
 * 
 * @author zhihongp
 * 
 */
public class DynamicCreateDubboProviderManager {

	private DynamicDubboProvider dynamicDubboProvider;

	private List<DubboService> dubboServiceList;

	private List<DubboConfigServer> dubboConfigServerList;

	public void setDynamicDubboProvider(DynamicDubboProvider dynamicDubboProvider) {
		this.dynamicDubboProvider = dynamicDubboProvider;
	}

	public void setDubboServiceList(List<DubboService> dubboServiceList) {
		this.dubboServiceList = dubboServiceList;
	}

	public void setDubboConfigServerList(List<DubboConfigServer> dubboConfigServerList) {
		this.dubboConfigServerList = dubboConfigServerList;
	}

	/**
	 * 初始化dubbo的服务提供者
	 * 
	 * @param applicationContext
	 */
	public void initCreateDubboProvider() {
		registerDubboProvider();
	}

	/**
	 * 
	 * 
	 */
	private void registerDubboProvider() {
		List<DubboService> dubboServiceList = new ArrayList<DubboService>();
		List<DubboConfigServer> dubboConfigServerList = new ArrayList<DubboConfigServer>();
		DubboConfigServer defaultTargetDubboConfigServer = null;

		if (this.dubboServiceList == null || this.dubboServiceList.isEmpty()) {
			Map<String, DubboService> dubboServiceMap = SpringContextHolder.applicationContext.getBeansOfType(DubboService.class);

			if (dubboServiceMap != null && !dubboServiceMap.isEmpty()) {
				for (Entry<String, DubboService> en : dubboServiceMap.entrySet()) {
					dubboServiceList.add(en.getValue());
				}
			}
		} else {
			dubboServiceList = this.dubboServiceList;
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

		dynamicDubboProvider.setTargetDubboServiceList(dubboServiceList);
		dynamicDubboProvider.setDefaultTargetDubboConfigServer(defaultTargetDubboConfigServer);
		dynamicDubboProvider.initDubboLog();
		dynamicDubboProvider.afterPropertiesSet();
	}

}