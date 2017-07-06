package com.tuhanbao.base.util.io.codeGenarator.xlsParseUtil.fixConfig;

import java.util.ArrayList;
import java.util.List;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.io.codeGenarator.ProjectInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.ClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.NotesInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.PackageEnum;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.VarInfo;
import com.tuhanbao.base.util.io.codeGenarator.codeUtil.Xls2CodeUtil;
import com.tuhanbao.base.util.io.codeGenarator.xlsParseUtil.Xls2Code;
import com.tuhanbao.base.util.objutil.StringUtil;

/**
 * @author tuhanbao
 *
 */
public class Xls2ErrorCode implements Xls2Code
{
	@Override
	public List<ClassInfo> getClassInfos(ProjectInfo project, String[][] arrays) {
		List<ClassInfo> classInfos = new ArrayList<ClassInfo>();
    	ClassInfo clazzInfo = new ClassInfo();
    	clazzInfo.setPackageInfo(project.getConstantsUrl());
    	clazzInfo.addImportInfo(BaseErrorCode.class);
    	clazzInfo.setName("ErrorCode extends BaseErrorCode");
    	clazzInfo.setFinal(true);
    	
//    	StringBuilder staticStr = new StringBuilder();
        for (String[] array : arrays)
        {
            if (Xls2CodeUtil.isEmptyLine(array)) continue;
            
            String name = Xls2CodeUtil.getString(array, 0);
            String value = Xls2CodeUtil.getString(array, 1);
            String desc = Xls2CodeUtil.getString(array, 2);
            if (name == null || value == null) continue;
            
            name = name.toUpperCase();
            
            //小于1000的为公共错误，不需要生成
            if (Integer.valueOf(value) < 1000) continue;
            VarInfo varInfo = new VarInfo();
            varInfo.setName(name);
            varInfo.setValue(value);
            NotesInfo note = new NotesInfo(1);
            note.addNote(desc);
            varInfo.setNote(note);
            varInfo.setFinal(true);
            varInfo.setStatic(true);
            varInfo.setPe(PackageEnum.PUBLIC);
            varInfo.setType("int");
            clazzInfo.addVarInfo(varInfo);
            
//            staticStr.append(Xls2CodeUtil.GAP2).append("ErrorCodeMsgManager.put(").append(name).append(", \"")
//            	.append(desc).append("\");").append(Constants.ENTER);
        }
        
//        if (staticStr.length() > 0) staticStr = staticStr.deleteCharAt(staticStr.length() - 1);
//        clazzInfo.setStaticStr(staticStr.toString());
        
        classInfos.add(clazzInfo);
        return classInfos;
	}
	
	public String getProperties(String[][] arrays) {
		StringBuilder sb = new StringBuilder();
        for (String[] array : arrays) {
            if (Xls2CodeUtil.isEmptyLine(array)) continue;
            
            String name = Xls2CodeUtil.getString(array, 0);
            String value = Xls2CodeUtil.getString(array, 1);
            String desc = Xls2CodeUtil.getString(array, 2);
            if (StringUtil.isEmpty(name) || StringUtil.isEmpty(value) || StringUtil.isEmpty(desc)) continue;
            
            //小于1000的为公共错误，不需要生成
            sb.append(value).append(Constants.EQUAL).append(desc).append(Constants.ENTER);
            
        }
        return sb.toString();
	}
	
}
