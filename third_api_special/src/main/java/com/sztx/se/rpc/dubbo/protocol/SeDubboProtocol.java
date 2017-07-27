package com.sztx.se.rpc.dubbo.protocol;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.Version;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.Transporter;
import com.alibaba.dubbo.remoting.exchange.ExchangeClient;
import com.alibaba.dubbo.remoting.exchange.ExchangeHandler;
import com.alibaba.dubbo.remoting.exchange.Exchangers;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.protocol.dubbo.DubboCodec;
import com.alibaba.dubbo.rpc.protocol.dubbo.DubboProtocol;
import com.sztx.se.rpc.dubbo.exchange.LazyConnectExchangeClient;
import com.sztx.se.rpc.dubbo.exchange.ReferenceCountExchangeClient;
import com.sztx.se.rpc.dubbo.invoker.SeDubboInvoker;
import com.tuhanbao.base.util.objutil.ReflectUtil;

public class SeDubboProtocol extends DubboProtocol {

	public static final String EXTENSION_NAME = "dubbo";

	@Override
	public <T> Invoker<T> refer(Class<T> serviceType, URL url) throws RpcException {
		// create rpc invoker.
		SeDubboInvoker<T> invoker = new SeDubboInvoker<T>(serviceType, url, getClients(url), invokers);
		invokers.add(invoker);
		return invoker;
	}

	private ExchangeClient[] getClients(URL url) {
		// 是否共享连接
		boolean service_share_connect = false;
		int connections = url.getParameter(Constants.CONNECTIONS_KEY, 0);
		// 如果connections不配置，则共享连接，否则每服务每连接
		if (connections == 0) {
			service_share_connect = true;
			connections = 1;
		}

		ExchangeClient[] clients = new ExchangeClient[connections];
		for (int i = 0; i < clients.length; i++) {
			if (service_share_connect) {
				clients[i] = getSharedClient(url);
			} else {
				clients[i] = initClient(url);
			}
		}
		return clients;
	}

	/**
	 * 获取共享连接
	 */
	@SuppressWarnings("unchecked")
	private ExchangeClient getSharedClient(URL url) {
		String key = url.getAddress();
		Map<String, ReferenceCountExchangeClient> referenceClientMap = (Map<String, ReferenceCountExchangeClient>) ReflectUtil.getFieldValue(this,
				"referenceClientMap");
		ReferenceCountExchangeClient client = referenceClientMap.get(key);
		if (client != null) {
			if (!client.isClosed()) {
				client.incrementAndGetCount();
				return client;
			} else {
				// logger.warn(new
				// IllegalStateException("client is closed,but stay in clientmap .client :"+
				// client));
				referenceClientMap.remove(key);
			}
		}
		ExchangeClient exchagneclient = initClient(url);
		ConcurrentMap<String, LazyConnectExchangeClient> ghostClientMap = (ConcurrentMap<String, LazyConnectExchangeClient>) ReflectUtil.getFieldValue(this,
				"ghostClientMap");
		client = new ReferenceCountExchangeClient(exchagneclient, ghostClientMap);
		referenceClientMap.put(key, client);
		ghostClientMap.remove(key);
		return client;
	}

	/**
	 * 创建新连接.
	 */
	private ExchangeClient initClient(URL url) {

		// client type setting.
		String str = url.getParameter(Constants.CLIENT_KEY, url.getParameter(Constants.SERVER_KEY, Constants.DEFAULT_REMOTING_CLIENT));

		String version = url.getParameter(Constants.DUBBO_VERSION_KEY);
		boolean compatible = (version != null && version.startsWith("1.0."));
		url = url.addParameter(Constants.CODEC_KEY, Version.isCompatibleVersion() && compatible ? COMPATIBLE_CODEC_NAME : DubboCodec.NAME);
		// 默认开启heartbeat
		url = url.addParameterIfAbsent(Constants.HEARTBEAT_KEY, String.valueOf(Constants.DEFAULT_HEARTBEAT));

		// BIO存在严重性能问题，暂时不允许使用
		if (str != null && str.length() > 0 && !ExtensionLoader.getExtensionLoader(Transporter.class).hasExtension(str)) {
			throw new RpcException("Unsupported client type: " + str + "," + " supported client type is "
					+ StringUtils.join(ExtensionLoader.getExtensionLoader(Transporter.class).getSupportedExtensions(), " "));
		}

		ExchangeClient client;
		try {
			ExchangeHandler requestHandler = (ExchangeHandler) ReflectUtil.getFieldValue(this, "requestHandler");
			// 设置连接应该是lazy的
			if (url.getParameter(Constants.LAZY_CONNECT_KEY, false)) {
				client = new LazyConnectExchangeClient(url, requestHandler);
			} else {
				client = Exchangers.connect(url, requestHandler);
			}
		} catch (RemotingException e) {
			throw new RpcException("Fail to create remoting client for service(" + url + "): " + e.getMessage(), e);
		}
		return client;
	}

}
