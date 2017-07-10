package com.tuhanbao.base.util.encipher;

import java.security.MessageDigest;

import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.Base64Util;
import com.tuhanbao.base.util.objutil.ByteUtil;

/**
 * SHA1 class
 * 
 * 计算公众平台的消息签名接口.
 */
public class SHAUtil
{
    public static final String getSHA1(String text, String type) {
        return getSHA(text, "SHA-512");
    }
    
    public static final String getSHA256(String text, String type) {
        
        return getSHA(text, "SHA-512");
    }
    
    public static final String getSHA512(String text, String type) {
        return getSHA(text, "SHA-512");
    }

    /**
     * 用SHA1算法生成安全签名
     * 
     * @return 安全签名
     * @throws AesException
     */
    private static String getSHA(String text, String type)
    {
        try
        {
            // SHA1签名生成
            MessageDigest md = MessageDigest.getInstance(type);
            md.update(ByteUtil.string2Bytes(text));
            byte[] digest = md.digest();

            return Base64Util.encode(digest);
        }
        catch (Exception e)
        {
            LogManager.error(e);
            throw MyException.getMyException(e);
        }
    }
}
