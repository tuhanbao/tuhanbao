package com.tuhanbao.study.mina;

import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tuhanbao.io.base.Constants;
import com.tuhanbao.util.exception.BaseErrorCode;
import com.tuhanbao.util.exception.MyException;

public class MessageFilter implements IMessageFilter
{
    public static final MessageFilter instance = new MessageFilter();
    
    private MessageFilter()
    {
    }
    
    @Override
    public void check(IoSession session, JSONObject json, boolean isHttp) throws JSONException
    {
        int code = json.getIntValue(Constants.CMD);
        
        if (!isHttp)
        {
            //如果还没有登录过，而且code不是登录命令，直接报错关闭
            if (session.getAttribute(Constants.SAVE_SESSION_OBJECT) == null && !isFirstCode(code))
            {
                throw new MyException(BaseErrorCode.PLEASE_LOGIN_FIRST);
            }
        }
    }
    
    private static final boolean isFirstCode(int code)
    {
        return code == Constants.LOGIN;
    }
}
