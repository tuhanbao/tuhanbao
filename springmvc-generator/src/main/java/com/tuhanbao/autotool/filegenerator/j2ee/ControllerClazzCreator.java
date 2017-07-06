package com.tuhanbao.autotool.filegenerator.j2ee;

import com.tuhanbao.Constants;
import com.tuhanbao.autotool.mvc.J2EETable;
import com.tuhanbao.autotool.mvc.ProjectConfig;
import com.tuhanbao.autotool.mvc.SpringMvcProjectInfo;
import com.tuhanbao.base.util.clazz.ClazzUtil;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.ClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.MethodInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.PackageEnum;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.VarInfo;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn;
import com.tuhanbao.base.util.objutil.OverwriteStrategy;


public class ControllerClazzCreator extends J2EETableClazzCreator {

	public ControllerClazzCreator(SpringMvcProjectInfo project) {
		super(project, OverwriteStrategy.NEVER_COVER);
	}

	@Override
	public ClassInfo table2Class(J2EETable table) {
		ClassInfo clazz = new ClassInfo();
		clazz.setName(table.getControllerName());
		clazz.setPackageInfo(project.getControllerUrl(table.getModule()));
		clazz.addImportInfo("org.springframework.stereotype.Controller");
		clazz.addImportInfo("org.springframework.web.bind.annotation.RequestMapping");
		clazz.addImportInfo("org.springframework.web.bind.annotation.ResponseBody");
		clazz.addImportInfo("org.springframework.beans.factory.annotation.Autowired");
		clazz.addImportInfo(project.getIServiceUrl(table.getModule()) + Constants.STOP_EN + table.getIServiceName());
		clazz.addImportInfo(project.getServiceBeanUrl(table.getModule()) + Constants.STOP_EN + table.getModelName());
		clazz.addAnnotation("@Controller");
		String requestMap = "@RequestMapping(\""+ ClazzUtil.firstCharLowerCase(table.getModelName()) + "\")";
		clazz.addAnnotation(requestMap);
		
		VarInfo varInfo = new VarInfo();
		varInfo.addAnnotation("@Autowired");
		varInfo.setType(table.getIServiceName());
		String serviceVarName = ClazzUtil.firstCharLowerCase(table.getIServiceName().substring(1));
		varInfo.setName(serviceVarName);
		varInfo.setPe(PackageEnum.PRIVATE);
		clazz.addVarInfo(varInfo);
		
		MethodInfo add = createAddMethod(serviceVarName, table.getModelName());
		ImportColumn pk = table.getPK();
		if (pk != null) {
			MethodInfo delete = createDeleteMethod(serviceVarName, table.getModelName(), pk);
			clazz.addMethodInfo(delete);
		}
		MethodInfo search = createListMethod(serviceVarName, table.getModelName());
		MethodInfo update = createUpdateMethod(serviceVarName, table.getModelName());
		clazz.addMethodInfo(add);
		clazz.addMethodInfo(search);
		clazz.addMethodInfo(update);

		if (ProjectConfig.NEED_CREATE_WEB) {
			MethodInfo index = createIndexMethod(ClazzUtil.firstCharLowerCase(table.getModelName()));
			clazz.addMethodInfo(index);
		}
		return clazz;
	}
	
	private MethodInfo createIndexMethod(String modelName){
		MethodInfo method = new MethodInfo();
		String annotation = "@RequestMapping(\"index\")";
		method.addAnnotation(annotation);
		method.setPe(PackageEnum.PUBLIC);
		method.setType("Object");
		method.setName("index");
		method.setMethodBody("return \"" + modelName + "\";");
		return method;
	}
	
	private MethodInfo createAddMethod(String serviceName , String modelName){
		MethodInfo method = new MethodInfo();
		String annotation = "@RequestMapping(\"add\")";
		method.addAnnotation(annotation);
		method.setPe(PackageEnum.PUBLIC);
		method.addAnnotation("@ResponseBody");
		method.setType("Object");
		method.setName("add");
		method.setArgs(modelName +" " + ClazzUtil.getVarName(modelName));
		StringBuilder methodBody = new StringBuilder();
		methodBody.append( serviceName +".add(" + ClazzUtil.getVarName(modelName) + ");").append(Constants.ENTER);
		methodBody.append(Constants.GAP2).append("return null;").append(Constants.ENTER);
		method.setMethodBody(methodBody.toString());
		return method;
	}
	
	private MethodInfo createDeleteMethod(String serviceName , String modelName, ImportColumn pk){
		MethodInfo method = new MethodInfo();
		String annotation = "@RequestMapping(\"delete\")";
		method.addAnnotation(annotation);
		method.setPe(PackageEnum.PUBLIC);
		method.addAnnotation("@ResponseBody");
		method.setType("Object");
		method.setName("delete");
		method.setArgs(pk.getDataType().getName() + " id");
		StringBuilder methodBody = new StringBuilder();
		methodBody.append("return " + serviceName +".deleteById(id);").append(Constants.ENTER);
		method.setMethodBody(methodBody.toString());
		return method;
	}

	private MethodInfo createUpdateMethod(String serviceName , String modelName){
		MethodInfo method = new MethodInfo();
		String annotation = "@RequestMapping(\"edit\")";
		method.addAnnotation(annotation);
		method.setPe(PackageEnum.PUBLIC);
		method.addAnnotation("@ResponseBody");
		method.setType("Object");
		method.setName("edit");
		method.setArgs(modelName +" " + ClazzUtil.getVarName(modelName));
		StringBuilder methodBody = new StringBuilder();
		methodBody.append("return " + serviceName +".update(" + ClazzUtil.getVarName(modelName) + ");").append(Constants.ENTER);
		method.setMethodBody(methodBody.toString());
		return method;
	}
	
	private MethodInfo createListMethod(String serviceName , String modelName){
		MethodInfo method = new MethodInfo();
		String annotation = "@RequestMapping(\"list\")";
		method.addAnnotation(annotation);
		method.setPe(PackageEnum.PUBLIC);
		method.addAnnotation("@ResponseBody");
		method.setType("Object");
		method.setName("list");
		StringBuilder methodBody = new StringBuilder();
		methodBody.append("return ").append(serviceName +".select(null);").append(Constants.ENTER);
		method.setMethodBody(methodBody.toString());
		return method;
	}

}
