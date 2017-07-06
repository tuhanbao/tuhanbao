package com.tuhanbao.base.util.objutil;

public class ObjectUtil
{   

    public static boolean isEqual(Object obj1, Object obj2)
    {
        if (obj1 == null)
        {
            return obj2 == null;
        }
        else
        {
            return obj1.equals(obj2);
        }
    }
}
