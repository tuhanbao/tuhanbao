package com.tuhanbao.base.util.io.codeGenarator;


/**
 * 代码类型，占时只支持java和c#,
 * 如果扩展了代码类型，需要实现对应的CodeUtil
 * 
 * @author tuhanbao
 *
 */
public enum CodeType
{
    //java
    JAVA(0),
    
    //c#
    CS(1);
    
    private int value;
    
    private CodeType(int value)
    {
        this.value = value;
    }
    
    public static CodeType getCodeType(int value)
    {
        for (CodeType temp : CodeType.values())
        {
            if (temp.value == value)
            {
                return temp;
            }
        }

        return null;
    }
}
