package com.tuhanbao.base.util.io.codeGenarator.xlsParseUtil.fixConfig;

import java.io.IOException;

import com.tuhanbao.base.util.io.codeGenarator.CodeType;
import com.tuhanbao.base.util.io.codeGenarator.ProjectInfo;
import com.tuhanbao.base.util.io.codeGenarator.codeUtil.CodeUtilManager;

public class Xls2Config
{
    public String getConfigStr(ProjectInfo project, String[][] arrays) throws IOException
    {
        return CodeUtilManager.getCodeUtil(CodeType.JAVA).xls2Config(arrays);
    }
}
