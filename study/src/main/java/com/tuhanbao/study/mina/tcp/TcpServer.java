package com.tuhanbao.study.mina.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.DefaultSocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.tuhanbao.study.mina.MessageFilter;
import com.tuhanbao.study.mina.MessageHandler;
import com.tuhanbao.util.config.BaseConfigConstants;
import com.tuhanbao.util.config.ConfigManager;
import com.tuhanbao.util.log.LogManager;

public final class TcpServer
{
    private static IoAcceptor acceptor = new NioSocketAcceptor();
    
    /**
     * 
     * @return 是否监听成功
     */
    public static void start()
    {
        acceptor.getFilterChain().addLast("es", new ProtocolCodecFilter(new TcpProtocolCodecFactory()));
        IoHandler handler = new TcpIoHandler();
        //设置buffer的长度
        DefaultSocketSessionConfig sc = (DefaultSocketSessionConfig) acceptor.getSessionConfig();
        sc.setReadBufferSize(ConfigManager.getBaseConfig().getInt(BaseConfigConstants.BUFFER_READ_SIZE));
        sc.setMaxReadBufferSize(ConfigManager.getBaseConfig().getInt(BaseConfigConstants.MAX_BUFFER_READ_SIZE));
        sc.setBothIdleTime(ConfigManager.getBaseConfig().getInt(BaseConfigConstants.TCP_IDLE_TIME));
        sc.setTcpNoDelay(true);
        
        acceptor.setHandler(handler);
        int tcpPort = ConfigManager.getBaseConfig().getInt(BaseConfigConstants.TCP_PORT);
        SocketAddress add = new InetSocketAddress(tcpPort);
        try
        {
            acceptor.bind(add);
            LogManager.info("tcpserver start success:" + tcpPort);
        }
        catch (IOException e)
        {
            LogManager.info("tcpserver start fail:" + tcpPort);
            LogManager.error(e);
            System.exit(0);
        }
        
        //过滤器注册
        MessageHandler.registerFilter(MessageFilter.instance);
    }
    
    public static void stop()
    {
        acceptor.unbind();
        acceptor.dispose();
    }
}