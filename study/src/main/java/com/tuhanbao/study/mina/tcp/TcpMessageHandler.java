package com.tuhanbao.study.mina.tcp;

import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tuhanbao.io.base.ByteBuffer;
import com.tuhanbao.io.base.Constants;
import com.tuhanbao.io.impl.encipherUtil.Encipher;
import com.tuhanbao.io.objutil.ByteUtil;
import com.tuhanbao.study.mina.MessageHandler;
import com.tuhanbao.util.exception.BaseErrorCode;
import com.tuhanbao.util.exception.MyException;
import com.tuhanbao.util.log.LogManager;

public class TcpMessageHandler
{
    private IoSession session;
    
    private ByteBuffer data = new ByteBuffer();
    
    private int nextLength = 0;

    public void setSession(IoSession session)
    {
        this.session = session;
    }

    /**
     * 新收到消息
     * @param session 
     * @param message
     * @throws JSONException 
     * @throws JSONException 
     */
    public void handle(byte[] message)
    {
        data.put(message);
        if (nextLength == 0) nextLength = data.readInt();
        while (nextLength > 0 && data.size() >= nextLength)
        {
            String s = ByteUtil.bytes2String(Encipher.decryptTcp(data.readBytes(nextLength)));
            nextLength = 0;
            JSONObject json = null;
            try
            {
                json = (JSONObject)JSONObject.toJSON(s);
                JSONObject result = MessageHandler.handle(session, json, false);
                if (result != null)
                {
                    TcpSendUtil.send(this.session, result);
                }
            }
            catch (Exception e)
            {
                try
                {
                    int cmd = json.getIntValue(Constants.CMD);
                    String sn = null;
                    if (!json.containsKey(Constants.SN)) sn = json.getString(Constants.SN);
                    handleThrowable(session, e, cmd, sn);
                }
                catch (Exception jsonException)
                {
                    LogManager.error(jsonException);
                }
            }
            
            if (this.data.size() >= 4)
                nextLength = this.data.readInt();
        }
        
        if (nextLength < 0)
            throw new MyException(BaseErrorCode.ILLEGAL_ARGUMENT, "invalid length : " + nextLength );
    }
    

    
    private static void handleThrowable(IoSession session, Throwable cause, int cmd, String sn)
    {
        MyException e = MyException.getMyException(cause);
        TcpSendUtil.send(session, e, cmd, sn);
        LogManager.error(cause);
    }
    
}
