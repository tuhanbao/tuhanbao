package com.tuhanbao.base.util.io.codeGenarator.xlsParseUtil.fixConfig;

import java.util.ArrayList;
import java.util.List;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.io.codeGenarator.ProjectInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.ClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.EnumClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.EnumVarInfo;
import com.tuhanbao.base.util.io.codeGenarator.codeUtil.Xls2CodeUtil;
import com.tuhanbao.base.util.io.codeGenarator.xlsParseUtil.Xls2Code;
import com.tuhanbao.base.util.objutil.StringUtil;

public class Xls2Enum implements Xls2Code
{
	private StringBuilder properties = new StringBuilder();
	
    @Override
    public List<ClassInfo> getClassInfos(ProjectInfo project, String[][] arrays)
    {
        return getEnums(project, arrays);
    }
    
//    /**
//     * 默认是写java的服务端
//     * @param clazzInfo
//     * @throws IOException 
//     */
//    protected void writeServer(String url, ClassInfo clazzInfo) throws IOException
//    {
//        TxtUtil.write(url + serverCodeUtil.getClassSrcUrl(clazzInfo), serverCodeUtil.Enum2Java(clazzInfo));
//    }
    
    /**
     * 获取枚举
     * @return
     */
    protected List<ClassInfo> getEnums(ProjectInfo project, String[][] arrays)
    {
        EnumClassInfo clazzInfo = null;
        
        List<ClassInfo> list = new ArrayList<ClassInfo>();
        for (String[] array : arrays)
        {
            if (isEmptyEnumLine(array))
            {
                if (clazzInfo != null) list.add(clazzInfo);
                clazzInfo = null;
                continue;
            }
            
            if (clazzInfo == null)
            {
                clazzInfo = createNewEnum(project.getEnumUrl(), array);
            }
            else
            {
                EnumVarInfo varInfo = new EnumVarInfo(array);
				clazzInfo.addVarInfo(varInfo);
				String note = Xls2CodeUtil.getString(array, 2);
				if (!StringUtil.isEmpty(note)) {
					properties.append(clazzInfo.getName().toUpperCase()).append("_")
                		.append(varInfo.getName().toUpperCase()).append(Constants.EQUAL).append(note).append(Constants.ENTER);
				}
            }
        }
        
        if (clazzInfo != null) list.add(clazzInfo);
        
        return list;
    }
    
	public String getProperties() {
		return properties.toString();
	}

    protected EnumClassInfo createNewEnum(String packageName, String[] array)
    {
    	EnumClassInfo clazzInfo = new EnumClassInfo();
        clazzInfo.setName(array[0]);
        clazzInfo.setPackageInfo(packageName);
        return clazzInfo;
    }
    
    private boolean isEmptyEnumLine(String[] array)
    {
        boolean isEmptyLine = Xls2CodeUtil.isEmptyLine(array);
        if (!isEmptyLine)
        {
            if (array != null && array.length > 0 && (array[0] == null || array[0].isEmpty()))
            {
                isEmptyLine = true;
            }
        }
        return isEmptyLine;
    }

}
