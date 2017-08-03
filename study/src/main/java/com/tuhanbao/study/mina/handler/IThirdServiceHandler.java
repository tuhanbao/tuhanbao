package com.tuhanbao.study.mina.handler;

import java.io.IOException;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tuhanbao.util.exception.BaseErrorCode;
import com.tuhanbao.util.exception.MyException;

public abstract class IThirdServiceHandler implements IServiceHandler
{
    public abstract String handle(IoSession session, Map<String, String> params, String args) throws IOException;
    
    public JSONObject handle(IoSession session, JSONObject bean) throws JSONException
    {
        throw new MyException(BaseErrorCode.ERROR, "third service handler cannot handler this request!");
    }
}
