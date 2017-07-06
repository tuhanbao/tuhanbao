package com.tuhanbao.base.util.objutil;


public class BinaryUtil
{
    public static boolean isZero(int value, int index)
    {
        return ((value >> index) & 1) == 0;
    }
    
    public static int setZero(int value, int index)
    {
        return ~setOne(~value, index);
    }
    
    public static int setOne(int value, int index)
    {
        return ((1 << index) | value);
    }
    
    /**
     * 返回二进制所在位数
     * 
     * 1->0, 2->1 4->2...16->4
     * @param value
     * @return
     */
    public static int getIndex(int value)
    {
        int i = 0;
        while (isZero(value, i)) 
        {
            i++;
        }
        return i;
    }
}
