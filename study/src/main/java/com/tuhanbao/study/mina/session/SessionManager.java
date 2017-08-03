package com.tuhanbao.study.mina.session;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.mina.core.session.IoSession;

import com.tuhanbao.io.base.Constants;
import com.tuhanbao.io.objutil.TimeUtil;
import com.tuhanbao.study.mina.tcp.TcpMessageHandler;
import com.tuhanbao.util.config.BaseConfigConstants;
import com.tuhanbao.util.config.ConfigManager;
import com.tuhanbao.util.exception.BaseErrorCode;
import com.tuhanbao.util.exception.MyException;
import com.tuhanbao.util.log.LogManager;

public final class SessionManager
{
    public static final Set<IoSession> ONLINE_SESSIONS = new HashSet<IoSession>();

    //临时session
    private static final List<IoSession> TEMP_SESSIONS = new ArrayList<IoSession>();
    
    private static final int LOGIN_TIMEOUT = ConfigManager.getBaseConfig().getInt(BaseConfigConstants.TCP_LOGIN_TIME);
    
    private static final int MAX_TEMP_SESSION_SIZE = ConfigManager.getBaseConfig().getInt(BaseConfigConstants.MAX_TEMP_SESSION_SIZE);

    private static final int MAX_ONLINE_SESSION_SIZE = ConfigManager.getBaseConfig().getInt(BaseConfigConstants.MAX_ONLINE_SESSION_SIZE);
    
    private static List<String> WHITE_IPS = new ArrayList<String>();
    
//    //服务器的两种状态
//    public static final int NORMAL = 0, PAUSE = 1, BUSY = 2;
//    
//    //服务器状态:
//    public static volatile int SERVER_STATE = NORMAL;
    
    
    public static void closeSession(IoSession session)
    {
        closeSession(session, true);
    }
    
    public static void closeSession(IoSession session, boolean immediately)
    {
        if (session != null)
        {
            LogManager.info("close session : " + session.getId());
            session.removeAttribute(Constants.MESSAGE_HANDLER);
            synchronized (ONLINE_SESSIONS)
            {
                if (ONLINE_SESSIONS.remove(session))
                {
                    SaveSessionObject saveSessionObject = (SaveSessionObject)session.getAttribute(Constants.SAVE_SESSION_OBJECT);
                    if (saveSessionObject != null)
                        saveSessionObject.removeSession();
                }
            }
            synchronized (TEMP_SESSIONS)
            {
                TEMP_SESSIONS.remove(session);
            }
            session.close(immediately);
        }
    }
    
    private static void removeTimeOutSessions()
    {
        for (Iterator<IoSession> it = TEMP_SESSIONS.iterator(); it.hasNext();)
        {
            IoSession session = it.next();
            //session建立连接有超过两s未登陆成功，删除此session
            if (TimeUtil.now() - session.getCreationTime() >=  LOGIN_TIMEOUT * 1000)
            {
                session.close(true);
                it.remove();
            }
            else
            {
                break;
            }
        }
    }
    
    public static void addSession(IoSession session)
    {
        filterWhiteIp(session);
        try
        {
            checkSessionIsFull(session);
        }
        catch (MyException e)
        {
            LogManager.error(e);
            session.close(true);
            return;
        }
        
        //添加session
        LogManager.info("create session : " + session.getId());
        TcpMessageHandler messageHandler = new TcpMessageHandler();
        messageHandler.setSession(session);
        session.setAttribute(Constants.MESSAGE_HANDLER, messageHandler);
        synchronized (TEMP_SESSIONS)
        {
            TEMP_SESSIONS.add(session);
        }
    }

    /**
     * 检查连接数是否过多
     * @param session
     */
    private static void checkSessionIsFull(IoSession session)
    {
        checkOnlineSessionIsFull();
        
        synchronized (TEMP_SESSIONS)
        {
            if (TEMP_SESSIONS.size() >= MAX_TEMP_SESSION_SIZE)
            {
                removeTimeOutSessions();
                
                //清理过后session依然过多，报系统繁忙
                if (TEMP_SESSIONS.size() >= MAX_TEMP_SESSION_SIZE)
                {
                    throw new MyException(BaseErrorCode.SERVER_IS_BUSY);
                }
            }
        }
    }


    private static void checkOnlineSessionIsFull()
    {
        if (getOnlineSize() >= MAX_ONLINE_SESSION_SIZE)
        {
            throw new MyException(BaseErrorCode.ONLINE_PLAYERS_FULL);
        }
    }
    
    public static int getOnlineSize()
    {
        return ONLINE_SESSIONS.size();
    }

    public static void loginSuccess(IoSession session)
    {
        synchronized (TEMP_SESSIONS)
        {
            //可能这里登录时间过晚，session被关闭了，即使登录成功，也需要报错
            if (session.isClosing()) throw new MyException(BaseErrorCode.SERVER_IS_BUSY);
            
            TEMP_SESSIONS.remove(session);
            synchronized (ONLINE_SESSIONS)
            {
                checkOnlineSessionIsFull();
                LogManager.info("login session : " + session.getId());
                ONLINE_SESSIONS.add(session);
            }
        }
    }
    
    /**
     * 所有在线的玩家
     * @return
     */
    public static SaveSessionObject[] getOnlinePlayers()
    {
        synchronized (ONLINE_SESSIONS)
        {
            SaveSessionObject[] objs = new SaveSessionObject[ONLINE_SESSIONS.size()];
            int i = 0;
            for (IoSession session : ONLINE_SESSIONS)
            {
                objs[i++] = (SaveSessionObject) session.getAttribute(Constants.SAVE_SESSION_OBJECT);
            }
            return objs;
        }
    }
    
    public synchronized static void resetWhiteIps(String[] ips)
    {
        WHITE_IPS.clear();
        if (ips != null)
        {
            for (String ip : ips)
            {
                WHITE_IPS.add(ip);
            }
        }
    }
    
    /**
     * 过滤IP地址
     * @param session
     */
    private static void filterWhiteIp(IoSession session)
    {
        //先检查是否需要过滤
        if (isFilterWhiteIp())
        {
            if (!WHITE_IPS.contains(getIp(session)))
            {
                throw new MyException(BaseErrorCode.SERVER_IS_MAINTAINING);
            }
        }
    }
    
    /**
     * 获取客户端ip
     * 暂时只发现session发送过来的客户端地址形如 /192.168.2.17:9999
     * @param session
     * @return
     */
    public static String getIp(IoSession session)
    {
        InetSocketAddress address = (InetSocketAddress)session.getRemoteAddress();
        return address.getAddress().getHostAddress();
    }
    
    /**
     * 当ip白名单存在时，需要进行过滤
     * @return
     */
    private static boolean isFilterWhiteIp()
    {
        return WHITE_IPS.size() > 0;
    }
}
