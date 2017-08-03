package com.tuhanbao.study.mina.http;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import com.tuhanbao.io.base.Constants;

public class HttpResponseEncoder implements MessageEncoder<HttpResponseMessage>
{
    private static CharsetEncoder ENCODER = Charset.forName(Constants.UTF_8).newEncoder();
    
    private static final byte[] CRLF = {13, 10};

    public HttpResponseEncoder()
    {
    }

    public void encode(IoSession session, HttpResponseMessage message, ProtocolEncoderOutput out) throws Exception
    {
        IoBuffer buf = IoBuffer.allocate(256);

        buf.setAutoExpand(true);
        try
        {
            buf.putString("HTTP/1.1 ", ENCODER);
            buf.putString(String.valueOf(message.getResponseCode()), ENCODER);
            switch (message.getResponseCode())
            {
                case 200:
                    buf.putString(" OK", ENCODER);
                    break;
                case 404:
                    buf.putString(" Not Found", ENCODER);
            }

            buf.put(CRLF);
            for (Map.Entry<String, String> entry : message.getHeaders().entrySet())
            {
                buf.putString(entry.getKey(), ENCODER);
                buf.putString(": ", ENCODER);
                buf.putString(entry.getValue(), ENCODER);
                buf.put(CRLF);
            }

            buf.putString("Content-Length: ", ENCODER);
            buf.putString(String.valueOf(message.getBodyLength()), ENCODER);
            buf.put(CRLF);
            buf.put(CRLF);

            buf.put(message.getBody());
        }
        catch (CharacterCodingException ex)
        {
            ex.printStackTrace();
        }

        buf.flip();
        out.write(buf);
    }
}