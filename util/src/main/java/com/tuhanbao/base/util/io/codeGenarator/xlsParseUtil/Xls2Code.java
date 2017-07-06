package com.tuhanbao.base.util.io.codeGenarator.xlsParseUtil;

import java.io.IOException;
import java.util.List;

import com.tuhanbao.base.util.io.codeGenarator.ProjectInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.ClassInfo;

public interface Xls2Code
{
    public List<ClassInfo> getClassInfos(ProjectInfo project, String[][] arrays) throws IOException;
}
