package com.tuhanbao.study.mina.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoderAdapter;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import com.tuhanbao.io.base.Constants;
import com.tuhanbao.util.log.LogManager;

public class HttpRequestDecoder extends MessageDecoderAdapter
{
    private static final byte[] CONTENT_LENGTH = "Content-Length:".getBytes();

    private static CharsetDecoder DECODER = Charset.forName(Constants.UTF_8).newDecoder();;


    public HttpRequestDecoder()
    {
    }

    public MessageDecoderResult decodable(IoSession session, IoBuffer in)
    {
        try
        {
            return messageComplete(in) ? MessageDecoderResult.OK : MessageDecoderResult.NEED_DATA;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return MessageDecoderResult.NOT_OK;
    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception
    {
        HttpRequestMessage m = decodeBody(in);

        if (m == null)
        {
            return MessageDecoderResult.NEED_DATA;
        }

        out.write(m);

        return MessageDecoderResult.OK;
    }

    private static boolean messageComplete(IoBuffer in)
    {
        int last = in.remaining() - 1;
        if (in.remaining() < 4)
        {
            return false;
        }

        if ((in.get(0) == 71) && (in.get(1) == 69) && (in.get(2) == 84))
        {
            return (in.get(last) == 10) && (in.get(last - 1) == 13) && (in.get(last - 2) == 10)
                    && (in.get(last - 3) == 13);
        }
        if ((in.get(0) == 80) && (in.get(1) == 79) && (in.get(2) == 83) && (in.get(3) == 84))
        {
            int eoh = -1;
            for (int i = last; i > 2; i--)
            {
                if ((in.get(i) == 10) && (in.get(i - 1) == 13) && (in.get(i - 2) == 10) && (in.get(i - 3) == 13))
                {
                    eoh = i + 1;
                    break;
                }
            }
            if (eoh == -1)
            {
                return false;
            }
            for (int i = 0; i < last; i++)
            {
                boolean found = false;
                for (int j = 0; j < CONTENT_LENGTH.length; j++)
                {
                    if (in.get(i + j) != CONTENT_LENGTH[j])
                    {
                        found = false;
                        break;
                    }
                    found = true;
                }
                if (found)
                {
                    StringBuilder contentLength = new StringBuilder();
                    for (int j = i + CONTENT_LENGTH.length; j < last; j++)
                    {
                        if (in.get(j) == 13)
                        {
                            break;
                        }
                        contentLength.append(new String(new byte[] { in.get(j) }));
                    }

                    return Integer.parseInt(contentLength.toString().trim()) + eoh == in.remaining();
                }
            }

        }

        return false;
    }

    public static HttpRequestMessage decodeBody(IoBuffer in)
    {
        try
        {
            return parseRequest(new StringReader(in.getString(DECODER)));
        }
        catch (IOException e)
        {
            LogManager.error(e);
            return null;
        }
    }

    private static HttpRequestMessage parseRequest(StringReader is) throws IOException
    {
        HttpRequestMessage requestMessage = new HttpRequestMessage();
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> parameters = new HashMap<String, String>();
        BufferedReader rdr = new BufferedReader(is);
        
        String line = rdr.readLine();
        String[] url = line.split(" ");
        if (url.length < 3)
        {
            return requestMessage;
        }

        headers.put("URI", line);
        headers.put("Method", url[0].toUpperCase());
        headers.put("Context", url[1].substring(1));
        headers.put("Protocol", url[2]);

        while (((line = rdr.readLine()) != null) && (line.length() > 0))
        {
            String[] tokens = line.split(": ");
            //vivo手机有时候会发空的换行，这里需要判断是否有数据
            if (tokens.length >= 2)
            {
                headers.put(tokens[0], tokens[1]);
            }
        }

        if (url[0].equalsIgnoreCase("POST"))
        {
            int len = Integer.parseInt(headers.get("Content-Length"));
            char[] buf = new char[len];
            if (rdr.read(buf) == len) line = String.copyValueOf(buf);
        }
        else if (url[0].equalsIgnoreCase("GET"))
        {
            int idx = url[1].indexOf('?');
            if (idx != -1)
            {
                headers.put("Context", url[1].substring(1, idx));
                line = url[1].substring(idx + 1);
                
                //get请求需要解码
                line = HttpSendUtil.decode(line);
            }
            else
            {
                line = null;
            }
        }
        if (line != null)
        {
            String[] match = line.split("\\&");
            for (String element : match)
            {
                if (!element.isEmpty())
                {
                    int index = element.indexOf("=");
                    
                    if (index == -1)
                    {
                        parameters.put(element, Constants.EMPTY);
                    }
                    else
                    {
                        parameters.put(element.substring(0, index), element.substring(index + 1).trim());
                    }
                }
            }
        }

        requestMessage.setHeaders(headers);
        requestMessage.setParemeters(parameters);
        return requestMessage;
    }
}