package com.tuhanbao.base.util.log;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.objutil.StringUtil;

public final class LogManager
{
    private static final Logger logger = Logger.getRootLogger();
    
    public static void init(String fileName)
    {
    	if (StringUtil.isEmpty(fileName)) PropertyConfigurator.configure("init/log4j.properties");
    	else PropertyConfigurator.configure(fileName);
    }
    
    public static void debug(Object o)
    {
        logger.debug(o);
    }
    
    public static void info(Object o)
    {
        logger.info(o);
    }
    
    public static void warn(Object o)
    {
        logger.warn(o);
    }
    
    public static void error(Throwable t)
    {
        for (StackTraceElement e : t.getStackTrace())
        {
            logger.info(e);
        }
        logger.error(t);
    }
    
    public static void error(String msg)
    {
        logger.error(msg);
    }
    
    public static void error(String msg, Throwable t)
    {
        logger.info(msg);
        for (StackTraceElement e : t.getStackTrace())
        {
            logger.info(e);
        }
        logger.error(t);
    }
    
    public static void fatal(Throwable t)
    {
        for (StackTraceElement e : t.getStackTrace())
        {
            logger.info(e);
        }
        logger.fatal(t);
    }
    
    public static void notify(Object o) {
        notify(0, false);
    }
    
    public static void notify(Object o, boolean needSendMsg2Admin) {
        if (o == null) return;

        logger.error(o);
        if (needSendMsg2Admin && !ConfigManager.isDebug()) {
            for (String mobile : ConfigManager.getAdminTelephone()) {
				try {
					//依赖三方短信发送
					Class<?> smsUtilClass = Class.forName("com.tuhanbao.thirdapi.sms.SmsUtil");
					if (smsUtilClass == null) break;
					smsUtilClass.getMethod("sendMsg", String.class, String.class).invoke(null, mobile, o.toString());
				} catch (Exception e) {
					// 不需要处理
				}
            }
        }
    }
}
