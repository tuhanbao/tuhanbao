package com.tuhanbao.autotool.filegenerator.j2ee;

import com.tuhanbao.Constants;
import com.tuhanbao.autotool.filegenerator.ClazzCreator;
import com.tuhanbao.autotool.mvc.J2EETable;
import com.tuhanbao.autotool.mvc.SpringMvcProjectInfo;
import com.tuhanbao.base.dataservice.CTServiceBean;
import com.tuhanbao.base.dataservice.ICTBean;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.base.util.clazz.ClazzUtil;
import com.tuhanbao.base.util.db.table.data.BooleanValue;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.ClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.IEnumType;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.MethodInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.PackageEnum;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.Relation;
import com.tuhanbao.base.util.objutil.StringUtil;

public class MOClazzCreator extends J2EETableClazzCreator {
    private static final String MO_CLASS = MetaObject.class.getName();
    private static final String SERVICE_BEAN_CLASS = ServiceBean.class.getName();
    private static final String CT_SERVICE_BEAN_CLASS = CTServiceBean.class.getName();
    private static final String MOCLASS_SUFFIX = "MO";
    
	public MOClazzCreator(SpringMvcProjectInfo project) {
		super(project);
	}

	public ClassInfo table2Class(J2EETable table) {
		ClassInfo classInfo = new ClassInfo();
        String modelName = table.getModelName();
        String tableName = table.getTableName();
        String className = modelName + MOCLASS_SUFFIX;
        
        if (table.isCTTable()) {
        	classInfo.setName(className + " extends CTServiceBean");
        	classInfo.addImportInfo(ICTBean.class);
        	classInfo.addImportInfo(CT_SERVICE_BEAN_CLASS);
        }
        else {
        	classInfo.setName(className + " extends ServiceBean");
        }
        classInfo.setPackageInfo(this.project.getServiceBeanUrl(table.getModule()));
        classInfo.addImportInfo(SERVICE_BEAN_CLASS);
        classInfo.addImportInfo(this.project.getConstantsUrl() + ".TableConstants");
        classInfo.addImportInfo(MO_CLASS);
        
        MethodInfo method = new MethodInfo();
        method.setName(className);
        method.setPe(PackageEnum.PROTECTED);
        if (table.isCTTable()) {
        	method.setArgs("ICTBean ctBean");
        	method.setMethodBody("this(ctBean, new MetaObject(TableConstants." + tableName + ".TABLE));");
        }
        else {
        	method.setMethodBody("this(new MetaObject(TableConstants." + tableName + ".TABLE));");
        }
        classInfo.addMethodInfo(method);
        
        method = new MethodInfo();
        
        if (table.isCTTable()) {
        	method.setArgs("ICTBean ctBean, MetaObject mo");
        	method.setMethodBody("super(ctBean, mo);");
        }
        else {
        	method.setArgs("MetaObject mo");
        	method.setMethodBody("super(mo);");
        }
        method.setName(className);
        method.setPe(PackageEnum.PROTECTED);
        classInfo.addMethodInfo(method);
        
        boolean hasByteBuffer = false;

        for (ImportColumn col : table.getColumns())
        {
    		String importInfo = ClazzCreator.getImportFullPath(col.getDataType());
    		if (importInfo != null) classInfo.addImportInfo(importInfo);
        	
            String returnMethodType = col.getDataType().getDIYValue();
            classInfo.addImportInfo("com.tuhanbao.base.util.db.table.data." + returnMethodType);
            
            IEnumType enumInfo = col.getEnumInfo();
            if (enumInfo != null) {
            	String enumClassInfo = enumInfo.getFullClassName(this.project);
            	classInfo.addImportInfo(enumClassInfo);
            }
            method = new MethodInfo();
			method.setName(getGetName(col));
            method.setPe(PackageEnum.PUBLIC);
            method.setType(getColReturnType(col));
            StringBuilder methodBody = new StringBuilder();
            String methodType = col.getDataType().getDIYValue();
            methodBody.append(methodType).append(" value = (").append(methodType).append(")getValue(TableConstants.").append(tableName).append(".").append(col.getName()).append(");").append(Constants.ENTER);
            methodBody.append(Constants.GAP2).append("if (value == null) return ").append(getDefaultValue(col, classInfo)).append(";").append(Constants.ENTER);
            if (enumInfo == null) methodBody.append(Constants.GAP2).append("else return value.getValue();");
            else {
            	String enumName = enumInfo.getClassName();
            	methodBody.append(Constants.GAP2).append("else return ").append(enumName).append(".get").append(enumName).append("(value.getValue());");
            }
            method.setMethodBody(methodBody.toString());
            classInfo.addMethodInfo(method);
            
            method = new MethodInfo();
            method.setName(getSetName(col));
            method.setPe(PackageEnum.PUBLIC);
            method.setType(Constants.VOID);
            method.setArgs(getColArgType(col) + " value");
            if (enumInfo == null) method.setMethodBody("setValue(TableConstants." + tableName + "." + col.getName() + ", " + methodType + ".valueOf(value));");
            else method.setMethodBody("setValue(TableConstants." + tableName + "." + col.getName() + ", " + methodType + ".valueOf(value.value));");
            classInfo.addMethodInfo(method);
            
            if (col.getFkColumn() != null)
            {
                ImportColumn fk = col.getFkColumn();
                String fkTableName = fk.getTable().getName();
        		if (fkTableName.startsWith("T_") || fkTableName.startsWith("I_")) {
        			fkTableName = fkTableName.substring(2);
        		}
                String fkModelName = ClazzCreator.getClassName(fkTableName);
                
                String name = getColClassName(col);
                
                Relation fkRT = col.getFkRT();
					
                boolean isSingle = fkRT == Relation.One2One || fkRT == Relation.N2One;
				classInfo.addMethodInfo(getGetMethod(col, fkModelName, name, classInfo, isSingle, false));
                classInfo.addMethodInfo(getRemoveMethod(col, fkModelName, name, classInfo, isSingle, false));
                classInfo.addMethodInfo(getSetMethod(col, fkModelName, name, classInfo, isSingle, false));
                
                classInfo.addImportInfo("java.util.List");
            }
            
            if (col.getDataType() == DataType.BYTEARRAY) hasByteBuffer = true;
        }
        
        for (ImportColumn col : table.getFKColumns()) {
            String fkTableName = col.getTable().getName();
    		if (fkTableName.startsWith("T_") || fkTableName.startsWith("I_")) {
    			fkTableName = fkTableName.substring(2);
    		}
            String fkModelName = ClazzCreator.getClassName(fkTableName);
            
            Relation fkRT = col.getFkRT();
            boolean isFkMySelf = col.getTable() == table;
            boolean isSingle = fkRT == Relation.One2One || fkRT == Relation.One2N;
			classInfo.addMethodInfo(getGetMethod(col, fkModelName, fkModelName, classInfo, isSingle, isFkMySelf));
            classInfo.addMethodInfo(getRemoveMethod(col, fkModelName, fkModelName, classInfo, isSingle, isFkMySelf));
            classInfo.addMethodInfo(getSetMethod(col, fkModelName, fkModelName, classInfo, isSingle, isFkMySelf));
            classInfo.addImportInfo("java.util.List");
        }
        
        if (hasByteBuffer)
        {
            classInfo.addImportInfo("com.tuhanbao.util.ByteBuffer");
        }
        
        return classInfo;
	}
	
	private static String getDefaultValue(ImportColumn col, ClassInfo classInfo) {
	    String defaultValue = col.getDefaultValue();
	    DataType dt = col.getDataType();
	    IEnumType iet = col.getEnumInfo();
	    //获取默认值
	    if (StringUtil.isEmpty(defaultValue)) {
	        if (iet != null) return "null";
	        if (dt == DataType.BOOLEAN) return "false";
	        if (dt == DataType.LONG || dt == DataType.FLOAT || dt == DataType.DOUBLE
	                || DataType.INT.getName().equals(dt.getName())) return "0";
	        if (dt == DataType.BIGDEECIMAL) return "BigDecimal.ZERO";
	        return "null";
	    }
	    else {
	        if (iet != null) {
	            String enumName = iet.getClassName();
                return enumName + ".get" + enumName + "(" + defaultValue + ")";
	        }
	        if (dt == DataType.BOOLEAN) return BooleanValue.valueOf(defaultValue).toString();
            if (dt == DataType.BIGDEECIMAL) return "new BigDecimal(" + defaultValue + ")";
            if (dt == DataType.LONG) {
                defaultValue = defaultValue.toUpperCase();
                if (!defaultValue.endsWith("L")) defaultValue += "L";
                return defaultValue;
            }
            if (DataType.STRING.getName().equals(dt.getName())) return "\"" + defaultValue + "\"";
            if (dt == DataType.DATE) {
                classInfo.addImportInfo("com.tuhanbao.io.objutil.TimeUtil");
                return "new Date(TimeUtil.getTime(\"" + defaultValue + "\"))";
            }
            return defaultValue;
	    }
	}

	private MethodInfo getGetMethod(ImportColumn col, String fkModelName, String name, ClassInfo classInfo, boolean isSingle, boolean fkIsMine) {
		MethodInfo method = new MethodInfo();
		String fkMySelfStr = fkIsMine ? ", true" : Constants.EMPTY;
		
		String methodName = "get" + name + (isSingle ? "" : "s");
		if (classInfo.constainsMethodName(methodName)) {
			methodName += "By" + getColClassName(col);
		}
		method.setName(methodName);
		method.setPe(PackageEnum.PUBLIC);
		StringBuilder methodBody = new StringBuilder();
		methodBody.append("List<? extends ServiceBean> result = this.getFKBean(TableConstants.");
		methodBody.append(col.getTable().name).append(".").append(col.getName()).append(fkMySelfStr + ");").append(Constants.ENTER);
		methodBody.append(Constants.GAP2);
		if (isSingle) {
			method.setType(fkModelName);
			methodBody.append("return result == null || result.isEmpty() ? null : (" + fkModelName + ")result.get(0);");
		}
		else {
			method.addAnnotation("@SuppressWarnings(\"unchecked\")");
			method.setType("List<" + fkModelName + ">");
			methodBody.append("return (List<" + fkModelName + ">)result;");
		}
		method.setMethodBody(methodBody.toString());
		return method;
	}
	
	private MethodInfo getRemoveMethod(ImportColumn col, String fkModelName, String name, ClassInfo classInfo, boolean isSingle, boolean fkIsMine) {
		MethodInfo method = new MethodInfo();
		String fkMySelfStr = fkIsMine ? ", true" : Constants.EMPTY;
		String methodName = "remove" + name + (isSingle ? "" : "s");
		if (classInfo.constainsMethodName(methodName)) {
			methodName += "By" + getColClassName(col);
		}
		method.setName(methodName);
		method.setPe(PackageEnum.PUBLIC);
		StringBuilder methodBody = new StringBuilder();
		methodBody.append("List<? extends ServiceBean> result = this.removeFKBean(TableConstants.");
		methodBody.append(col.getTable().name).append(".").append(col.getName()).append(fkMySelfStr + ");").append(Constants.ENTER);
		methodBody.append(Constants.GAP2);
		if (isSingle) {
			method.setType(fkModelName);
			methodBody.append("return result == null || result.isEmpty() ? null : (" + fkModelName + ")result.get(0);");
		}
		else {
			method.addAnnotation("@SuppressWarnings(\"unchecked\")");
			method.setType("List<" + fkModelName + ">");
			methodBody.append("return (List<" + fkModelName + ">)result;");
		}
		method.setMethodBody(methodBody.toString());
		return method;
	}
	
	private MethodInfo getSetMethod(ImportColumn col, String fkModelName, String name, ClassInfo classInfo, boolean isSingle, boolean fkIsMine) {
		MethodInfo method = new MethodInfo();
		String fkMySelfStr = fkIsMine ? ", true" : Constants.EMPTY;
		String methodName = "set" + name + (isSingle ? "" : "s");
		if (classInfo.constainsMethodName(methodName)) {
			methodName += "By" + getColClassName(col);
		}
		method.setName(methodName);
		method.setPe(PackageEnum.PUBLIC);
		method.setArgs(fkModelName + " value");
		method.setType("void");
		StringBuilder methodBody = new StringBuilder();
		if (isSingle) {
			method.setArgs(fkModelName + " value");
		}
		else {
			method.setArgs("List<" + fkModelName + ">" + " value");
		}
		
		methodBody.append("this.setFKBean(TableConstants." + col.getTable().name + "." + col.getName()
				+ ", value").append(fkMySelfStr + ");");
		method.setMethodBody(methodBody.toString());
		return method;
	}

	private String getGetName(ImportColumn col) {
		String className = ClazzUtil.getClassName(col.getName());
		if (col.getDataType() == DataType.BOOLEAN) {
			if (className.startsWith("Is")) {
				return ClazzUtil.firstCharLowerCase(className);
			}
		}
		return "get" + ClazzUtil.getClassName(col.getName());
	}

	private String getSetName(ImportColumn col) {
		String className = ClazzUtil.getClassName(col.getName());
//		if (col.getDataType() == DataType.BOOLEAN) {
//			if (className.startsWith("Is")) {
//				className = className.substring(2);
//			}
//		}
		return "set" + className;
	}

	private String getColClassName(ImportColumn col) {
		String name = col.getName().toLowerCase();
		//外键命名必须规范
		if (name.endsWith("_id")) name = name.substring(0, name.length() - 3);
		name = ClazzUtil.getClassName(name);
		return name;
	}
	
	private static String getColReturnType(ImportColumn col) {
		if (col.getEnumInfo() != null) {
			return col.getEnumInfo().getClassName();
		}
		return col.getDataType().getName();
	}

	private static String getColArgType(ImportColumn col) {
		if (col.getEnumInfo() != null) {
			return col.getEnumInfo().getClassName();
		}
		return col.getDataType().getBigName();
	}
}
