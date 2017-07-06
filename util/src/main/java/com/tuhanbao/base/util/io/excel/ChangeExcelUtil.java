package com.tuhanbao.base.util.io.excel;

/**
 * 整个类的东西不用太关心
 * 
 * 继承此类，实现change(String[] rowValue)即可
 * 此方法仅能实现一些字符串的简单转换
 * 如去掉标点，小写变大写等操作
 * 
 * @author tuhanbao
 *
 */
public abstract class ChangeExcelUtil
{
    private String inFileURL, outFileURL;
    
    public ChangeExcelUtil(String fileURL)
    {
        this(fileURL, fileURL);
    }
    
    public ChangeExcelUtil(String inFileURL, String outFileURL)
    {
        super();
        this.inFileURL = inFileURL;
        this.outFileURL = outFileURL;
    }

    public void change(int index) throws Exception
    {
        String[][] s = ExcelUtil.read(inFileURL, index);
        
        for (String[] rowValue : s)
        {
            change(rowValue);
        }
        
        if (outFileURL.equals(inFileURL))
        {
            ExcelUtil.writeSheet(inFileURL, index, s);
        }
        else
        {
            ExcelUtil.createSheet(outFileURL, null, s);
        }
    }
    
    protected abstract String change(String[] rowValue);

    /**
     * getter and setter ----------------------------------------------------------------------------------------------------------
     */
    public String getInFileURL()
    {
        return inFileURL;
    }

    public void setInFileURL(String inFileURL)
    {
        this.inFileURL = inFileURL;
    }

    public String getOutFileURL()
    {
        return outFileURL;
    }

    public void setOutFileURL(String outFileURL)
    {
        this.outFileURL = outFileURL;
    }
}
