package com.tuhanbao.study.mina.http;

import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tuhanbao.io.base.Constants;
import com.tuhanbao.study.mina.MessageHandler;
import com.tuhanbao.util.exception.MyException;
import com.tuhanbao.util.log.LogManager;

/** http处理器 */
public class HttpMessageHandler
{
    public static final int THIRD_CMD_START = 20000;
    
    protected HttpMessageHandler()
    {
    }
    
    public HttpResponseMessage handle(HttpRequestMessage request, IoSession session) throws JSONException
    {
        String s = request.getParameter(Constants.DATA);
        if (s == null) return null;
        
        JSONObject json = JSONObject.parseObject(s);
        JSONObject responesJson = null;
        try
        {
            responesJson = MessageHandler.handle(session, json, true);
            responesJson.put(Constants.STATE, Constants.SUCCESS_CODE);
        }
        catch (Exception e)
        {
            int cmd = json.getIntValue(Constants.CMD);
            String sn = null;
            if (!json.containsKey(Constants.SN)) sn = json.getString(Constants.SN);
            responesJson = handleThrowable(session, e, cmd, sn);
            LogManager.error(e);
        }
        LogManager.info("response : " + responesJson.toString());
        return getResponseMessage(responesJson);
    }

    public static HttpResponseMessage getResponseMessage(JSONObject responesJson)
    {
        //回应
        HttpResponseMessage response = new HttpResponseMessage();
        response.setContentType("text/html");
        response.getHeaders().put("Content-Encoding", Constants.UTF_8);
        response.getHeaders().put("Access-Control-Allow-Origin", "*");
        response.appendBody(responesJson.toString());
        response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
        return response;
    }
    
    private static JSONObject handleThrowable(IoSession session, Throwable cause, int cmd, String sn)
    {
        MyException exception = MyException.getMyException(cause);
        JSONObject errorMsg = new JSONObject();
        if (session != null && exception != null)
        {
            try
            {
                if (exception.getArgs() != null)
                    errorMsg.put(Constants.ERROR_ARGS, JSONArray.toJSON(exception.getArgs()));
                errorMsg.put(Constants.CMD, cmd);
                if (sn != null) errorMsg.put(Constants.SN, sn);
                errorMsg.put(Constants.STATE, exception.getErrCode());
            }
            catch (Exception e)
            {
                LogManager.error(e);
            }
        }
        return errorMsg;
    }
}

