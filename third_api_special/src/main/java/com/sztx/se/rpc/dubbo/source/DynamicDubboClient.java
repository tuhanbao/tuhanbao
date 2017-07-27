package com.sztx.se.rpc.dubbo.source;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.sztx.se.rpc.dubbo.client.DubboClient;
import com.sztx.se.rpc.dubbo.client.DubboClientFactory;
import com.sztx.se.rpc.dubbo.client.DubboClientFilter;
import com.sztx.se.rpc.dubbo.config.DubboConfigServer;
import com.sztx.se.rpc.dubbo.extension.DubboExtensionLoader;
import com.sztx.se.rpc.dubbo.provider.DubboServiceFactory;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.objutil.StringUtil;

/**
 * 动态dubbo客户端
 * 
 * @author zhihongp
 * 
 */
public class DynamicDubboClient implements DubboClientFactory {

	private DubboConfigServer defaultTargetDubboConfigServer;

	private List<DubboClient> targetDubboClientList;

	private Map<String, Map<String, Object>> dubboClientMap;

	/**
	 * 日志开关，默认为false不打开
	 */
	private boolean openLog = false;

	public void setDefaultTargetDubboConfigServer(DubboConfigServer defaultTargetDubboConfigServer) {
		this.defaultTargetDubboConfigServer = defaultTargetDubboConfigServer;
	}

	public void setDubboClientMap(Map<String, Map<String, Object>> dubboClientMap) {
		this.dubboClientMap = dubboClientMap;
	}

	public void setTargetDubboClientList(List<DubboClient> targetDubboClientList) {
		this.targetDubboClientList = targetDubboClientList;
	}

	public void setOpenLog(boolean openLog) {
		this.openLog = openLog;
	}

	public void afterPropertiesSet() {
		DubboExtensionLoader.loadExtension();
		dubboClientMap = new ConcurrentHashMap<String, Map<String, Object>>();

		for (DubboClient dubboClient : targetDubboClientList) {
			DubboConfigServer dubboConfigServer = dubboClient.getDubboConfigServer();

			if (dubboConfigServer == null) {
				dubboConfigServer = defaultTargetDubboConfigServer;
			}

			if (dubboConfigServer == null) {
				throw new MyException("Can not get a dubboConfigServer, dubboClient=" + dubboClient);
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

			// 动态关联
			Map<String, Object> referenceMap = dubboClientMap.get(configServerKey);

			if (referenceMap == null) {
				referenceMap = new ConcurrentHashMap<String, Object>();
			}

			String beanId = dubboClient.getBeanId();
			referenceMap.put(getDubboClientKey(beanId), dubboClient);

			if (dubboClient.getIsCheck()) {
				ReferenceConfig<Object> reference = new ReferenceConfig<Object>();
				reference.setApplication(application);
				reference.setRegistry(registry);
				reference.setInterface(dubboClient.getInterfaceClass());
				reference.setTimeout(dubboClient.getTimeout());
				reference.setRetries(dubboClient.getRetries());
				reference.setProxy(DubboServiceFactory.EXTENSION_NAME);

				if (StringUtils.isNotBlank(dubboClient.getProtocol())) {
					reference.setProtocol(dubboClient.getProtocol());
				}

				if (StringUtils.isNotBlank(dubboClient.getVersion())) {
					reference.setVersion(dubboClient.getVersion());
				}

				try {
					Object obj = reference.get();
					referenceMap.put(getDubboReferenceKey(beanId), obj);
				} catch (Exception e) {
					throw MyException.getMyException(e);
				}
			}

			dubboClientMap.put(configServerKey, referenceMap);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getDubboClient(DubboClient dubboClient) {
		String dubboClientType = DubboClientSwitcher.getDubboClientType();
		T obj = null;

		if (StringUtils.isBlank(dubboClientType)) {
			dubboClientType = defaultTargetDubboConfigServer.getConfigServerKey();
		}

		String beanId = dubboClient.getBeanId();
		Map<String, Object> referenceMap = dubboClientMap.get(dubboClientType);

		if (referenceMap != null && !referenceMap.isEmpty()) {
			if (StringUtils.isNotBlank(beanId)) {
				obj = (T) referenceMap.get(getDubboReferenceKey(beanId));
			} else {
				beanId = dubboClient.toString();
			}
		}

		if (obj == null) {
			if (referenceMap != null && !referenceMap.isEmpty()) {
				DubboConfigServer dubboConfigServer = dubboClient.getDubboConfigServer();

				if (dubboConfigServer == null) {
					dubboConfigServer = defaultTargetDubboConfigServer;
				}

				if (dubboConfigServer == null) {
					throw new MyException("Can not get a dubboConfigServer, dubboClient=" + dubboClient);
				}

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

				ReferenceConfig<T> reference = new ReferenceConfig<T>();
				reference.setApplication(application);
				reference.setRegistry(registry);
				reference.setInterface(dubboClient.getInterfaceClass());
				reference.setTimeout(dubboClient.getTimeout());
				reference.setRetries(dubboClient.getRetries());
				reference.setProxy(DubboServiceFactory.EXTENSION_NAME);

				if (StringUtils.isNotBlank(dubboClient.getProtocol())) {
					reference.setProtocol(dubboClient.getProtocol());
				}

				if (StringUtils.isNotBlank(dubboClient.getVersion())) {
					reference.setVersion(dubboClient.getVersion());
				}

				try {
					obj = reference.get();
					referenceMap.put(getDubboReferenceKey(beanId), obj);
				} catch (Exception e) {
					throw MyException.getMyException(e);
				}
			}
		}

		if (obj == null) {
			throw new MyException("Can not get a dubboClient, beanId=" + beanId);
		}

		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getDubboClient(String beanId) {
		String dubboClientType = DubboClientSwitcher.getDubboClientType();
		T obj = null;

		if (StringUtils.isBlank(dubboClientType)) {
			dubboClientType = defaultTargetDubboConfigServer.getConfigServerKey();
		}

		Map<String, Object> referenceMap = dubboClientMap.get(dubboClientType);

		if (referenceMap != null && !referenceMap.isEmpty()) {
			obj = (T) referenceMap.get(getDubboReferenceKey(beanId));
		}

		if (obj == null) {
			if (referenceMap != null && !referenceMap.isEmpty()) {
				DubboClient dubboClient = (DubboClient) referenceMap.get(getDubboClientKey(beanId));

				if (dubboClient != null) {
					DubboConfigServer dubboConfigServer = dubboClient.getDubboConfigServer();

					if (dubboConfigServer == null) {
						dubboConfigServer = defaultTargetDubboConfigServer;
					}

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

					ReferenceConfig<T> reference = new ReferenceConfig<T>();
					reference.setApplication(application);
					reference.setRegistry(registry);
					reference.setInterface(dubboClient.getInterfaceClass());
					reference.setTimeout(dubboClient.getTimeout());
					reference.setRetries(dubboClient.getRetries());
					reference.setProxy(DubboServiceFactory.EXTENSION_NAME);

					if (StringUtils.isNotBlank(dubboClient.getProtocol())) {
						reference.setProtocol(dubboClient.getProtocol());
					}

					if (StringUtils.isNotBlank(dubboClient.getVersion())) {
						reference.setVersion(dubboClient.getVersion());
					}

					if (!StringUtil.isEmpty(dubboConfigServer.getMonitor())) {
						reference.setMonitor(dubboConfigServer.getMonitor());
					}

					try {
						obj = reference.get();
						referenceMap.put(getDubboReferenceKey(beanId), obj);
					} catch (Exception e) {
						throw MyException.getMyException(e);
					}
				}
			}
		}

		if (obj == null) {
			throw new MyException("Can not get a dubboClient, beanId=" + beanId);
		}

		return obj;
	}

	public void initDubboLog() {
		DubboClientFilter.setOpenLog(openLog);
	}

	private String getDubboClientKey(String beanId) {
		return beanId + ":dubboClient";
	}

	private String getDubboReferenceKey(String beanId) {
		return beanId + ":reference";
	}

}
