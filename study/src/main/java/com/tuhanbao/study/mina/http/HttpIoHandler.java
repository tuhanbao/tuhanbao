package com.tuhanbao.study.mina.http;

import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSONException;
import com.tuhanbao.io.base.Constants;
import com.tuhanbao.io.objutil.StringUtil;
import com.tuhanbao.study.mina.ThirdMessageHandler;
import com.tuhanbao.util.log.LogManager;

public class HttpIoHandler extends IoHandlerAdapter
{
    private HttpMessageHandler handler = new HttpMessageHandler();
    
    @Override
    public void sessionCreated(IoSession session) throws Exception
    {
        LogManager.debug("http session create : " + session.getId());
    }
    
    @Override
    public void messageReceived(IoSession session, Object message)
    {
        HttpRequestMessage request = (HttpRequestMessage) message;
        HttpResponseMessage response = null;
        String context = request.getContext();
        String args = null;
        if (!StringUtil.isEmpty(context))
        {
            int gapIndex = context.indexOf(Constants.FILE_SEP);
            String cmd = "";
            if (gapIndex != -1)
            {
                cmd = context.substring(0, gapIndex);
                args = context.substring(gapIndex + 1);
            }
            else cmd = context;
            //提供给第三方的url不允许使用带参形式,如http://192.168.2.17:9132/?data={cmd=20002,data={platform_id=7,game_type=1}}
            //原因：有些三方会增加参数，会导致url出错。上例中就会变成这样http://192.168.2.17:9132/?data={cmd=20002,data={platform_id=7,game_type=1}}?third_args=third_value
            //所以所有的三方url一般格式如下http://192.168.2.17:9132/20002/args;
            if (StringUtil.isNumber(cmd))
            {
                LogManager.info(request.getHeader("URI"));
                response = ThirdMessageHandler.handle(session, Integer.valueOf(cmd), request, args);
            }
        }
        else
        {
            try
            {
                response = this.handler.handle(request, session);
            }
            catch (JSONException e)
            {
                LogManager.error(e);
                LogManager.info(request.toString());
            }
        }
        if (response != null)
        {
            session.write(response).addListener(IoFutureListener.CLOSE);
        }
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
    {
        close(session);
    }
    
    @Override
    public void sessionClosed(IoSession session) throws Exception 
    {
        close(session);
    }

    private void close(IoSession session)
    {
        if (session != null)
        {
            LogManager.debug("http session close : " + session.getId());
            session.close(true);
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
    {
        LogManager.fatal(cause);
        close(session);
    }
}