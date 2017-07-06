package com.tuhanbao.autotool.mvc.excel;

import com.tuhanbao.autotool.filegenerator.IFileGenerator;
import com.tuhanbao.autotool.filegenerator.j2ee.IServiceClazzCreator;
import com.tuhanbao.autotool.filegenerator.j2ee.MOClazzCreator;
import com.tuhanbao.autotool.filegenerator.j2ee.ModelClazzCreator;
import com.tuhanbao.autotool.filegenerator.j2ee.ServiceClazzCreator;
import com.tuhanbao.autotool.mvc.ProjectConfig;
import com.tuhanbao.autotool.mvc.SpringMvcProjectInfo;
import com.tuhanbao.base.chain.Context;
import com.tuhanbao.base.chain.FilterAnnotation;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportTable;

@FilterAnnotation("table")
public class TableGeneratorFilter extends ExcelAGCFilter {

    @Override
    public void filter(Context context) {
        //生成mo, bean, service, iService, controller代码
        if (ProjectConfig.NEED_CREATE_CODE) {
            SpringMvcProjectInfo project = getProject(context);
            IFileGenerator serviceFG = new ServiceClazzCreator(project);
            IFileGenerator iServiceFG = new IServiceClazzCreator(project);
            IFileGenerator modelFG = new ModelClazzCreator(project);
            IFileGenerator MOFG = new MOClazzCreator(project);
            
            for (ImportTable table : getTables(context)) {
                context.addEvent(serviceFG.getFileBean(table));
                context.addEvent(iServiceFG.getFileBean(table));
                context.addEvent(modelFG.getFileBean(table));
                context.addEvent(MOFG.getFileBean(table));
            }
        }
    }

}
