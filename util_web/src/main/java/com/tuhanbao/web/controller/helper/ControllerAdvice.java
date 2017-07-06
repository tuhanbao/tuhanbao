package com.tuhanbao.web.controller.helper;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.json.JsonUtil;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.web.controller.authority.IUser;
import com.tuhanbao.web.controller.authority.TokenService;
import com.tuhanbao.web.controller.authority.UserAccessApiInterceptor;

/**
 * @author: chaoyang.ren
 * @date:2016年9月1日
 * @time:上午11:55:02
 * @email:chaoyang.ren@foxmail.com
 */
@Component
@Aspect
public class ControllerAdvice {

    /**
     * 执行带有controller注解类里的方法时执行此方法的内容
     * 
     * @author: chaoyang.ren
     * @date:2016年9月1日 下午4:12:54
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("within(@org.springframework.stereotype.Controller *)")
    public Object arroundMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request = getRequest();
        MethodSignature signature = (MethodSignature)proceedingJoinPoint.getSignature();
        StringBuilder sb = new StringBuilder();
        String ip = UserAccessApiInterceptor.getRemoteHost(request);
        sb.append("ip:").append(ip).append(Constants.COMMA);
        TokenService tokenService = TokenService.getInstance();
        if (tokenService != null) {
            IUser currentUser = tokenService.getCurrentUser(request);
            if (currentUser != null) {
                sb.append("user:").append(currentUser.getUserId()).append(Constants.COMMA);
            }
        }
        sb.append("method:").append(signature.getName()).append(Constants.COMMA);
        Object[] args = proceedingJoinPoint.getArgs();
        sb.append("args:").append(StringUtil.array2String(args)).append(Constants.COMMA);
        LogManager.info(sb);
        
        //这里可能报异常
        Object result = proceedingJoinPoint.proceed();
        if (result != JsonHttpMessageConverter.NULL && !(result instanceof String)) {
            ResponseAdapter adapter = signature.getMethod().getAnnotation(ResponseAdapter.class);
            result = JsonUtil.toJSON(result);
            if (adapter != null) {
                result = ResponseAdapterManager.getValue(result, adapter);
            }
        }
        LogManager.info("result:" + JSON.toJSONString(result));
        return result;
    }

    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }
}
