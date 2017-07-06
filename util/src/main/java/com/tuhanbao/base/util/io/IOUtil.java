package com.tuhanbao.base.util.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.log.LogManager;

public class IOUtil
{
    public static final String ENTER = "\n";
    
    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String GBK = "gbk";
    
    public static void close(Closeable closeable)
    {
        if (closeable != null)
        {
            try
            {
                closeable.close();
            }
            catch (IOException e)
            {
            	LogManager.error(e);
            }
        }
    }
    
    /**
     * @param is
     * @return
     * @throws IOException
     */
    public static String read(InputStream is) throws IOException
    {
        return read(is, null);
    }
    
    /**
     * @param is
     * @param timeout
     * @return
     * @throws IOException
     */
    public static String read(InputStream is, EndChecker checker) throws IOException
    {
        if (is == null) return null;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        while (br.ready())
        {
            String line = br.readLine();
            sb.append(line).append(Constants.ENTER);
            //可以指定，当读到某行信息时停止读取
            if (checker != null && checker.isEnd(line))
            {
                break;
            }
        }
        if (sb.length() > 0)
        {
            sb.delete(sb.length() - Constants.ENTER.length(), sb.length());
        }
        return sb.toString();
    }
    
    public static void checkFileExist(String url) {
    	if (!new File(url).exists()) {
    		throw new MyException(BaseErrorCode.FILE_NOT_EXISTS);
    	}
    }
}
