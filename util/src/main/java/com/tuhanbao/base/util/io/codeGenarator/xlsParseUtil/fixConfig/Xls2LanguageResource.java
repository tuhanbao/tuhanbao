package com.tuhanbao.base.util.io.codeGenarator.xlsParseUtil.fixConfig;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.io.codeGenarator.ProjectInfo;
import com.tuhanbao.base.util.io.codeGenarator.codeUtil.Xls2CodeUtil;

/**
 * @author tuhanbao
 *
 */
/**
 * @author tuhanbao
 *
 */
/**
 * @author tuhanbao
 *
 */
public class Xls2LanguageResource extends Xls2Config
{
    public static final String ERRORCODE_URL = "com/threetorch/service/base/constant/ErrorCode.java";
    
    
    public String[] getConfigStrAndCode(ProjectInfo project, String[][] arrays) {
    	return getLanguageResource(project, arrays);
    }
    
    private String[] getLanguageResource(ProjectInfo project, String[][] arrays)
    {
        StringBuilder sb = new StringBuilder();
        StringBuilder configStr = new StringBuilder();
        
        sb.append("package " + project.getConstantsUrl() + ";").append(Constants.ENTER);
        sb.append(Constants.ENTER);
        sb.append("public class LanguageResource").append(Constants.BLANK);
        sb.append(Constants.LEFT_BRACE).append(Constants.ENTER);
        
        for (String[] array : arrays)
        {
            if (Xls2CodeUtil.isEmptyLine(array)) continue;
            
            String name = Xls2CodeUtil.getString(array, 0);
            String value = Xls2CodeUtil.getString(array, 1);

            if (name == null || value == null) continue;
            
            name = name.toUpperCase();
			sb.append(Constants.GAP1).append("public static final String ").append(name).append(" = \"")
                .append(name).append("\";").append(Constants.ENTER).append(Constants.ENTER);
			configStr.append(name).append(Constants.EQUAL).append(value).append(Constants.ENTER);
        }
        sb.append(Constants.RIGHT_BRACE);
        
        return new String[]{sb.toString(), configStr.toString()};
    }
}
