package com.sztx.se.rpc.dubbo.exception;

import java.lang.reflect.Method;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ReflectUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.tuhanbao.base.util.exception.MyException;

@Activate(group = Constants.PROVIDER)
public class DubboServiceExceptionFilter implements Filter {

	public static final String EXTENSION_NAME = "exception";

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		try {
			Result result = invoker.invoke(invocation);

			if (result.hasException() && GenericService.class != invoker.getInterface()) {
				try {
					Throwable ex = result.getException();

					// 如果是checked异常，直接抛出
					if (!(ex instanceof RuntimeException) && (ex instanceof Exception)) {
						return result;
					}

					// 在方法签名上有声明，直接抛出
					try {
						Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
						Class<?>[] exceptionClassses = method.getExceptionTypes();

						for (Class<?> exceptionClass : exceptionClassses) {
							if (ex.getClass().equals(exceptionClass)) {
								return result;
							}
						}
					} catch (NoSuchMethodException e) {
						return result;
					}

					// 未在方法签名上定义的异常，在服务器端打印ERROR日志
					logger.error("Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost() + ". service: "
							+ invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + ex.getClass().getName() + ": "
							+ ex.getMessage(), ex);

					// 异常类和接口类在同一jar包里，直接抛出
					String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
					String exceptionFile = ReflectUtils.getCodeBase(ex.getClass());

					if (serviceFile == null || exceptionFile == null || serviceFile.equals(exceptionFile)) {
						return result;
					}

					// 是JDK自带的异常，直接抛出
					String className = ex.getClass().getName();

					if (className.startsWith("java.") || className.startsWith("javax.")) {
						return result;
					}

					// 是Dubbo本身的异常，直接抛出
					if (ex instanceof RpcException) {
						return result;
					} else if (ex instanceof MyException) {
                        result = getRpcResult((MyException)ex);
                        return result;
                    }

					// 否则，包装成RuntimeException抛给客户端
					return new RpcResult(new RuntimeException(StringUtils.toString(ex)));
				} catch (Throwable e) {
					logger.warn("Fail to ExceptionFilter when called by " + RpcContext.getContext().getRemoteHost() + ". service: "
							+ invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + e.getClass().getName() + ": "
							+ e.getMessage(), e);
					return result;
				}
			}

			return result;
		} catch (RuntimeException e) {
			logger.error(
					"Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost() + ". service: "
							+ invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + e.getClass().getName() + ": "
							+ e.getMessage(), e);
			throw e;
		}
	}



    private RpcResult getRpcResult(MyException ex) {
        return new RpcResult(ex);
    }
}
