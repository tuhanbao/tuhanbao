package com.tuhanbao.study.mina.tcp;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class TcpProtocolDecoder implements ProtocolDecoder
{

    @Override
    public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception
    {
        int limit = in.limit();
        in.position(limit);
        byte[] bs = new byte[limit];
        System.arraycopy(in.array(), 0, bs, 0, limit);
        out.write(bs);
    }

    @Override
    public void dispose(IoSession session) throws Exception
    {
    }

    @Override
    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception
    {
    }

}
