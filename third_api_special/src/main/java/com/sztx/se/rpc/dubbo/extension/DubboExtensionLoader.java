package com.sztx.se.rpc.dubbo.extension;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.registry.RegistryFactory;
import com.alibaba.dubbo.rpc.ExporterListener;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.rpc.ProxyFactory;
import com.alibaba.dubbo.rpc.cluster.RouterFactory;
import com.sztx.se.rpc.dubbo.client.DubboClientFilter;
import com.sztx.se.rpc.dubbo.exception.DubboServiceExceptionFilter;
import com.sztx.se.rpc.dubbo.listener.DubboServiceExporterListener;
import com.sztx.se.rpc.dubbo.protocol.SeDubboProtocol;
import com.sztx.se.rpc.dubbo.provider.DubboServiceFactory;
import com.sztx.se.rpc.dubbo.registry.SeZookeeperRegistryFactory;
import com.sztx.se.rpc.dubbo.route.SeConditionRouterFactory;

public class DubboExtensionLoader {

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public static void loadExtension() {
		ExtensionLoader registryExtensionLoader = ExtensionLoader.getExtensionLoader(RegistryFactory.class);
		registryExtensionLoader.replaceExtension(SeZookeeperRegistryFactory.EXTENSION_NAME, SeZookeeperRegistryFactory.class);
		
		ExtensionLoader proxyFactoryExtensionLoader = ExtensionLoader.getExtensionLoader(ProxyFactory.class);
		proxyFactoryExtensionLoader.replaceExtension(DubboServiceFactory.EXTENSION_NAME, DubboServiceFactory.class);

		ExtensionLoader filterExtensionLoader = ExtensionLoader.getExtensionLoader(Filter.class);
		filterExtensionLoader.replaceExtension(DubboClientFilter.EXTENSION_NAME, DubboClientFilter.class);
		filterExtensionLoader.replaceExtension(DubboServiceExceptionFilter.EXTENSION_NAME, DubboServiceExceptionFilter.class);
	
		ExtensionLoader protocolExtensionLoader = ExtensionLoader.getExtensionLoader(Protocol.class);
		protocolExtensionLoader.replaceExtension(SeDubboProtocol.EXTENSION_NAME, SeDubboProtocol.class);
		
		ExtensionLoader routerExtensionLoader = ExtensionLoader.getExtensionLoader(RouterFactory.class);
		routerExtensionLoader.replaceExtension(SeConditionRouterFactory.EXTENSION_NAME, SeConditionRouterFactory.class);
	
		ExtensionLoader exporterExtensionLoader = ExtensionLoader.getExtensionLoader(ExporterListener.class);
		exporterExtensionLoader.replaceExtension(DubboServiceExporterListener.EXTENSION_NAME, DubboServiceExporterListener.class);
	}

}
