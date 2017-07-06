package com.tuhanbao.base.util.io.codeGenarator.codeUtil;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.ClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.EnumClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.NotesInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.VarInfo;
import com.tuhanbao.base.util.objutil.StringUtil;

public abstract class Xls2CodeUtil
{
    public static String getGap(int n)
    {
        switch (n)
        {
            case 1:
                return Constants.GAP1;
            case 2:
                return Constants.GAP2;
            case 3:
                return Constants.GAP3;
            case 4:
                return Constants.GAP4;
            case 5:
                return Constants.GAP5;
            default:
                return Constants.EMPTY;
        }
    } 
    
    public static NotesInfo getNotesInfo(String[] arrays, int gap)
    {
        NotesInfo notes = new NotesInfo(0);
        if (arrays != null)
        {
            for (String s : arrays)
            {
                notes.addNote(getNotesInfo(s, gap));
            }
        }
        return notes;
    }
    
    public static NotesInfo getNotesInfo(String s)
    {
        return getNotesInfo(s, 0);
    }
    
    public static NotesInfo getNotesInfo(String s, int gap)
    {
        if (s != null && !s.trim().isEmpty())
        {
            NotesInfo notes = new NotesInfo(gap);
            String[] array = s.split(Constants.ENTER);
            for (String t : array)
            {
                notes.addNote(t);
            }
            return notes;
        }
        return null;
    }
    
    public static boolean isEmptyLine(String[] array)
    {
        if (array == null || array.length == 0) return true;
        
        //行的第一格为注释则表示该行全部为注释
        if (!isEmpty(array[0]) && array[0].trim().charAt(0) == Constants.POUND_SIGN)
        {
            return true;
        }
        
        //最后一种情况，该行全部为空或者全部为注释
        for (String s : array)
        {
            if (!isEmpty(s) && s.trim().charAt(0) != Constants.POUND_SIGN)
            {
                return false;
            }
        }
        
        return true;
    }
    
    public String str2ClassName(String s)
    {
        if (s == null) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    
    public static String getString(String[] array, int index)
    {
        if (array == null || array.length <= index)
        {
            return null;
        }
        else
            return array[index];
    }

    private static boolean isEmpty(String s)
    {
        return StringUtil.isEmpty(s);
    }
    
    public abstract String getClassSrcUrl(ClassInfo classInfo);
    
    public abstract String getNoteStr(NotesInfo note);
    
    public abstract String getVarInfoStr(VarInfo var);
    
    public abstract String getEnumStr(EnumClassInfo enumInfo);
    
    public abstract String getClassStr(ClassInfo classInfo);
    
//    
//    public static String string2Notes(String gap, String desc)
//    {
//        if (StringUtil.isEmpty(desc)) return Constants.EMPTY;
//        StringBuilder sb = new StringBuilder();
//        StringTokenizer st = new StringTokenizer(desc, "\n\r");
//        while(st.hasMoreTokens())
//        {
//            String s = st.nextToken();
//            sb.append(gap).append("//").append(s).append(Constants.ENTER);
//        }
//        return sb.toString();
//    }
//    
    /**
     * 将xls读取的内容转换为配置文件，
     * 只是简单地根据行，列进行组装
     */
    public String xls2Config(String[][] arrays)
    {
        StringBuilder sb = new StringBuilder();
        for (String[] array : arrays)
        {
            if (isEmptyLine(array)) continue;
            
            //先将array中的每个元素的换行要替换掉
            for (int i = 0; i < array.length; i++)
            {
                array[i] = array[i].replace("\n", "\\n");
            }
            
            sb.append(StringUtil.array2String(array, Constants.SEMICOLON)).append(Constants.ENTER);
        }
        
        if (sb.charAt(sb.length() - 1) == '\n') sb.deleteCharAt(sb.length() - 1);
          
        return sb.toString();
    }

    /**
     * 将xls读取的内容转换为配置文件，
     * 只是简单地根据行，列进行组装
     * 
     * length，只取前length列，之后的不管
     */
    public String xls2Config(String[][] arrays, int length)
    {
        StringBuilder sb = new StringBuilder();
        for (String[] array : arrays)
        {
            if (isEmptyLine(array)) continue;
            
            //先将array中的每个元素的换行要替换掉
            for (int i = 0; i < array.length; i++)
            {
                array[i] = array[i].replace("\n", "\\n");
            }
            
            String[] temp = new String[length];
            if (array.length > length)
            {
                System.arraycopy(array, 0, temp, 0, length);    
            }
            else
            {
                System.arraycopy(array, 0, temp, 0, array.length);    
            }
            
            sb.append(StringUtil.array2String(temp, Constants.SEMICOLON)).append(Constants.ENTER);
        }
        if (sb.charAt(sb.length() - 1) == '\n') sb.deleteCharAt(sb.length() - 1); 
        return sb.toString();
    }
}
