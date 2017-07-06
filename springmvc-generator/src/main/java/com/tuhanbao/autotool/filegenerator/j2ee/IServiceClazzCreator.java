package com.tuhanbao.autotool.filegenerator.j2ee;

import com.tuhanbao.Constants;
import com.tuhanbao.autotool.mvc.SpringMvcProjectInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.ClassInfo;
import com.tuhanbao.base.util.objutil.OverwriteStrategy;
import com.tuhanbao.autotool.mvc.J2EETable;

public class IServiceClazzCreator extends J2EETableClazzCreator {
	public IServiceClazzCreator(SpringMvcProjectInfo project) {
		super(project, OverwriteStrategy.NEVER_COVER);
	}

	public ClassInfo table2Class(J2EETable table) {
		ClassInfo clazz = new ClassInfo();
		clazz.setInterface(true);
		String modelName = table.getModelName();
		clazz.setName(table.getIServiceName() + " extends IService<" + modelName + ">");
		clazz.setPackageInfo(project.getIServiceUrl(table.getModule()));
		String modelPath = project.getServiceBeanUrl(table.getModule());
		clazz.addImportInfo(modelPath + Constants.STOP_EN + modelName);
		clazz.addImportInfo(Constants.ISERVICE_NAME);
		return clazz;
	}
}
