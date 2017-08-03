package com.tuhanbao.study.mina.tcp;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.tuhanbao.io.impl.encipherUtil.Encipher;

public class TcpProtocolEncoder implements ProtocolEncoder
{

    @Override
    public void dispose(IoSession session) throws Exception
    {
    }

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception
    {
        byte[] bytes = (byte[])message;
        IoBuffer buff = IoBuffer.allocate(bytes.length + 4);
        buff.put(getIntByte(bytes.length));
        buff.put(Encipher.encryptTcp(bytes));
        buff.flip();
        out.write(buff);
    }
    
    private static byte[] getIntByte(int i)
    {
        byte[] bs = new byte[4];
        bs[3] = ((byte)(i >> 24));
        bs[2] = ((byte)(i >> 16));
        bs[1] = ((byte)(i >> 8));
        bs[0] = ((byte)i);
        return bs;
    }

}
