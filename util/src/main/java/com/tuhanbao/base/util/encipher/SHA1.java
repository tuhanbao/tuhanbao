package com.tuhanbao.base.util.encipher;

import java.security.MessageDigest;

import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.ByteUtil;

/**
 * SHA1 class
 * 
 * 计算公众平台的消息签名接口.
 */
public class SHA1
{

    /**
     * 用SHA1算法生成安全签名
     * 
     * @return 安全签名
     * @throws AesException
     */
    public static String getSHA1(String text)
    {
        try
        {
            // SHA1签名生成
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(ByteUtil.string2Bytes(text));
            byte[] digest = md.digest();

            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++)
            {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2)
                {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
        }
        catch (Exception e)
        {
            LogManager.error(e);
            throw new MyException(BaseErrorCode.ERROR);
        }
    }
}
