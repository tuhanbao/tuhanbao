package com.tuhanbao.study.mina.handler;

import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public interface IServiceHandler
{
    public JSONObject handle(IoSession session, JSONObject bean) throws JSONException;
}
