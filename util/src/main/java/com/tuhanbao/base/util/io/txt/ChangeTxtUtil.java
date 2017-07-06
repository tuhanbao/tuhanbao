package com.tuhanbao.base.util.io.txt;

import java.io.IOException;
import java.util.StringTokenizer;

import com.tuhanbao.base.util.io.IOUtil;

/**
 * 改变一个文本
 * @author tuhanbao
 *
 */
public abstract class ChangeTxtUtil
{
    private String charset;
    
    private String delim = "\n\r";
    
    public ChangeTxtUtil()
    {
        this(IOUtil.DEFAULT_CHARSET);
    }
    
    
    public ChangeTxtUtil(String charset)
    {
        this.charset = charset;
    }
    
    public String getChangeStr(String inFileUrl) throws IOException
    {
        String s = TxtUtil.read(inFileUrl, charset);
        
        StringTokenizer st = new StringTokenizer(s, delim);
        
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens())
        {
            String temp = st.nextToken();
            sb.append(change(temp));
        }
        
        return sb.toString();
    }
    
    public void change(String inFileUrl, String outFileUrl) throws IOException
    {
        TxtUtil.write(outFileUrl, getChangeStr(inFileUrl));
    }
    
    protected abstract String change(String s);

    public String getCharset()
    {
        return charset;
    }

    public void setCharset(String charset)
    {
        this.charset = charset;
    }

    public String getDelim()
    {
        return delim;
    }

    public void setDelim(String delim)
    {
        this.delim = delim;
    }
}
