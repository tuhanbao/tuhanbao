package com.tuhanbao.autotool.filegenerator.j2ee;

import com.tuhanbao.Constants;
import com.tuhanbao.autotool.mvc.SpringMvcProjectInfo;
import com.tuhanbao.base.util.clazz.ClazzUtil;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.ClassInfo;
import com.tuhanbao.base.util.objutil.OverwriteStrategy;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.autotool.mvc.J2EETable;

public class ServiceClazzCreator extends J2EETableClazzCreator {
	public ServiceClazzCreator(SpringMvcProjectInfo project) {
		super(project, OverwriteStrategy.NEVER_COVER);
	}

	public ClassInfo table2Class(J2EETable table) {
		ClassInfo clazz = new ClassInfo();
		String modelName = table.getModelName();
		clazz.setName(table.getServiceName() + " extends ServiceImpl<" + modelName + "> implements " + table.getIServiceName());
		String module = table.getModule();
		clazz.setPackageInfo(project.getServiceUrl(module));
		clazz.addImportInfo("org.springframework.stereotype.Service");
		clazz.addImportInfo("org.springframework.transaction.annotation.Transactional");
		clazz.addImportInfo(project.getServiceBeanUrl(module) + Constants.STOP_EN + modelName);
		clazz.addImportInfo(project.getIServiceUrl(module) + Constants.STOP_EN + table.getIServiceName());
		
		clazz.addAnnotation("@Service(\"" + ClazzUtil.firstCharLowerCase(modelName) + "Service\")");
		clazz.addAnnotation("@Transactional" + getTransactionManagerName(module));
		return clazz;
	}
	
	private static String getTransactionManagerName(String module) {
		if (StringUtil.isEmpty(module)) {
			return Constants.EMPTY;
		}
		String s = "TransactionManager";
		return "(\"" + module + s + "\")";
	}
}
