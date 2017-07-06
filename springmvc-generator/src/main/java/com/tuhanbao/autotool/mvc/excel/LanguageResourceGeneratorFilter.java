package com.tuhanbao.autotool.mvc.excel;

import com.tuhanbao.Constants;
import com.tuhanbao.autotool.mvc.SpringMvcProjectInfo;
import com.tuhanbao.base.chain.Context;
import com.tuhanbao.base.chain.FilterAnnotation;
import com.tuhanbao.base.chain.event.CreateFileEvent;
import com.tuhanbao.base.util.io.codeGenarator.xlsParseUtil.fixConfig.Xls2LanguageResource;
import com.tuhanbao.base.util.objutil.FileUtil;
import com.tuhanbao.base.util.objutil.OverwriteStrategy;

@FilterAnnotation("language")
public class LanguageResourceGeneratorFilter extends ExcelAGCFilter {

    @Override
    public void filter(Context context) {
        String[][] arrays = removeConfig(context, LANGUAGE);
        SpringMvcProjectInfo project = getProject(context);
        //语言资源
        String[] strs = new Xls2LanguageResource().getConfigStrAndCode(project, arrays);
        String url = FileUtil.appendPath(project.getConstantsPath(),
                Constants.LANGUAGE_CLASS);
        context.addEvent(new CreateFileEvent(url, strs[0]));
        url = FileUtil.appendPath(project.getRootPath(), project.getConfigUrl(),
                Constants.LANGUAGE_PROPERTIES);
        context.addEvent(new CreateFileEvent(url, strs[1], OverwriteStrategy.ADD));
    }

}
