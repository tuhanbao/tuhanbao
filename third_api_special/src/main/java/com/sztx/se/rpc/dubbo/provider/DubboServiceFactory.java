package com.sztx.se.rpc.dubbo.provider;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.bytecode.Proxy;
import com.alibaba.dubbo.common.bytecode.Wrapper;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.proxy.AbstractProxyFactory;
import com.alibaba.dubbo.rpc.proxy.AbstractProxyInvoker;
import com.alibaba.dubbo.rpc.proxy.InvokerInvocationHandler;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sztx.se.rpc.dubbo.client.DubboClientFilter;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.TimeUtil;

public class DubboServiceFactory extends AbstractProxyFactory {

	public static final String EXTENSION_NAME = "javassist";

	private static boolean openLog;

	public static void setOpenLog(boolean openLog) {
		DubboServiceFactory.openLog = openLog;
	}

	@SuppressWarnings("unchecked")
	public <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces) {
		return (T) Proxy.getProxy(interfaces).newInstance(new InvokerInvocationHandler(invoker));
	}

	public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) {
		// TODO Wrapper类不能正确处理带$的类名
		final Wrapper wrapper = Wrapper.getWrapper(proxy.getClass().getName().indexOf('$') < 0 ? proxy.getClass() : type);
		return new AbstractProxyInvoker<T>(proxy, type, url) {
			@Override
			protected Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Throwable {
				if (openLog) {
					long startTime = System.currentTimeMillis();
					long endTime = 0;
					Object obj = null;
					String className = this.getInterface().getCanonicalName();
					String clientRequestId = "";
					String clientApplication = "";
					String providerApplication = this.getUrl().getParameter("application", "");

					try {
						Map<String, String> attachments = RpcContext.getContext().getAttachments();
						clientRequestId = attachments.get(DubboClientFilter.CLIENT_REQUEST_ID);
						clientApplication = attachments.get("clientApplication") != null ? attachments.get("clientApplication") : "";
						obj = wrapper.invokeMethod(proxy, methodName, parameterTypes, arguments);
						return obj;
					} catch (Throwable t) {
						if (t instanceof InvocationTargetException) {
							InvocationTargetException ite = (InvocationTargetException) t;
							Throwable e = ite.getTargetException();
							obj = e.getClass().getCanonicalName() + ":" + e.getMessage();
						} else {
							obj = t.getClass().getCanonicalName() + ":" + t.getMessage();
						}

						throw t;
					} finally {
						try {
							String inputParams = "";
							String rspResult = "";

							if (arguments != null) {
								inputParams = JSON.toJSONStringWithDateFormat(arguments, TimeUtil.MAX_LONG_DATE_FORMAT_STR,
										SerializerFeature.DisableCircularReferenceDetect);
							}

							if (obj != null) {
								rspResult = JSON.toJSONStringWithDateFormat(obj, TimeUtil.MAX_LONG_DATE_FORMAT_STR,
										SerializerFeature.DisableCircularReferenceDetect);
							}

							endTime = (endTime == 0 ? System.currentTimeMillis() : endTime);
							// 打印日志
							String rpcLog = getRpcLog(clientApplication, providerApplication, clientRequestId, className, methodName, inputParams, rspResult,
									startTime, endTime);

							LogManager.info(rpcLog);
						} catch (Exception e) {
							LogManager.error(e);
						}
					}
				} else {
					return wrapper.invokeMethod(proxy, methodName, parameterTypes, arguments);
				}
			}

			private String getRpcLog(String clientApplication, String providerApplication, String clientRequestId, String className, String methodName,
					String inputParams, String rspResult, long startTime, long endTime) {
				String localAddress = RpcContext.getContext().getLocalAddressString();
				String remoteAddress = RpcContext.getContext().getRemoteAddressString();
				long cost = endTime - startTime;
				String startTimeStr = TimeUtil.getTimeStr(TimeUtil.MAX_LONG_DATE_FORMAT_STR, startTime);
				String endTimeStr = TimeUtil.getTimeStr(TimeUtil.MAX_LONG_DATE_FORMAT_STR, endTime);
				return String.format("[DubboProvider] %s(%s) -> %s(%s) - %s|%s|%s|IN:%s|OUT:%s|[start:%s, end:%s, cost:%dms]", remoteAddress,
						clientApplication, localAddress, providerApplication, clientRequestId, className, methodName, inputParams, rspResult, startTimeStr,
						endTimeStr, cost);
			}
		};
	}
}