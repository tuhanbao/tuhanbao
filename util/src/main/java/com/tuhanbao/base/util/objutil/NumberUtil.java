package com.tuhanbao.base.util.objutil;

public class NumberUtil
{
    public static final int CEIL = 0;
    
    public static final int FLOOR = 1;
    
    public static final int ROUND = 2;
    
    public static final int PERCENT_EXPAND_VALUE = 10000;
    
    public static int getInt(double d)
    {
        return getInt(d, CEIL);
    }
    
    public static long getLong(double d)
    {
        return getLong(d, CEIL);
    }
    
    public static long getLong(double d, int type)
    {
        //由于java本身浮点数的误差性，先对value进行一次保底四舍五入，然后再进行处理
        //举例 3 * 0.1d * 10本来等于3，但是由于精度问题，可能结果未2.9999999999999.如果此时做floor操作，会得到2
        d = round(d, 5);
        if (type == CEIL)
            return (long) Math.ceil(d);
        else if (type == FLOOR)
            return (long) Math.floor(d);
        else
            return Math.round(d);
    }
    
    public static int getInt(double d, int type)
    {
        //由于java本身浮点数的误差性，先对value进行一次保底四舍五入，然后再进行处理
        //举例 3 * 0.1d * 10本来等于3，但是由于精度问题，可能结果未2.9999999999999.如果此时做floor操作，会得到2
        d = round(d, 5);
        if (type == CEIL)
            return (int) Math.ceil(d);
        else if (type == FLOOR)
            return (int) Math.floor(d);
        else
            return (int)Math.round(d);
    }
    
    /**
     * @param value
     * @param decimal
     * @return
     */
    public static double round(double value, int decimal)
    {
        int decimalValue = (int) Math.pow(10, decimal);
        return Math.round(value * decimalValue) / 1d / decimalValue;
    }
    
    @Deprecated
    @SuppressWarnings("unused")
    private static double ceil(double value, int decimal)
    {
        int decimalValue = (int) Math.pow(10, decimal);
        return Math.ceil(value * decimalValue) / decimalValue;
    }
    
    @Deprecated
    @SuppressWarnings("unused")
    private static double floor(double value, int decimal)
    {
        int decimalValue = (int) Math.pow(10, decimal);
        return Math.floor(value * decimalValue) / decimalValue;
    }
    
    public static int percent2Int(double d)
    {
        return getInt(d * PERCENT_EXPAND_VALUE, ROUND);
    }

    public static double int2Percent(int percent)
    {
        return percent * 1d / PERCENT_EXPAND_VALUE;
    }
    
    /**
     * 本方法可能会改变到array1,array2
     * @param array1
     * @param array2
     * @return
     */
    public static double[] addDoubleArray(double[] array1, double[] array2)
    {
        if (array1 == null) return array2;
        if (array2 == null) return array1;
        
        int length1 = array1.length;
        int length2 = array2.length;
        if (length1 > length2)
        {
            for (int i = 0; i < length2; i++)
            {
                array1[i] += array2[i];
            }
            return array1;
        }
        else
        {
            for (int i = 0; i < length1; i++)
            {
                array2[i] += array1[i];
            }
            return array2;
        }
        
    }
}
