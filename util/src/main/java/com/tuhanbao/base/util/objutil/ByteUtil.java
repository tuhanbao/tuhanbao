package com.tuhanbao.base.util.objutil;

import java.io.UnsupportedEncodingException;

import com.tuhanbao.base.Constants;

public final class ByteUtil
{
    public static byte[] string2Bytes(String s)
    {
        try
        {
            return s.getBytes(Constants.UTF_8);
        }
        catch (UnsupportedEncodingException e)
        {
            return s.getBytes();
        }
    }
    
    public static String bytes2String(byte[] bs)
    {
        if (bs == null) return null;
        try
        {
            return new String(bs, Constants.UTF_8);
        }
        catch (UnsupportedEncodingException e)
        {
            return new String(bs);
        }
    }
    
    public static String bytes2String(byte[] bs, int offset, int length)
    {
        try
        {
            return new String(bs, offset, length, Constants.UTF_8);
        }
        catch (UnsupportedEncodingException e)
        {
            return new String(bs, offset, length);
        }
    }
    
    /**
     * 将bytebuffer读取成字符串，并按“,”分隔
     * 
     * @param bb
     * @return
     */
    public static String[] bytes2StringArray(ByteBuffer bb)
    {
        return bytes2StringArray(bb, Constants.COMMA);
    }
    

    /**
     * 将bytebuffer读取成字符串，并按sep分隔
     * 
     * @param bb
     * @param sep 分隔符
     * @return
     */
    public static String[] bytes2StringArray(ByteBuffer bb, String sep)
    {
        return bytes2String(bb.getData()).split(sep);
    }
}
