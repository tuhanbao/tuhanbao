package com.tuhanbao.study.mina;

import org.apache.mina.core.session.IoSession;

import com.tuhanbao.io.base.Constants;
import com.tuhanbao.io.objutil.ByteUtil;
import com.tuhanbao.study.mina.handler.ThirdServiceHandlerManager;
import com.tuhanbao.study.mina.http.HttpRequestMessage;
import com.tuhanbao.study.mina.http.HttpResponseMessage;

public final class ThirdMessageHandler
{
    public static final HttpResponseMessage handle(IoSession session, int cmd, HttpRequestMessage request, String args)
    {
        try
        {
            String result = ThirdServiceHandlerManager.getServiceHandler(cmd).handle(session, request.getParemeters(), args);
            return getResponseMessage(result);
        }
        catch (Exception e)
        {
            return getResponseMessage(e.getMessage());
        }
    }
    
    private static HttpResponseMessage getResponseMessage(String content)
    {
        //回应
        HttpResponseMessage response = new HttpResponseMessage();
        response.setContentType("text/html");
        response.getHeaders().put("Content-Encoding", Constants.UTF_8);
        response.getHeaders().put("Access-Control-Allow-Origin", "*");
        if (content != null) response.appendBody(ByteUtil.string2Bytes(content.toString()));
        response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
        return response;
    }
    
}
