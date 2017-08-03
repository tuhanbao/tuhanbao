package com.tuhanbao.study.mina.tcp;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.tuhanbao.io.base.Constants;
import com.tuhanbao.study.mina.session.SessionManager;
import com.tuhanbao.util.log.LogManager;

public class TcpIoHandler extends IoHandlerAdapter
{
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception
    {
        LogManager.error(cause);
        SessionManager.closeSession(session, true);
    }

    @Override
    public void messageReceived(IoSession session, Object message)
    {
        LogManager.info("message received : " + message + " from session " + session.getId());
        TcpMessageHandler messageHandler = (TcpMessageHandler) session.getAttribute(Constants.MESSAGE_HANDLER);
        if (messageHandler == null) return;
        messageHandler.handle((byte[])message);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception
    {
        LogManager.info("message sent : " + message + " from session " + session.getId());
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception
    {
        SessionManager.addSession(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception
    {
        SessionManager.closeSession(session);
    }

    public void sessionClosed(IoSession session) throws Exception 
    {
        SessionManager.closeSession(session);
    }
}
