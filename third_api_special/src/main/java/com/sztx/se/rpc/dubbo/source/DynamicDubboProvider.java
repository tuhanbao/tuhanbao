package com.sztx.se.rpc.dubbo.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.sztx.se.rpc.dubbo.config.DubboConfigServer;
import com.sztx.se.rpc.dubbo.config.DubboConstants;
import com.sztx.se.rpc.dubbo.extension.DubboExtensionLoader;
import com.sztx.se.rpc.dubbo.provider.DubboService;
import com.sztx.se.rpc.dubbo.provider.DubboServiceFactory;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.StringUtil;

/**
 * 动态DubboProvider
 * 
 * @author zhihongp
 * 
 */
public class DynamicDubboProvider {

	private DubboConfigServer defaultTargetDubboConfigServer;

	private List<DubboService> targetDubboServiceList;

	private Map<String, List<ServiceConfig<?>>> serviceConfigMap;

	private final static Integer PROTOCOL_THREADS = 100;

	/**
	 * 日志开关，默认为false不打开
	 */
	private boolean openLog = false;

	public Map<String, List<ServiceConfig<?>>> getServiceConfigMap() {
		return serviceConfigMap;
	}

	public void setDefaultTargetDubboConfigServer(DubboConfigServer defaultTargetDubboConfigServer) {
		this.defaultTargetDubboConfigServer = defaultTargetDubboConfigServer;
	}

	public void setTargetDubboServiceList(List<DubboService> targetDubboServiceList) {
		this.targetDubboServiceList = targetDubboServiceList;
	}

	public void setOpenLog(boolean openLog) {
		this.openLog = openLog;
	}

	public void afterPropertiesSet() {
		DubboExtensionLoader.loadExtension();
		serviceConfigMap = new HashMap<String, List<ServiceConfig<?>>>();

		for (DubboService dubboService : targetDubboServiceList) {
			DubboConfigServer dubboConfigServer = dubboService.getDubboConfigServer();

			if (dubboConfigServer == null) {
				dubboConfigServer = defaultTargetDubboConfigServer;
			}

			String configServerKey = dubboConfigServer.getConfigServerKey();

			// 当前应用配置
			ApplicationConfig application = new ApplicationConfig();
			application.setName(dubboConfigServer.getApplicationName());

			// 连接注册中心配置
			RegistryConfig registry = new RegistryConfig();
			registry.setAddress(dubboConfigServer.getRegistryAddress());
			registry.setUsername(dubboConfigServer.getRegistryUsername());
			registry.setPassword(dubboConfigServer.getRegistryPassword());

			if (!StringUtil.isEmpty(dubboConfigServer.getRegistryFile())) {
				registry.setFile(dubboConfigServer.getRegistryFile());
			}

			// 服务提供者协议配置
			List<ProtocolConfig> protocolList = new ArrayList<ProtocolConfig>();
			String[] protocolConfigs = dubboService.getProtocols().split(",");

			for (String protocolConfig : protocolConfigs) {
				LogManager.info("dubboService=" + dubboService.getInterfaceName());
				LogManager.info("protocolConfig=" + protocolConfig);
				String protocolName = protocolConfig.split(":")[0];
				int port = Integer.parseInt(protocolConfig.split(":")[1]);
				
				if (DubboConstants.DEFAULT_PROTOCOL.equalsIgnoreCase(protocolName) && DubboConfigServer.dubboPort != null) {
					port = DubboConfigServer.dubboPort;
				}
				
				ProtocolConfig protocol = new ProtocolConfig();
				protocol.setName(protocolName);
				protocol.setPort(port);
				protocol.setThreads(PROTOCOL_THREADS);
				protocolList.add(protocol);
			}

			// 动态关联
			List<ServiceConfig<?>> serviceConfigList = serviceConfigMap.get(configServerKey);

			if (serviceConfigList == null) {
				serviceConfigList = new ArrayList<ServiceConfig<?>>();
			}

			// 服务提供者
			ServiceConfig<Object> service = new ServiceConfig<Object>();
			service.setApplication(application);
			service.setRegistry(registry);
			service.setProtocols(protocolList);
			service.setInterface(dubboService.getInterfaceClass());
			service.setRef(dubboService.getInterfaceRef());
			service.setVersion(dubboService.getVersion());
			service.setOwner(dubboService.getOwner());
			service.setTimeout(dubboService.getTimeout());
			service.setRetries(dubboService.getRetries());
			service.setProxy(DubboServiceFactory.EXTENSION_NAME);

			if (!StringUtil.isEmpty(dubboConfigServer.getMonitor())) {
				service.setMonitor(dubboConfigServer.getMonitor());
			}

			serviceConfigList.add(service);
			startService(configServerKey, dubboService, service);
			serviceConfigMap.put(configServerKey, serviceConfigList);
		}
	}

	public List<ServiceConfig<?>> getServiceConfigList(String dubboServiceKey) {
		if (serviceConfigMap == null) {
			return null;
		}

		return serviceConfigMap.get(dubboServiceKey);
	}

	public void initDubboLog() {
		DubboServiceFactory.setOpenLog(openLog);
	}

	private void startService(String configServerKey, DubboService dubboService, ServiceConfig<?> service) {
		service.export();
		LogManager.info("Dubbo configServer " + configServerKey + ", service[name= " + dubboService.getInterfaceName() + ", version= " + dubboService.getVersion()
				+ " ] 成功启动");
	}

}
