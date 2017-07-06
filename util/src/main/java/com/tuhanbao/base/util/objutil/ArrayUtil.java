package com.tuhanbao.base.util.objutil;

import java.util.Collection;
import java.util.Iterator;

import com.tuhanbao.base.Constants;

public final class ArrayUtil
{
    /**
     * 将数组拼接成字符串
     * @param col
     * @param delimiter
     * @return
     */
	public static String collection2Str(Collection<?> col, String delimiter)
    {
        if (col == null || col.isEmpty()) return Constants.EMPTY;
        StringBuilder sb = new StringBuilder();
        for(Object o : col)
        {
            sb.append(o).append(delimiter);
        }
        
        if (sb.length() > 0) sb.delete(sb.length() - delimiter.length(), sb.length());
        return sb.toString();
    }
    
    /**
     * 将数组拼接成字符串
     * @param col
     * @param delimiter
     * @return
     */
    public static String array2Str(Object[] col, String delimiter)
    {
        if (col == null) return Constants.EMPTY;
        StringBuilder sb = new StringBuilder();
        for(Object o : col)
        {
            sb.append(o).append(delimiter);
        }
        
        if (sb.length() > 0) sb.delete(sb.length() - delimiter.length(), sb.length());
        return sb.toString();
    }
    
    public static boolean contain(Object[] objects, Object obj)
    {
        if (objects == null || objects.length == 0) return false;
        
        for (Object entry : objects)
        {
            if (entry == null && obj == null) return true;
            if (entry != null && entry.equals(obj)) return true;
        }
        
        return false;
    }
    
    public static <T extends Object> T indexOf(T[] array, int index) {
    	if (array == null || array.length <= index) {
    		return null;
    	}
    	else {
    		return array[index];
    	}
    }
    
    public static boolean isEmptyLine(String[] array)
    {
        if (array == null || array.length == 0) return true;
        
        
        //行的第一格为注释则表示该行全部为注释
        if (!StringUtil.isEmpty(array[0]) && array[0].trim().charAt(0) == Constants.POUND_SIGN)
        {
            return true;
        }
        
        //最后一种情况，该行全部为空或者全部为注释
        for (String s : array)
        {
            if (!StringUtil.isEmpty(s) && s.trim().charAt(0) != Constants.POUND_SIGN)
            {
                return false;
            }
        }
        
        return true;
    }
    
    public static void removeNullItem(Collection<?> col) {
        if (col == null || col.isEmpty()) return;
        
        for (Iterator<?> it = col.iterator(); it.hasNext();) {
            if (it.next() == null) it.remove();
        }
    }
}
