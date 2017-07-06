package com.tuhanbao.autotool.mvc.excel;

import java.util.List;

import com.tuhanbao.Constants;
import com.tuhanbao.autotool.mvc.SpringMvcProjectInfo;
import com.tuhanbao.base.chain.Context;
import com.tuhanbao.base.chain.FilterAnnotation;
import com.tuhanbao.base.chain.event.CreateFileEvent;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.ClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.EnumClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.EnumManager;
import com.tuhanbao.base.util.io.codeGenarator.xlsParseUtil.fixConfig.Xls2Enum;
import com.tuhanbao.base.util.objutil.FileUtil;

@FilterAnnotation("enum")
public class EnumGeneratorFilter extends ExcelAGCFilter {

    @Override
    public void filter(Context context) {
        String[][] arrays = removeConfig(context, TYPES);
        Xls2Enum xls2Enum = new Xls2Enum();
        SpringMvcProjectInfo project = getProject(context);
        List<ClassInfo> classInfos = xls2Enum.getClassInfos(getProject(context), arrays);;
        for (ClassInfo ci : classInfos) {
            EnumManager.register(ci.getName(), (EnumClassInfo)ci);
            context.addEvent(new CreateFileEvent(ci, project));
        }
        
        //枚举还需要写语言资源
        String url = FileUtil.appendPath(project.getRootPath(), project.getConfigUrl(),
                Constants.LANGUAGE_PROPERTIES);
        //必须保证枚举是第一个写语言资源的，因为写策略为覆盖
        context.addEvent(new CreateFileEvent(url, xls2Enum.getProperties()));
    }

}
