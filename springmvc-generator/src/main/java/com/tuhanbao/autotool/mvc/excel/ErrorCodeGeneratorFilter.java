package com.tuhanbao.autotool.mvc.excel;

import java.util.List;

import com.tuhanbao.autotool.mvc.SpringMvcProjectInfo;
import com.tuhanbao.base.chain.Context;
import com.tuhanbao.base.chain.FilterAnnotation;
import com.tuhanbao.base.chain.event.CreateFileEvent;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.ClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.xlsParseUtil.fixConfig.Xls2ErrorCode;
import com.tuhanbao.base.util.objutil.FileUtil;

@FilterAnnotation("errorCode")
public class ErrorCodeGeneratorFilter extends ExcelAGCFilter {

    @Override
    public void filter(Context context) {
        String[][] arrays = removeConfig(context, ERROR_CODE);
        Xls2ErrorCode xls2ErrorCode = new Xls2ErrorCode();
        
        SpringMvcProjectInfo project = getProject(context);
        
        List<ClassInfo> classInfos = xls2ErrorCode.getClassInfos(project, arrays);
        for (ClassInfo ci : classInfos) {
            context.addEvent(new CreateFileEvent(ci, project));
        }
        
        String url = FileUtil.appendPath(project.getRootPath(), project.getConfigUrl(),
                "errorCode.properties");
        context.addEvent(new CreateFileEvent(url, xls2ErrorCode.getProperties(arrays)));
    }

}
