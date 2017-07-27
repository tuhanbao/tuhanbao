package com.sztx.se.rpc.dubbo.client;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sztx.se.rpc.dubbo.config.DubboConstants;
import com.sztx.se.rpc.dubbo.registry.SeZookeeperRegistry;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.TimeUtil;
import com.tuhanbao.base.util.objutil.UUIDUtil;

@Activate(group = Constants.CONSUMER, order = -10000)
public class DubboClientFilter implements Filter {

	public static final String EXTENSION_NAME = "consumercontext";

	public static final String CLIENT_REQUEST_ID = "clientRequestId";

	private static boolean openLog;

	public static void setOpenLog(boolean openLog) {
		DubboClientFilter.openLog = openLog;
	}

	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		if (openLog) {
			long startTime = System.currentTimeMillis();
			long endTime = 0;
			RpcContext.getContext().setInvoker(invoker).setInvocation(invocation).setLocalAddress(NetUtils.getLocalHost(), 0)
					.setRemoteAddress(invoker.getUrl().getHost(), invoker.getUrl().getPort());

			if (invocation instanceof RpcInvocation) {
				((RpcInvocation) invocation).setInvoker(invoker);
			}

			Object obj = null;
			String className = invoker.getInterface().getCanonicalName();
			URL url = invoker.getUrl();
			String methodName = invocation.getMethodName();
			Object[] arguments = invocation.getArguments();
			Map<String, String> attachments = RpcContext.getContext().getAttachments();
			String clientRequestId = "";
			String clientApplication = "";
			String providerApplication = SeZookeeperRegistry.getProviderApplication(className);

			if (attachments != null) {
				clientRequestId = UUIDUtil.getJustNumUUID();
				clientApplication = url.getParameter(DubboConstants.APPLICATION_KEY, "");
				attachments.put("clientApplication", clientApplication);
				attachments.put(DubboClientFilter.CLIENT_REQUEST_ID, clientRequestId);
			}

			try {
				obj = invoker.invoke(invocation);
				Result result = getResult(obj, invoker);
				return result;
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
						rspResult = JSON.toJSONStringWithDateFormat(obj, TimeUtil.MAX_LONG_DATE_FORMAT_STR, SerializerFeature.DisableCircularReferenceDetect);
					}

					endTime = (endTime == 0 ? System.currentTimeMillis() : endTime);
					// 打印日志
					String rpcLog = getRpcLog(clientApplication, providerApplication, clientRequestId, className, methodName, inputParams, rspResult,
							startTime, endTime);

					LogManager.info(rpcLog);
				} catch (Exception e) {
					LogManager.error(e);
				}

				RpcContext.getContext().clearAttachments();
			}
		} else {
			try {
				Object obj = invoker.invoke(invocation);
				Result result = getResult(obj, invoker);
				return result;
			} finally {
				RpcContext.getContext().clearAttachments();
			}
		}
	}

	private Result getResult(Object obj, Invoker<?> invoker) {
		Result result = (Result) obj;
		return result;
	}

	private String getRpcLog(String clientApplication, String providerApplication, String clientRequestId, String className, String methodName,
			String inputParams, String rspResult, long startTime, long endTime) {
		String localAddress = RpcContext.getContext().getLocalAddressString();
		String remoteAddress = RpcContext.getContext().getRemoteAddressString();
		long cost = endTime - startTime;
		String startTimeStr = TimeUtil.getTimeStr(TimeUtil.MAX_LONG_DATE_FORMAT_STR, startTime);
		String endTimeStr = TimeUtil.getTimeStr(TimeUtil.MAX_LONG_DATE_FORMAT_STR, endTime);
		return String.format("[DubboClient] %s(%s) -> %s(%s) - %s|%s|%s|IN:%s|OUT:%s|[start:%s, end:%s, cost:%dms]", localAddress, clientApplication,
				remoteAddress, providerApplication, clientRequestId, className, methodName, inputParams, rspResult, startTimeStr, endTimeStr, cost);
	}

}
