package com.tuhanbao.study.mina.http;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.DefaultSocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.tuhanbao.study.mina.MessageFilter;
import com.tuhanbao.study.mina.MessageHandler;
import com.tuhanbao.util.config.BaseConfigConstants;
import com.tuhanbao.util.config.ConfigManager;
import com.tuhanbao.util.log.LogManager;

public final class HttpServer
{
    private static NioSocketAcceptor acceptor = new NioSocketAcceptor();
    /**
     * 启动http服务
     */
    public static void start()
    {
        
//        tcpport=9141
//                httpport=9142
//                buffer_read_size=512
//                max_buffer_read_size=8192
//                #session超时关闭时间
//                tcp_idle_time=10000
//                http_idle_time=10
//                #session连接后超过2s仍未登陆，删除
//                tcp_login_time=20
//                #最大连接数
//                max_temp_session_size=5
//                #最大在线人数
//                max_online_session_size=3
//                
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new HttpServerProtocolCodecFactory()));
        HttpIoHandler handler = new HttpIoHandler();
        DefaultSocketSessionConfig cfg = (DefaultSocketSessionConfig) acceptor.getSessionConfig();
        cfg.setBothIdleTime(1000000000);
        cfg.setTcpNoDelay(true);
        acceptor.setHandler(handler);
        int httpPort = 8081;
        try
        {
            acceptor.bind(new InetSocketAddress(httpPort));
            LogManager.info("httpserver start success:" + httpPort);
        }
        catch (IOException e)
        {
            LogManager.info("httpserver start fail:" + httpPort);
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
