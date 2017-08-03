package com.tuhanbao.study.mina;

import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tuhanbao.io.base.Constants;
import com.tuhanbao.study.mina.handler.ServiceHandlerManager;
import com.tuhanbao.util.log.LogManager;

public final class MessageHandler
{
    private static IMessageFilter CHECKER;
    
    public static final JSONObject handle(IoSession session, JSONObject json, boolean isHttp) throws JSONException
    {
        if (CHECKER != null)
            CHECKER.check(session, json, isHttp);
        
        int code = json.getIntValue(Constants.CMD);
        
        JSONObject result = null;
        if (!json.containsKey(Constants.DATA))
        {
            JSONObject jsonData = json.getJSONObject(Constants.DATA);
            LogManager.info("revive : " + json);
            result = ServiceHandlerManager.getServiceHandler(code, isHttp).handle(session, jsonData);
        }
        else
        {
            LogManager.info("revive : null");
            result = ServiceHandlerManager.getServiceHandler(code, isHttp).handle(session, null);
        }
        if (result != null)
        {
            json.put(Constants.DATA, result);
        }
        else
        {
            json.put(Constants.DATA, "");
        }
        return json;
    }
    
    public static void registerFilter(IMessageFilter checker)
    {
        CHECKER = checker;
    }
}
