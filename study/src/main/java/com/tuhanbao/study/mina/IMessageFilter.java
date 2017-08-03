package com.tuhanbao.study.mina;

import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public interface IMessageFilter
{
   void check(IoSession session, JSONObject json, boolean isHttp) throws JSONException;
}
