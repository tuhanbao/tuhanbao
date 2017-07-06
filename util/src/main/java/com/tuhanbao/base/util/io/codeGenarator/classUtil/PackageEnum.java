package com.tuhanbao.base.util.io.codeGenarator.classUtil;

public enum PackageEnum
{
    PUBLIC(0), PRIVATE(1), PROTECTED(2);
    
    private int value;
    
    private PackageEnum(int value)
    {
        this.value = value;
    }
    
    public static PackageEnum getPackageEnum(int value)
    {
        for (PackageEnum pe : PackageEnum.values())
        {
            if (pe.value == value)
            {
                return pe;
            }
        }
        
        return null;
    }
    
    public static PackageEnum getPackageEnum(String s)
    {
        for (PackageEnum pe : PackageEnum.values())
        {
            if (pe.name().toLowerCase().equals(s))
            {
                return pe;
            }
        }
        
        return null;
    }
}
