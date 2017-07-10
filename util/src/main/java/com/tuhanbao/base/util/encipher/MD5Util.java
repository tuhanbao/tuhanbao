package com.tuhanbao.base.util.encipher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.tuhanbao.base.util.log.LogManager;

/**
 * 网上截取的代码
 * 在上面的基础上有打乱16进制顺序
 * @author tuhanbao
 * 
 */
public class MD5Util
{
    private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f' };

    private static MessageDigest messagedigest = null;

    static
    {
        try
        {
            messagedigest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            LogManager.error(e);
        }
    }

    public static String getMD5String(String s)
    {
        return getMD5String(s.getBytes());
    }

    public static String getMD5String(byte[] bytes)
    {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte bytes[])
    {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n)
    {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++)
        {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer)
    {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}