package com.tuhanbao.study.mina.handler;

import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.io.base.Constants;
import com.tuhanbao.study.mina.http.HttpMessageHandler;
import com.tuhanbao.study.mina.http.HttpServer;
import com.tuhanbao.study.mina.tcp.TcpServer;
import com.tuhanbao.util.exception.BaseErrorCode;
import com.tuhanbao.util.exception.MyException;

public final class ServiceHandlerManager
{
    private static boolean IS_MAINTAINING = false;
    
    private static IServiceHandler MAINTAINING_HANDLER;

    private static final Map<Integer, IServiceHandler> TCP_HANDLER_MAP = new HashMap<Integer, IServiceHandler>();

    private static final Map<Integer, IServiceHandler> HTTP_HANDLER_MAP = new HashMap<Integer, IServiceHandler>();
    
    static
    {
        //基本的handler
    }
    
    public static IServiceHandler getServiceHandler(int code, boolean isHttp)
    {
        //非心跳命令在服务器暂停时不允许进入
        if (code != Constants.HEAT_BEAT && IS_MAINTAINING) return getMaintainServiceHandler();
        
        IServiceHandler handler = null;
        if (isHttp) 
        {
            handler = HTTP_HANDLER_MAP.get(code);
        }
        else
        {
            handler = TCP_HANDLER_MAP.get(code);
        }
        if (handler == null)
        {
            throw new MyException(BaseErrorCode.COMMAND_CODE_ISNOT_EXIST, "" + code);
        }
        return handler;
        
    }
    
    private static IServiceHandler getMaintainServiceHandler()
    {
        return MAINTAINING_HANDLER;
    }
    
    public static void registerMaintainServiceHandler(IServiceHandler handler)
    {
        MAINTAINING_HANDLER = handler;
    }
    
    public static void registerServiceHandler(int code, IServiceHandler handler)
    {
        registerServiceHandler(code, handler, false);
    }
    
    public static void registerServiceHandler(int code, IServiceHandler handler, boolean isHttp)
    {
        if (code > HttpMessageHandler.THIRD_CMD_START)
        {
            ThirdServiceHandlerManager.registerServiceHandler(code, (IThirdServiceHandler)handler);
        }
        if (isHttp) HTTP_HANDLER_MAP.put(code, handler);
        else TCP_HANDLER_MAP.put(code, handler);
    }
    
    
    public static void pauseServer()
    {
        IS_MAINTAINING = true;
    }
    
    public static void resumeServer()
    {
        IS_MAINTAINING = false;
    }
    
    public static void stopServer()
    {
        IS_MAINTAINING = true;
        
        HttpServer.stop();
        TcpServer.stop();
        
//        MemCache.saveAll();
    }
}
