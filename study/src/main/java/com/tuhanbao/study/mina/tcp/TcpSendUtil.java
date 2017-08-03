package com.tuhanbao.study.mina.tcp;

import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tuhanbao.io.base.Constants;
import com.tuhanbao.io.objutil.ByteUtil;
import com.tuhanbao.util.exception.MyException;
import com.tuhanbao.util.log.LogManager;

public class TcpSendUtil
{
    /**
     * 应答客户端消息
     * @param session
     * @param bean
     * @throws JSONException
     */
    public static void send(IoSession session, JSONObject bean) throws JSONException
    {
        send(session, bean, Constants.SUCCESS_CODE);
    }
    
    private static void send(IoSession session, JSONObject bean, int code) throws JSONException
    {
        if (session != null && bean != null)
        {
            bean.put(Constants.STATE, code);
            LogManager.info("response : " + bean);
            session.write(ByteUtil.string2Bytes(bean.toString()));
        }
    }
    
    public static void send(IoSession session, MyException exception, int cmd, String sn)
    {
        if (session != null && exception != null)
        {
            JSONObject errorMsg = new JSONObject();
            try
            {
                if (exception.getArgs() != null)
                    errorMsg.put(Constants.ERROR_ARGS, JSON.toJSON(exception.getArgs()));
                errorMsg.put(Constants.CMD, cmd);
                if (sn != null) errorMsg.put(Constants.SN, sn);
                send(session, errorMsg, exception.getErrCode());
            }
            catch (JSONException e)
            {
                LogManager.error(e);
            }
        }
    }
    
    /**
     * 像客户端推送消息
     * @param session
     * @param bean
     * @param code
     * @throws JSONException
     */
    public static void pushMsg(IoSession session, JSONObject bean, int cmd) throws JSONException
    {
        if (session != null)
        {
            JSONObject obj = new JSONObject();
            obj.put(Constants.CMD, cmd);
            if (bean != null)
            {
                obj.put(Constants.DATA, bean);
            }
            Object player = session.getAttribute(Constants.SAVE_SESSION_OBJECT);
            if (player != null)
            {
                LogManager.info("push to player " + player.toString() + " : "+ obj);
            }
            session.write(ByteUtil.string2Bytes(obj.toString()));
        }
    }
}
