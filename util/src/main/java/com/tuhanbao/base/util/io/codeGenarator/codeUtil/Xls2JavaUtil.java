package com.tuhanbao.base.util.io.codeGenarator.codeUtil;

import java.util.List;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.ClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.EnumClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.EnumVarInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.MethodInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.NotesInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.VarInfo;
import com.tuhanbao.base.util.objutil.StringUtil;

public class Xls2JavaUtil extends Xls2CodeUtil
{
    public static final Xls2JavaUtil instance = new Xls2JavaUtil();
    
    private Xls2JavaUtil()
    {
    }
    
    
    @Override
    public String getEnumStr(EnumClassInfo enumInfo)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("package " + enumInfo.getPackageInfo() + ";").append(Constants.ENTER).append(Constants.ENTER);
        String className = str2ClassName(enumInfo.getName());
        sb.append("public enum ").append(className).append(Constants.BLANK);
        sb.append(Constants.LEFT_BRACE).append(Constants.ENTER);
        
        int type = EnumClassInfo.INT;
        String typeStr = "int";
        List<VarInfo> varInfos = enumInfo.getVarInfos();
        for (int i = 0; i < varInfos.size() - 1; i++)
        {
            EnumVarInfo varInfo = (EnumVarInfo)varInfos.get(i);
            String value = varInfo.getValue();
            if (!StringUtil.isEmpty(value) && !value.matches("\\d*")) {
            	type = EnumClassInfo.STRING;
            	typeStr = "String";
            }
        }
        enumInfo.setType(type);
        
        boolean isString = EnumClassInfo.STRING == type;
        for (int i = 0; i < varInfos.size() - 1; i++)
        {
            EnumVarInfo varInfo = (EnumVarInfo)varInfos.get(i);
			sb.append(getEnumVarInfoStr(varInfo, isString)).append(Constants.COMMA).append(Constants.ENTER).append(Constants.ENTER);
        }
        EnumVarInfo varInfo = (EnumVarInfo)varInfos.get(varInfos.size() - 1);
        sb.append(getEnumVarInfoStr(varInfo, isString)).append(Constants.SEMICOLON).append(Constants.ENTER).append(Constants.ENTER);
        
        sb.append(Constants.GAP1).append("public final ");
        sb.append(typeStr);
        sb.append(" value;").append(Constants.ENTER).append(Constants.ENTER);
        
        sb.append(Constants.GAP1).append("private ").append(className).append(Constants.LEFT_PARENTHESE);
        sb.append(typeStr);
        sb.append(Constants.BLANK).append("value").append(Constants.RIGHT_PARENTHESE);
        sb.append(Constants.BLANK).append(Constants.LEFT_BRACE).append(Constants.ENTER);
        sb.append(Constants.GAP2).append("this.value = value;").append(Constants.ENTER);
        sb.append(Constants.GAP1).append(Constants.RIGHT_BRACE).append(Constants.ENTER).append(Constants.ENTER);
        
        sb.append(Constants.GAP1).append("public static ").append(className).append(" get").append(className).append(Constants.LEFT_PARENTHESE);
        sb.append(typeStr);
        sb.append(Constants.BLANK).append("value").append(Constants.RIGHT_PARENTHESE);
        sb.append(Constants.BLANK).append(Constants.LEFT_BRACE).append(Constants.ENTER);
        sb.append(Constants.GAP2).append("for (").append(className).append(" temp : ").append(className).append(".values())");
        sb.append(Constants.BLANK).append(Constants.LEFT_BRACE).append(Constants.ENTER);
        
        if (type == EnumClassInfo.STRING) sb.append(Constants.GAP3).append("if (temp.value.equals(value))");
        else sb.append(Constants.GAP3).append("if (temp.value == value)");
        
        sb.append(Constants.BLANK).append(Constants.LEFT_BRACE).append(Constants.ENTER);
        sb.append(Constants.GAP4).append("return temp;").append(Constants.ENTER);
        sb.append(Constants.GAP3).append(Constants.RIGHT_BRACE).append(Constants.ENTER);
        sb.append(Constants.GAP2).append(Constants.RIGHT_BRACE).append(Constants.ENTER).append(Constants.ENTER);
        sb.append(Constants.GAP2).append("return null;").append(Constants.ENTER);
        sb.append(Constants.GAP1).append(Constants.RIGHT_BRACE).append(Constants.ENTER).append(Constants.ENTER);
        sb.append(Constants.RIGHT_BRACE);
        return sb.toString();
    }
    
    /**
     * 吧一个变量信息转换成java代码
     */
    public String getVarInfoStr(VarInfo var)
    {
        StringBuilder sb = new StringBuilder();
        if (var.getNote() != null)
        {
            sb.append(getNoteStr(var.getNote()));
        }
        
        if (var.getAnnotations() != null) {
        	for (String annotation : var.getAnnotations()) {
        		sb.append(Constants.TAB_SPACE).append(annotation).append(Constants.ENTER);	
        	}
        }
        
        sb.append(Constants.TAB_SPACE);
        if (var.getPe() != null)
        {
            sb.append(var.getPe().name().toLowerCase()).append(Constants.BLANK);
        }
        
        if (var.isStatic())
        {
            sb.append(Constants.STATIC).append(Constants.BLANK);
        }
        
        if (var.isFinal())
        {
            sb.append(Constants.FINAL).append(Constants.BLANK);
        }
        
        sb.append(var.getType()).append(Constants.BLANK);
        sb.append(var.getName());
        
        if (var.getValue() != null && !var.getValue().trim().isEmpty())
        {
            sb.append(Constants.BLANK).append(Constants.EQUAL).append(Constants.BLANK);
            sb.append(var.getValue());
        }
        sb.append(Constants.SEMICOLON);
        
        return sb.toString();
    }

    /**
     * 吧一个枚举变量信息转换成java代码
     */
    public String getEnumVarInfoStr(EnumVarInfo var, boolean isString)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getNoteStr(var.getNote()));
        sb.append(Constants.GAP1).append(var.getName().toUpperCase()).append(Constants.LEFT_PARENTHESE);
        if (isString) sb.append("\"" + var.getValue() + "\"").append(Constants.RIGHT_PARENTHESE);
        else sb.append(var.getValue()).append(Constants.RIGHT_PARENTHESE);
        return sb.toString();
    }
    
    /**
     * 将代码路径转换为包路径
     * @param url
     * @return
     */
    @Override
    public String getClassSrcUrl(ClassInfo classInfo)
    {
        return classInfo.getPackageInfo().replaceAll("\\.", "/") + "/" + classInfo.getClassName() + ".java";
    }
    
    @Override
    public String getNoteStr(NotesInfo note)
    {
        StringBuilder sb = new StringBuilder();
        
        if (note == null || note.getList() == null)
        {
            return Constants.EMPTY;
        }
        String prefix = Xls2CodeUtil.getGap(note.getGap());
        sb.append(prefix + Constants.START).append(Constants.ENTER);
        for (String s : note.getList())
        {
            sb.append(prefix + Constants.MID).append(s).append(Constants.ENTER);
        }
        sb.append(prefix + Constants.END).append(Constants.ENTER);
        
        return sb.toString();
    }
    

    @Override
    public String getClassStr(ClassInfo classInfo)
    {
    	if (classInfo instanceof EnumClassInfo) return getEnumStr((EnumClassInfo) classInfo);
    	
    	StringBuilder sb = new StringBuilder();
        
        if (classInfo.getPackageInfo() != null) {
        	sb.append(Constants.PACKAGE).append(Constants.BLANK).append(classInfo.getPackageInfo()).append(Constants.SEMICOLON).append(Constants.ENTER).append(Constants.ENTER);
        }
        
        for (String importInfo : classInfo.getImportInfo())
        {
            sb.append(Constants.IMPORT).append(Constants.BLANK).append(importInfo).append(Constants.SEMICOLON).append(Constants.ENTER);
        }
        
        sb.append(Constants.ENTER);
        NotesInfo notes = classInfo.getNotes();
        if (notes != null)
        {
            sb.append(notes).append(Constants.ENTER);
        }
        
        List<String> annotations = classInfo.getAnnotations();
        if (annotations != null) {
        	for (String annotation : annotations) {
        		sb.append(annotation).append(Constants.ENTER);	
        	}
        }
        
        sb.append(classInfo.getPe().name().toLowerCase()).append(Constants.BLANK);
        if (classInfo.isAbstract()) sb.append(Constants.ABSTRACT).append(Constants.BLANK);
        if (classInfo.isStatic()) sb.append(Constants.STATIC).append(Constants.BLANK);
        if (classInfo.isFinal()) sb.append(Constants.FINAL).append(Constants.BLANK);
        if (classInfo.isInterface()) sb.append(Constants.INTERFACE).append(Constants.BLANK);
        else sb.append("class").append(Constants.BLANK);
        
        sb.append(classInfo.getName()).append(Constants.BLANK);
        sb.append(Constants.LEFT_BRACE).append(Constants.ENTER);
        
        for (VarInfo var : classInfo.getVarInfos())
        {
            sb.append(var).append(Constants.ENTER).append(Constants.ENTER);
        }
        
        if (!StringUtil.isEmpty(classInfo.getStaticStr())) {
        	sb.append(Constants.GAP1).append(Constants.STATIC).append(Constants.BLANK).append(Constants.LEFT_BRACE).append(Constants.ENTER);
        	sb.append(classInfo.getStaticStr()).append(Constants.ENTER);
        	sb.append(Constants.GAP1).append(Constants.RIGHT_BRACE).append(Constants.ENTER).append(Constants.ENTER);
        }
        
        for (MethodInfo method : classInfo.getMethodInfos())
        {
            sb.append(method).append(Constants.ENTER);
        }
        
        
        List<ClassInfo> innerClasses = classInfo.getInnerClass();
		if (innerClasses != null) {
	        for (ClassInfo innerClass : innerClasses) {
	        	sb.append(addGap2InnerClass(innerClass.toString())).append(Constants.ENTER).append(Constants.ENTER);
	        }
        }
        
        sb.append(Constants.RIGHT_BRACE);
        return sb.toString();
    }
    
    private static String addGap2InnerClass(String innerClass) {
    	String[] arrays = StringUtil.string2Array(innerClass, Constants.ENTER);
    	int length = arrays.length;
    	for (int i = 0; i < length; i++) {
    		if (!StringUtil.isEmpty(arrays[i])) arrays[i] = Constants.TAB_SPACE + arrays[i];
    	}
    	return StringUtil.array2String(arrays, Constants.ENTER);
    }
}
