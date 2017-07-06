package com.tuhanbao.base.util.io.codeGenarator.xlsParseUtil;

import com.tuhanbao.base.Constants;

public enum BufferType
{
    //BufferType主要是根据我们的ByteBuffer而来，比如short:readShort返回的是short，但是writeShort又传入的是int
    //有些BufferType其实又只有读的方法，没有写的方法，这里一起处理
    BYTE("Byte", "Byte", JavaType.BYTE, JavaType.INT), INT("Int", "Int", JavaType.INT), SHORT("Short", "Short", JavaType.SHORT, JavaType.INT), FLOAT(
            "Float", "Float", JavaType.FLOAT), UTF("UTF", "UTF", JavaType.STRING), UNSIGNEDBYTE("UnsignedByte", "Byte",
            JavaType.INT), UNSIGNEDSHORT("UnsignedShort", "Short", JavaType.INT), BOOLEAN("Boolean", "Boolean", JavaType.BOOLEAN),OBJECT("", "", JavaType.OBJECT);

    private String readMethodStr, writeMethodStr;

    private JavaType readJavaType, writeJavaType;

    private BufferType()
    {
    }

    private BufferType(String readMethodStr)
    {
        this(readMethodStr, readMethodStr);
    }

    private BufferType(String readMethodStr, String writeMethodStr)
    {
        this.readMethodStr = readMethodStr;
        this.writeMethodStr = writeMethodStr;
    }
    
    private BufferType(String readMethodStr, String writeMethodStr, JavaType javaType)
    {
        this(readMethodStr, writeMethodStr, javaType, javaType);
    }

    private BufferType(String readMethodStr, String writeMethodStr, JavaType readJavaType, JavaType writeJavaType)
    {
        this.readMethodStr = readMethodStr;
        this.writeMethodStr = writeMethodStr;
        this.readJavaType = readJavaType;
        this.writeJavaType = writeJavaType;
    }

    public String getReadMethodStr()
    {
        return readMethodStr;
    }

    public String getWriteMethodStr()
    {
        return writeMethodStr;
    }
    
    public JavaType getReadJavaType()
    {
        return readJavaType;
    }

    
    public JavaType getWriteJavaType()
    {
        return writeJavaType;
    }


    public static BufferType getType(String s)
    {
        s = s.toUpperCase();
        if (s.contains(Constants.LEFT_BRACKET))
        {
            s = s.substring(0, s.indexOf(Constants.LEFT_BRACKET));
        }
        for (BufferType t : values())
        {
            if (t.name().equals(s))
            {
                return t;
            }
        }

        return OBJECT;
    }

}
