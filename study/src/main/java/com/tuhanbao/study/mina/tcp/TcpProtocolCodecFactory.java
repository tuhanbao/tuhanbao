package com.tuhanbao.study.mina.tcp;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class TcpProtocolCodecFactory implements ProtocolCodecFactory
{
    private ProtocolDecoder pd = new TcpProtocolDecoder();
    private ProtocolEncoder pe = new TcpProtocolEncoder();
    
    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception
    {
        return pd;
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception
    {
        return pe;
    }

}
