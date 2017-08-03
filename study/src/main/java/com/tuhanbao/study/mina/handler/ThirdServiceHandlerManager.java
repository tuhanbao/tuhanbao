package com.tuhanbao.study.mina.handler;

import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.util.exception.BaseErrorCode;
import com.tuhanbao.util.exception.MyException;

public final class ThirdServiceHandlerManager
{
    private static final Map<Integer, IThirdServiceHandler> HANDLER_MAP = new HashMap<Integer, IThirdServiceHandler>();
    
    public static IThirdServiceHandler getServiceHandler(int code)
    {
        IThirdServiceHandler handler = HANDLER_MAP.get(code);;
        if (handler == null)
        {
            throw new MyException(BaseErrorCode.COMMAND_CODE_ISNOT_EXIST, "" + code);
        }
        return handler;
        
    }
    
    public static void registerServiceHandler(int code, IThirdServiceHandler handler)
    {
        HANDLER_MAP.put(code, handler);
    }
    
}
