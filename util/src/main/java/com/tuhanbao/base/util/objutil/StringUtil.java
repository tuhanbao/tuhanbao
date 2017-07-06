package com.tuhanbao.base.util.objutil;

import java.util.Collection;

import com.tuhanbao.base.Constants;

/**
 * 字符串工具
 * @author tuhanbao
 *
 */
public class StringUtil
{
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static final String[][] EMPTY_STRING_ARRAYS = new String[0][];

    public static final int[] EMPTY_INT_ARRAY = new int[0];

    public static final int[][] EMPTY_INT_ARRAYS = new int[0][];

    public static final long[] EMPTY_LONG_ARRAY = new long[0];

    public static final String NUMBER_REGEX = "^[1-9]\\d*$";
    
    public static boolean isEmpty(String s)
    {
        return s == null || s.trim().isEmpty();
    }
    
    public static boolean isEqual(String s1, String s2)
    {
        if (isEmpty(s1) && isEmpty(s2)) return true;
        if (s1 != null) return s1.equals(s2);
        else return s2.equals(s1);
    }
    /*
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     * START
     * 以下代码，直至END都是用于切割或组装字符串
     * 如将多个对象用逗号进行拼接或使用冒号分隔等等
     */
    public static String[] string2Array(String str)
    {
        return string2Array(str, Constants.COMMA);
    }
    
    public static String[][] string2Arrays(String str)
    {
        return string2Arrays(str, Constants.COMMA, Constants.COLON);
    }
    
    public static String[] string2Array(String str, String sep)
    {
        if (isEmpty(str)) return EMPTY_STRING_ARRAY;
        
        //这里有个小bug，split方法会忽略掉后面所有的空字符。比如"1,,,,"按","进行分割，最后只得到一位长度的数组
        //所以先给字符串增加一个无效位，然后分割，最后再删除
        //无效位不能与sep一样，否则会出错
        String invilidTail = Constants.WELL;
        if (sep.equals(Constants.WELL))
        {
            invilidTail = Constants.COLON;
        }
        
        //有些正则表达式占用过的特殊符号，需要+"\"
        sep = changedSep(sep);
        
        String[] split = (str + invilidTail).split(sep);
        String lastChild = split[split.length - 1];
        split[split.length - 1] = lastChild.substring(0, lastChild.length() - invilidTail.length());
        return split;
    }
    
    private static String changedSep(String sep)
    {
        //有些正则表达式占用过的特殊符号，需要+"\"
        if (sep.equals(Constants.STOP_EN) || sep.equals(Constants.QUESTION_MARK) || 
                sep.equals(Constants.STAR) || sep.equals("|") || sep.equals("\\")) sep = "\\" + sep;
        return sep;
    }
    
    public static int[] string2IntArray(String str)
    {
        return string2IntArray(str, Constants.COMMA);        
    }

    public static int[][] string2IntArrays(String str)
    {
        return string2IntArrays(str, Constants.COMMA, Constants.COLON);        
    }

    public static int[] string2IntArray(String str, String sep)
    {
        if (isEmpty(str)) return EMPTY_INT_ARRAY;
        String[] array = string2Array(str, sep);
        int length = array.length;
        int[] intArray = new int[length];
        for (int i = 0; i < length; i++)
        {
            String s = array[i].trim();
            if (s.isEmpty())
            {
                intArray[i] = 0;
            }
            else
            {
                intArray[i] = Integer.valueOf(s);
            }
        }
        return intArray;                       
    }

    public static int[][] string2IntArrays(String str, String sep1, String sep2)
    {
        if (isEmpty(str)) return EMPTY_INT_ARRAYS;
        String[] array = string2Array(str, sep1);
        int length = array.length;
        int[][] arrays = new int[length][];
        for (int i = 0; i < length; i++)
        {
            arrays[i] = string2IntArray(array[i], sep2);
        }
        return arrays;                    
    }

    public static long[] string2LongArray(String str)
    {
        return string2LongArray(str, Constants.COMMA);                       
    }

    public static long[] string2LongArray(String str, String sep)
    {
        if (isEmpty(str)) return EMPTY_LONG_ARRAY;
        String[] array = string2Array(str, sep);
        int length = array.length;
        long[] longArray = new long[length];
        for (int i = 0; i < length; i++)
        {
            String s = array[i].trim();
            if (s.isEmpty())
            {
                longArray[i] = 0;
            }
            else
            {
                longArray[i] = Long.valueOf(s);
            }
        }
        return longArray;                       
    }
    
    public static String[][] string2Arrays(String str, String sep1, String sep2)
    {
        if (isEmpty(str)) return EMPTY_STRING_ARRAYS;
        String[] array = string2Array(str, sep1);
        int length = array.length;
        String[][] arrays = new String[length][];
        for (int i = 0; i < length; i++)
        {
            arrays[i] = string2Array(array[i], sep2);
        }
        return arrays;
    }
    
    public static String array2String(Object[] array)
    {
        return array2String(array, Constants.COMMA);
    }
    
    public static String array2String(Collection<?> array)
    {
        return array2String(array.toArray(), Constants.COMMA);
    }
    
    public static String arrays2String(Object[][] arrays)
    {
        return arrays2String(arrays, Constants.COMMA, Constants.COLON);
    }
    
    public static String array2String(Object[] array, String sep)
    {
        if (array == null || array.length == 0) return Constants.EMPTY;
        StringBuilder sb = new StringBuilder();
        int length = array.length;
        if (array[0] != null)
        {
            sb.append(array[0]);
        }
        for (int i = 1; i < length; i++)
        {
            if (array[i] == null)
            {
                sb.append(sep);
            }
            else
            {
                sb.append(sep).append(array[i]);
            }
        }
        return sb.toString();
    }
    
    public static String array2String(Collection<?> cols, String sep)
    {
        if (cols == null || cols.size() == 0) return Constants.EMPTY;
        return array2String(cols.toArray(), sep);
    }
    
    public static String arrays2String(Object[][] arrays, String sep1, String sep2)
    {
        if (arrays == null || arrays.length == 0) return Constants.EMPTY;
        int length = arrays.length;
        String[] array = new String[arrays.length];
        for (int i = 0; i < length; i++)
        {
            array[i] = array2String(arrays[i], sep2);
        }
        return array2String(array, sep1);
    }
    
    public static String arrays2String(int[][] arrays, String sep1, String sep2)
    {
        if (arrays == null || arrays.length == 0) return Constants.EMPTY;
        int length = arrays.length;
        String[] array = new String[arrays.length];
        for (int i = 0; i < length; i++)
        {
            array[i] = array2String(arrays[i], sep2);
        }
        return array2String(array, sep1);
    }
    
    public static String array2String(long[] array)
    {
        return array2String(array, Constants.COMMA);
    }
    
    public static String array2String(long[] array, String sep)
    {
        if (array == null || array.length == 0) return Constants.EMPTY;
        StringBuilder sb = new StringBuilder();
        int length = array.length;
        sb.append(array[0]);
        for (int i = 1; i < length; i++)
        {
            sb.append(sep).append(array[i]);
        }
        return sb.toString();
    }

    
    public static String array2String(double[] array)
    {
        return array2String(array, Constants.COMMA);
    }
    
    public static String array2String(double[] array, String sep)
    {
        if (array == null || array.length == 0) return Constants.EMPTY;
        StringBuilder sb = new StringBuilder();
        int length = array.length;
        sb.append(array[0]);
        for (int i = 1; i < length; i++)
        {
            sb.append(sep).append(array[i]);
        }
        return sb.toString();
    }
    
    public static String array2String(int[] array)
    {
        return array2String(array, Constants.COMMA);
    }

    public static String array2String(int[] array, String sep)
    {
        if (array == null || array.length == 0) return Constants.EMPTY;
        StringBuilder sb = new StringBuilder();
        int length = array.length;
        sb.append(array[0]);
        for (int i = 1; i < length; i++)
        {
            sb.append(sep).append(array[i]);
        }
        return sb.toString();
    }
    
    /*
     * END
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    
    public static int getStrNumInStr(String input, String gap)
    {
        int length = gap.length();
        int count = 0;
        int index = input.indexOf(gap);
        while (index != -1)
        {
            count++;
            index = input.indexOf(gap, index + length);
        }
        
        return count;
    }
    
    public static boolean isNumber(String text) {
        return text.matches(NUMBER_REGEX);
    }
    
    public static String truncateString(String s, int length) {
        if (s == null || s.length() <= length) return s;
        
        return s.substring(0, length - 3) + Constants.ELLIPSIS;
    }
    
//    /**
//     * 转码
//     * @param str
//     * @return
//     */
//    private static String changeStr2Unicode(String str)
//    {
//        StringBuilder sb = new StringBuilder();
//        for (char c : str.toCharArray())
//        {
//            sb.append(toString(c));
//        }
//        return sb.toString();
//    }
//    
//    /**
//     * 解码
//     * @param str
//     * @return
//     */
//    private static String changeUnicode2Str(String str)
//    {
//        //需要把\\u转成字符
//        StringBuilder sb = new StringBuilder();
//        
//        while (str.length() >= 4)
//        {
//            sb.append((char)Integer.parseInt(str.substring(0, 4), 16));
//            str = str.substring(4);
//        }
//        return sb.toString();
//    }
//    
//    private static String toString(char c)
//    {
//        String s = Integer.toString(c, 16);
//        int zeroNum = 4 - s.length();
//        for (int i = 0; i < zeroNum; i++)
//        {
//            s = "0" + s;
//        }
//        return s;
//    }
    

    /**
     * 与array2String的区别在于，他每次会检查字符串末尾是不是已经有了gap，有的话不会再加
     * 
     * @param gap
     * @param paths
     * @return
     */
    public static String appendStr(String gap, String ... paths)  {
        if (paths == null || paths.length == 0) return null;
        
        StringBuilder sb = new StringBuilder();
        int length = paths.length;
        String lastPath = null;
        for (int i = 0; i < length; i++) {
            String path = paths[i];
            if (StringUtil.isEmpty(path)) {
                continue;
            }
            
            if (!StringUtil.isEmpty(lastPath) && !lastPath.endsWith(gap)) {
                sb.append(gap);
            }
            sb.append(path);
            lastPath = path;
        }
        return sb.toString();
        
    }
    
}
