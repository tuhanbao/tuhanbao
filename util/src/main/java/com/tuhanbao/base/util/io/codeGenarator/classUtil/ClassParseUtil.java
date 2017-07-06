package com.tuhanbao.base.util.io.codeGenarator.classUtil;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tuhanbao.base.Constants;

/**
 * 用于解析一个java类文件
 * 
 * @author tuhanbao
 *
 */
public class ClassParseUtil
{
    /**
     * 匹配成员变量的regex
     * 
     * 此正则未经过特别严格的校验，对于一些常见的成员变量写法，是可以过滤出来的。
     * 另：没有写封装类型的变量无法识别
     */
    private static final String VAR_REGEX = "(public|private|protected) [^;{}()=]*(=[^;]*)*;";
    
    /**
     * 匹配方法的regex
     * 
     * 此正则未经过特别严格的校验，对于一些常见的方法写法，是可以过滤出来的。
     * 另：没有写封装类型的方法无法识别
     */
    private static final String METHOD_REGEX = "(public|private|protected) [^;{}()=]*\\([^;{}()=]*\\)[^;{}()=]*\\{";
    
    public static ClassInfo parse(String s)
    {
        ClassInfo c = new ClassInfo();
        //不考虑内部类，枚举类，虚类等特殊情况
        int start = s.indexOf(Constants.PUBLIC_CLASS);
        
        parseImportAndPackageInfo(s.substring(0, start), c);
        
        s = s.substring(start);
        parseClassNameInfo(s, c);
        //解析变量
        parseClassVarInfo(s, c);
        //解析方法
        parseClassMethodInfo(s, c);
        
        return c;
    }

    private static void parseClassMethodInfo(String s, ClassInfo c)
    {
        Pattern p = Pattern.compile(METHOD_REGEX);
        Matcher m = p.matcher(s);
        while (m.find())
        {
            c.addMethodInfo(parseTxt2MethodInfo(m, s));
        }
    }

    private static MethodInfo parseTxt2MethodInfo(Matcher m, String s)
    {
        MethodInfo methodInfo = new MethodInfo();
        String txt = m.group();
        int start = m.start();
        int end = m.end();
        
        txt = txt.substring(0, txt.length() - 1);
        //向前搜索注释
        NotesInfo notes = getNotesInfo(s, start);
        notes.setGap(1);
        
        int index = txt.indexOf(Constants.BLANK);
        PackageEnum pe = PackageEnum.getPackageEnum(txt.substring(0, index));
        txt = txt.substring(index);
        
        boolean isStatic = isStatic(txt);
        boolean isFinal = isFinal(txt);
        txt = removeNoUseKey(txt).trim();
        
        int lp = txt.indexOf(Constants.LEFT_PARENTHESE);
        int rp = txt.indexOf(Constants.RIGHT_PARENTHESE);
        String args = txt.substring(lp + 1, rp);
        txt = txt.substring(0, lp).trim();
        index = txt.lastIndexOf(Constants.BLANK);
        String name = txt.substring(index + 1).trim();
        String type = txt.substring(0, index).trim();
        
        index = findNextRightBrace(s, end);
        String methodBody = s.substring(end + 1, index);
        
        methodInfo.setArgs(args);
        methodInfo.setFinal(isFinal);
        methodInfo.setStatic(isStatic);
        methodInfo.setMethodBody(methodBody);
        methodInfo.setName(name);
        methodInfo.setNote(notes);
        methodInfo.setPe(pe);
        methodInfo.setType(type);
        
        return methodInfo;
    }

    private static int findNextRightBrace(String s, int end)
    {
        int index = s.indexOf(Constants.RIGHT_BRACE, end + 1);
        int num = 1;
        while (index != -1)
        {
            if (isVisable(index, s))
            {
                num--;
            }

            int temp = s.lastIndexOf(Constants.LEFT_BRACE, index - 1);;
            while (temp  > end)
            {
                if (isVisable(temp, s))
                {
                    num++;
                }
                temp = s.lastIndexOf(Constants.LEFT_BRACE, temp - 1);
            }
            
            if (num == 0)
            {
                break;
            }
            end = index;
            index = s.indexOf(Constants.RIGHT_BRACE, end + 1);
        }
        
        if (index == -1)
        {
            throw new RuntimeException("can not find }");
        }
        
        return index;
    }
    
//    private static void printlnCurrentLine(String s, int index)
//    {
//        int start = s.lastIndexOf(Constants.ENTER, index);
//        int end = s.indexOf(Constants.ENTER, index);
//        String temp = s.substring(start + 1, end);
//        System.out.println(temp);
//    }
    
    private static boolean isVisable(int index, String s)
    {
        int start = s.lastIndexOf(Constants.ENTER, index);
        int end = s.indexOf(Constants.ENTER, index);
        String temp = s.substring(start + 1, end);
        index = index - start - 1;
        if (isInStr(temp, index) || isInNote(temp, index))
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * 
     * @param s
     * @param c
     */
    private static void parseClassVarInfo(String s, ClassInfo c)
    {
        Pattern p = Pattern.compile(VAR_REGEX);
        Matcher m = p.matcher(s);
        while (m.find())
        {
            parseTxt2VarInfo(m, s, c);
        }
    }

    /**
     * 没有时间做任何重构
     * 
     * 让人疯掉的代码
     * 
     * 总之，这是用于解析java文件中的成员变量的
     * @param m
     * @param s
     * @param c
     */
    private static void parseTxt2VarInfo(Matcher m, String s, ClassInfo c)
    {
        String txt = m.group();
        int start = m.start();
        int end = m.end();
        
        //当前匹配的字符是否是注释
        int preEnter = s.lastIndexOf(Constants.ENTER, start);
        if (isInNote(s.substring(preEnter, start), start - preEnter))
        {
            return;
        }
        
        txt = txt.substring(0, txt.length() - 1);
        //向前搜索注释
        NotesInfo notes = getNotesInfo(s, start);
        notes.setGap(1);
        
        int index = txt.indexOf(Constants.BLANK);
        PackageEnum pe = PackageEnum.getPackageEnum(txt.substring(0, index));
        txt = txt.substring(index);
        
        boolean isStatic = isStatic(txt);
        boolean isFinal = isFinal(txt);
        txt = removeNoUseKey(txt);
        
        //再找下一个空格，空格前的字符串肯定就是类型了
        txt = txt.trim();
        index = txt.indexOf(Constants.BLANK);
        String type = txt.substring(0, index);
        txt = txt.substring(index).trim();
        while (txt.startsWith(Constants.LEFT_BRACKET))
        {
            txt = txt.substring(1).trim();
            if (txt.startsWith(Constants.RIGHT_BRACKET))
            {
                type = type + Constants.LEFT_BRACKET + Constants.RIGHT_BRACKET;
                txt = txt.substring(1).trim();
            }
            else
            {
                throw new RuntimeException("");
            }
        }
        
        
        //暂不考虑字符串里面的“,”
        index = txt.indexOf(Constants.COMMA);
        String temp = null;
        while (index != -1)
        {
            int indexOfLB = txt.indexOf(Constants.LEFT_BRACE, index);
            if (indexOfLB == -1)
            {
                indexOfLB = txt.length();
            }
            int indexOfRB = txt.indexOf(Constants.RIGHT_BRACE, index);
            if (indexOfRB == -1)
            {
                indexOfRB = txt.length();
            }
            boolean isNotArray = indexOfRB >= indexOfLB;
            if (!isNotArray)
            {
                index = txt.indexOf(Constants.COMMA, index + 1);
                continue;
            }
            
            temp = txt.substring(0, index);
            temp = removeNotes(temp);
            int indexEqual = temp.indexOf(Constants.EQUAL);
            if (indexEqual == -1)
            {
                indexEqual = temp.length();
            }
            String name = temp.substring(0, indexEqual).trim();
            while (name.endsWith(Constants.RIGHT_BRACKET))
            {
                name = name.substring(0, name.length() - 1).trim();
                if (name.endsWith(Constants.LEFT_BRACKET))
                {
                    type = type + Constants.LEFT_BRACKET + Constants.RIGHT_BRACKET;
                    name = name.substring(0, name.length() - 1).trim();
                }
                else
                {
                    throw new RuntimeException("");
                }
            }
            
            String value = temp.substring(indexEqual);
            if (value.length() > 0)
            {
                value = value.substring(Constants.EQUAL.length());
            }
            VarInfo varInfo = new VarInfo();
            varInfo.setFinal(isFinal);
            varInfo.setName(removeNotes(name));
            varInfo.setPe(pe);
            varInfo.setStatic(isStatic);
            varInfo.setValue(removeNotes(value));
            varInfo.setType(type);
            NotesInfo varNotes = notes.clone();
            //向后搜索注释
            int pre = index + 1;
            getMyselfNoteOfCurrentLine(txt, pre, varNotes);
            txt = txt.substring(index + 1).trim();
            varInfo.setNote(varNotes);
            c.addVarInfo(varInfo);
            
            index = txt.indexOf(Constants.COMMA);
        }
        
        int indexEqual = txt.indexOf(Constants.EQUAL);
        if (indexEqual == -1)
        {
            indexEqual = txt.length();
        }
        String name = txt.substring(0, indexEqual);
        while (name.endsWith(Constants.RIGHT_BRACKET))
        {
            name = name.substring(0, name.length() - 1).trim();
            if (name.endsWith(Constants.LEFT_BRACKET))
            {
                type = type + Constants.LEFT_BRACKET + Constants.RIGHT_BRACKET;
                name = name.substring(0, name.length() - 1).trim();
            }
            else
            {
                throw new RuntimeException("");
            }
        }
        
        String value = txt.substring(indexEqual).trim();
        if (value.length() > 0)
        {
            value = value.substring(Constants.EQUAL.length());
        }
        
        VarInfo varInfo = new VarInfo();
        varInfo.setFinal(isFinal);
        varInfo.setName(removeNotes(name));
        varInfo.setPe(pe);
        varInfo.setStatic(isStatic);
        varInfo.setValue(removeNotes(value));
        varInfo.setType(type);
        
        //向后搜索注释
        getMyselfNoteOfCurrentLine(s, end ,notes);
        varInfo.setNote(notes);
        c.addVarInfo(varInfo);
    }
    
    /**
     * 只查找当前行 //结尾的注释
     * @param s
     * @param index
     * @return
     */
    private static void getMyselfNoteOfCurrentLine(String s, int index, NotesInfo notes)
    {
        int suf = s.indexOf(Constants.ENTER, index);
        if (suf == -1)
        {
            return;
        }
        
        String temp = s.substring(index, suf).trim();
        if ((index =temp.indexOf("//")) != -1)
        {
            notes.addNote(temp.substring(index + 2).trim()); 
        }
    }

    private static boolean isFinal(String txt)
    {
        boolean isFinal = false;
        if (txt.contains(Constants.FINAL))
        {
            isFinal = true;
        }
        return isFinal;
    }

    private static boolean isStatic(String txt)
    {
        boolean isStatic = false;
        if (txt.contains(Constants.STATIC))
        {
            isStatic = true;
        }
        return isStatic;
    }

    private static String removeNoUseKey(String txt)
    {
        txt = txt.replaceAll("volatile", Constants.EMPTY);
        txt = txt.replaceAll("transient", Constants.EMPTY);
        txt = txt.replaceAll("synchronized", Constants.EMPTY);
        txt = txt.replaceAll(Constants.STATIC, Constants.EMPTY);
        txt = txt.replaceAll(Constants.FINAL, Constants.EMPTY);
        return txt;
    }
    
    /**
     * 从index前搜索注释
     * @param s
     * @param index
     * @return
     */
    private static NotesInfo getNotesInfo(String s, int fromIndex)
    {
        NotesInfo notes = new NotesInfo(0);
        int index = s.lastIndexOf(Constants.ENTER, fromIndex);
        while (fromIndex != 0)
        {
            String temp = s.substring(index + 1, fromIndex).trim();
            if (temp.startsWith("//"))
            {
                notes.addNoteFromFront(getNoteInfo(temp));
            }
            else if (temp.startsWith("/*"))
            {
                int indexTemp = temp.indexOf("*/");
                if (indexTemp == -1) indexTemp = temp.length();
                notes.addNoteFromFront(getNoteInfo(temp.substring(0, indexTemp)));
            }
            else if (temp.startsWith("@"))
            {
                notes.addNoteFromFront(getNoteInfo(temp));
            }
            else if (temp.startsWith("*"))
            {
                notes.addNoteFromFront(getNoteInfo(temp));
            }
            else if (temp.trim().isEmpty())
            {
                //继续
            }
            else
            {
                break;
            }
            
            //结束，寻找上一句语句
            fromIndex = index;
            index = s.lastIndexOf(Constants.ENTER, fromIndex - 1);
            if (index == -1)
            {
                index = 0;
            }
        }
        
        return notes;
    }
    
    /**
     * 从index前搜索注释
     * @param s
     * @param index
     * @return
     */
    private static String removeNotes(String s)
    {
        StringBuilder sb = new StringBuilder();
        String[] arrays = s.split(Constants.ENTER);
        int index = -1;
        for (String array : arrays)
        {
            array = array.trim();
            //未做循环去注释，一般一行出现多段注释的可能性太低，形如： "/*我是注释1*/ int i = 0; //我是注释2"
            //此类情况不作处理
            if (array.startsWith("/") || array.startsWith("*") || array.startsWith("@"))
            {
            }
            else if ((index = array.indexOf("//")) != -1 && !isInStr(array, index))
            {
                array = array.substring(0, index);
                sb.append(array);
            }
            else if ((index = array.indexOf("/*")) != -1 && !isInStr(array, index))
            {
                if (array.indexOf("*/") != -1)
                {
                    array = array.substring(0, index) + array.substring(array.indexOf("*/") + 2);
                }
                else
                {
                    array = array.substring(0, index);
                }
                
                sb.append(array);
            }
            else
            {
                sb.append(array);
            }
        }
        
        String trim = sb.toString().trim();
        trim = trim.replaceAll("\r", Constants.EMPTY);
        trim = trim.replaceAll("\t", Constants.EMPTY);
        while (trim.contains("  "))
        {
            trim = trim.replaceAll("  ", Constants.EMPTY);
        }
        return trim;
    }
    
    private static boolean isInStr(String s, int index)
    {
        int num = 0;
        index = s.lastIndexOf(Constants.QUOTE, index);
        while (index != -1)
        {
            if (index < 1 || s.charAt(index - 1) != '\\')
            {
                num++;
            }
            index = s.lastIndexOf(Constants.QUOTE, index - 1);
        }
        
        if (num % 2 == 1)
        {
            return true;
        }
        
        return false;
    }
    

    private static boolean isInNote(String s, int index)
    {
        int temp = s.lastIndexOf("//", index - 1);
        if (temp != -1 && !isInStr(s, temp))
        {
            return true;
        }
        
        temp = s.lastIndexOf("/*", index - 1);
        if (temp != -1 && !isInStr(s, temp) && s.lastIndexOf("*/", index - 1) < temp + 2)
        {
            return true;
        }
        
        if (s.startsWith(Constants.STAR) || s.startsWith("/"))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * 
     * @param s 
     * @return
     */
    private static String getNoteInfo(String s)
    {
        s = s.trim();
        while (s.length() > 0 && (s.charAt(0) == '*' || s.charAt(0) == '/'))
        {
            s = s.substring(1).trim();
        }
        while (s.length() > 0 && (s.charAt(s.length() - 1) == '*' || s.charAt(s.length() - 1) == '/'))
        {
            s = s.substring(0, s.length() - 1).trim();
        }
        
        return s.trim();
    }
    
    /**
     * 是否是注释
     * @param s
     * @return
     */
    private static boolean isNote(String s)
    {
        if (s.startsWith("*") || s.startsWith("//") || s.startsWith("@") || s.startsWith("/*"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private static void parseClassNameInfo(String s, ClassInfo c)
    {
        int end = s.indexOf(Constants.LEFT_BRACE);
        c.setName(s.substring(Constants.PUBLIC_CLASS.length() + 1, end).trim());
    }

    private static void parseImportAndPackageInfo(String s, ClassInfo c)
    {
        //解析类的package和import信息
        StringTokenizer st = new StringTokenizer(s, Constants.ENTER);
        NotesInfo notes = new NotesInfo(0);
        while (st.hasMoreTokens())
        {
            String t = st.nextToken().trim();
            if (t.startsWith(Constants.PACKAGE))
            {
                int end = t.indexOf(Constants.SEMICOLON);
                c.setPackageInfo(t.substring(Constants.PACKAGE.length() + 1, end).trim());
            }
            else if (t.startsWith(Constants.IMPORT))
            {
                int end = t.indexOf(Constants.SEMICOLON);
                c.addImportInfo(t.substring(Constants.IMPORT.length() + 1, end).trim());
            }
            else if (isNote(t))
            {
                notes.addNote(getNoteInfo(t));
            }
        }
        c.setNotes(notes);
    }
}
