package com.td.ca.web.controller.helper;

import com.fasterxml.jackson.databind.JavaType;
import com.td.ca.base.dataservice.ServiceBean;
import com.td.ca.base.util.db.table.data.BooleanValue;
import com.td.ca.base.util.exception.AppException;
import com.td.ca.base.util.exception.BaseErrorCode;
import com.td.ca.base.util.exception.ErrorCodeMsgManager;
import com.td.ca.base.util.io.IOUtil;
import com.td.ca.base.util.json.JSONUtil;

import com.td.ca.base.util.objutil.StringUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author tuhanbao
 *
 */
public abstract class BaseHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> {

    protected BaseHttpMessageConverter() {
        super();
    }

    protected BaseHttpMessageConverter(MediaType supportedMediaType) {
        super(supportedMediaType);
    }

    public BaseHttpMessageConverter(MediaType... types) {
        super(types);
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return this.canRead(mediaType);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return this.canWrite(mediaType);
    }

    @Override
    protected void writeInternal(Object o, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        Charset charset = this.getCharset(outputMessage.getHeaders());
        OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody(), charset);

        ResultBean rb = null;
        if (o instanceof ResultBean) {
            rb = (ResultBean) o;
        } else {
            if (o == null) {
                //保证有data
                rb = ResultBean.getSuccessResult();
            } else {
                rb = new ResultBean();
                rb.setMsg(ErrorCodeMsgManager.getErrMsg(BaseErrorCode.NO_ERROR));
                rb.setData(o);
            }
        }
        writer.append(convertResultBean(rb));
        writer.close();
    }

    protected abstract String convertResultBean(ResultBean rb);


    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return this.read(clazz, null, inputMessage);
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        InputStreamReader isr = new InputStreamReader(inputMessage.getBody(), this.getCharset(inputMessage.getHeaders()));

        char[] total = new char[0];
        try {
            CharBuffer dst = CharBuffer.allocate(1000);
            int length;
            while ((length = isr.read(dst)) != -1) {
                char[] temp = total;
                total = new char[temp.length + length];
                System.arraycopy(temp, 0, total, 0, temp.length);
                System.arraycopy(dst.array(), 0, total, temp.length, length);
                dst.clear();
            }
        } finally {
            IOUtil.close(isr);
        }

        Object obj = parse(type, inputMessage, total);
        if (inputMessage.getHeaders().containsKey(DiyRequestBodyAdvice.VALID)) {
            invalidServiceBean(inputMessage, obj);
        }
        return obj;
    }

    private void invalidServiceBean(HttpInputMessage inputMessage, Object obj) {
        String value = inputMessage.getHeaders().getFirst(DiyRequestBodyAdvice.VALID);
        boolean isSelective = true;
        if (!StringUtil.isEmpty(value)) {
            isSelective = BooleanValue.valueOf(value).getValue();
        }

        if (obj instanceof List) {
            for (Object item : (List) obj) {
                if (obj instanceof ServiceBean) {
                    ServiceBeanValidatorFactoryBean.validate((ServiceBean) item, isSelective);
                }
            }
        } else {
            if (obj instanceof ServiceBean) {
                ServiceBeanValidatorFactoryBean.validate((ServiceBean) obj, isSelective);
            }
        }
    }

    private Object parse(Type t, HttpInputMessage inputMessage, char[] total) {
        return JSONUtil.getBean(new String(total), t);
    }

    private Charset getCharset(HttpHeaders headers) {
        return headers != null && headers.getContentType() != null && headers.getContentType().getCharset() != null
                ? headers.getContentType().getCharset() : IOUtil.DEFAULT_CHARSET;
    }
}
