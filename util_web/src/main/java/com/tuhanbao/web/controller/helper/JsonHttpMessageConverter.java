package com.tuhanbao.web.controller.helper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.exception.ErrorCodeMsgManager;
import com.tuhanbao.base.util.io.IOUtil;

/**
 * Created by dell on 2016/6/16.
 */
public class JsonHttpMessageConverter extends AbstractHttpMessageConverter<Object> {
	
    public static final Charset DEFAULT_CHARSET = Charset.forName(IOUtil.DEFAULT_CHARSET);
    
    public static final Object NULL = new Object();
    
    public JsonHttpMessageConverter() {
        super(new MediaType[]{MediaType.APPLICATION_JSON, MediaType.TEXT_HTML ,new MediaType("application", "*+json", DEFAULT_CHARSET)});
    }

    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return this.canRead(mediaType);
    }

    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return this.canWrite(mediaType);
    }

    protected boolean supports(Class<?> clazz) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        Charset charset = this.getCharset(outputMessage.getHeaders());
        OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody(), charset);
        
        ResultBean rb = null;
        if (o instanceof ResultBean) {
        	rb = (ResultBean) o;
        }
        else {
        	rb = new ResultBean();
        	if (o == NULL) {
        	    rb.setMsg(ErrorCodeMsgManager.getErrMsg(BaseErrorCode.NO_ERROR));
        	    rb.setData(Constants.EMPTY);
        	}
        	else if (o instanceof String) {
        	    rb.setMsg((String)o);
        	    rb.setData(Constants.EMPTY);
        	}
        	else {
        	    rb.setMsg(ErrorCodeMsgManager.getErrMsg(BaseErrorCode.NO_ERROR));
        	    rb.setData(o);
        	}
        }
        writer.append(rb.toString());
        writer.close();
    }

    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        InputStreamReader isr = new InputStreamReader(inputMessage.getBody(), this.getCharset(inputMessage.getHeaders()));
        
        byte[] total = new byte[0];
        try
        {
            CharBuffer dst = CharBuffer.allocate(1000);
            int length;
            while ((length = isr.read(dst)) != -1)
            {
                byte[] temp = total;
                total = new byte[temp.length + length];
                System.arraycopy(temp, 0, total, 0, temp.length);
                System.arraycopy(dst.array(), 0, total, temp.length, length);
                dst.clear();
            }
        }
        finally
        {
            IOUtil.close(isr);
        }
        return JSON.parseObject(total, clazz);
    }

    private Charset getCharset(HttpHeaders headers) {
        return headers != null && headers.getContentType() != null && headers.getContentType().getCharSet() != null ? headers.getContentType().getCharSet() : DEFAULT_CHARSET;
    }
}
