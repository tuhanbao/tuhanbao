package com.tuhanbao.autotool.filegenerator.j2ee;

import com.tuhanbao.autotool.mvc.J2EETable;
import com.tuhanbao.autotool.mvc.SpringMvcProjectInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.ClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.MethodInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.PackageEnum;
import com.tuhanbao.base.util.objutil.OverwriteStrategy;

public class ModelClazzCreator extends J2EETableClazzCreator {
    private static final String MOCLASS_SUFFIX = "MO";
	
	public ModelClazzCreator(SpringMvcProjectInfo project) {
		super(project, OverwriteStrategy.NEVER_COVER);
	}

	public ClassInfo table2Class(J2EETable table) {
		ClassInfo classInfo = new ClassInfo();
        String modelName = table.getModelName();
        String moClassName = modelName + MOCLASS_SUFFIX;
        classInfo.setName(modelName + " extends " + moClassName);
        classInfo.setPackageInfo(this.project.getServiceBeanUrl(table.getModule()));
        
        //不需要import，在同一个包下面
//        classInfo.addImportInfo(this.project.getServiceBeanUrl(table.getModule()) + "." + moClassName);
        MethodInfo method = new MethodInfo();
        method.setName(modelName);
        method.setPe(PackageEnum.PUBLIC);
        classInfo.addMethodInfo(method);
        
        return classInfo;
	}
}
