package com.tuhanbao.base.util.encipher;

import org.apache.xmlbeans.impl.util.Base64;

import com.tuhanbao.base.util.objutil.NumberUtil;

public class SelfEncipherTool implements IEncipherTool {

    private byte[] password;

    private int passwordLength;

    private static final int GROUP_LENGTH = 11;
    
    private SelfEncipherTool(byte[] password)
    {
        this.password = password;
        passwordLength = this.password.length;
    }

    public static IEncipherTool getInstance(String password) {
        return new SelfEncipherTool(password.getBytes());
    }
    
    public byte[] encrypt(byte[] bytes)
    {
        if (bytes == null) return null;
        //加密方法：
        //1.跟密码异或，然后按照写死的规律调换位置及运算。
        int length = bytes.length;
        for (int i = 0; i < length; i++)
        {
            bytes[i] ^= password[i % passwordLength];
        }
        
        //将异或后的bytes按11位进行分组
        int groups = NumberUtil.getInt(length  * 1d / GROUP_LENGTH);
        for (int i = 0; i < groups; i++)
        {
            int start = i * GROUP_LENGTH;
            //对每组进行置换及运算
            //No.1 + 5与No.7 - 7调换。
            exchange(bytes, start, start + 6, 5, -7);
            //No.2 - 11与No.3 + 29调换
            exchange(bytes, start + 1, start + 2, -11, 29);
            //No.4 + 1与No.9 - 19调换
            exchange(bytes, start + 3, start + 8, 1, -19);
            //No.5与No.11 - 7调换
            exchange(bytes, start + 4, start + 10, 0, -7);
            //No.6与No.7 + 59调换
            exchange(bytes, start + 5, start + 6, 0, 59);
            //No.8 - 73与No.10调换
            exchange(bytes, start + 7, start + 9, -73, 0);
        }
        return Base64.encode(bytes);
    }

    private static void exchange(byte[] bytes, int index1, int index2, int increment1, int increment2) 
    {
        if (index1 >= bytes.length || index2 >= bytes.length) return;
                
        bytes[index1] += increment1;
        bytes[index2] += increment2;
        bytes[index1] ^= bytes[index2];
        bytes[index2] = (byte) (bytes[index1] ^ bytes[index2]);
        bytes[index1] ^= bytes[index2];
    }

    public byte[] decrypt(byte[] bytes)
    {
        //加密方法：
        //然后按照写死的规律调换位置及运算，然后异或密码
        //将异或后的bytes按11位进行分组
        bytes = Base64.decode(bytes);
        if (bytes == null) return null;
        
        int length = bytes.length;
        int groups = NumberUtil.getInt(length * 1d / GROUP_LENGTH);
        for (int i = 0; i < groups; i++)
        {
            int start = i * GROUP_LENGTH;
            //对每组进行置换及运算
            //No.8与No.10 + 73调换
            exchange(bytes, start + 7, start + 9, 0, 73);
            //No.6 - 59与No.7调换
            exchange(bytes, start + 5, start + 6, -59, 0);
            //No.5 + 7与No.11调换
            exchange(bytes, start + 4, start + 10, 7, 0);
            //No.4 + 19与No.9 - 1调换
            exchange(bytes, start + 3, start + 8, 19, -1);
            //No.2 - 29与No.3 + 11调换
            exchange(bytes, start + 1, start + 2, -29, 11);
            //No.1 + 7与No.7 - 5调换。
            exchange(bytes, start, start + 6, 7, -5);
        }
        
        for (int i = 0; i < length; i++)
        {
            bytes[i] ^= password[i % passwordLength];
        }
        return bytes;
    }
}
