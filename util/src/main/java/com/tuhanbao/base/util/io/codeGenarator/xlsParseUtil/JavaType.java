package com.tuhanbao.base.util.io.codeGenarator.xlsParseUtil;

import com.tuhanbao.base.Constants;

public enum JavaType
{
    BYTE("byte", "Byte"), INT("int", "Integer"), SHORT("short", "Short"), FLOAT("float", "Float"), STRING("String", "String"), BOOLEAN("boolean", "Boolean"), OBJECT("", "");
    
    private String bigType;
    private String type;

    private JavaType(String type, String bigType)
    {
        this.type = type;
        this.bigType = bigType;
    }
    
    public static JavaType getType(String s)
    {
        s = s.toUpperCase();
        if (s.contains(Constants.LEFT_BRACKET))
        {
            s = s.substring(0, s.indexOf(Constants.LEFT_BRACKET));
        }
        for (JavaType t : values())
        {
            if (t.name().equals(s))
            {
                return t;
            }
        }
        
        return OBJECT;
    }
    
    public String getBigType()
    {
        return bigType;
    }

    public String getType()
    {
        return type;
    }
}
