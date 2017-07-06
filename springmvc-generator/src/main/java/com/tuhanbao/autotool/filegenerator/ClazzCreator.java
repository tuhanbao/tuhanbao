package com.tuhanbao.autotool.filegenerator;

import java.util.ArrayList;
import java.util.List;

import com.tuhanbao.base.chain.IEvent;
import com.tuhanbao.base.chain.event.CreateFileEvent;
import com.tuhanbao.base.util.io.codeGenarator.ProjectInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.ClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;
import com.tuhanbao.base.util.objutil.OverwriteStrategy;
import com.tuhanbao.base.util.objutil.StringUtil;

public abstract class ClazzCreator implements IFileGenerator {
	
	protected ProjectInfo project;
	
	protected OverwriteStrategy os;
	
	public ClazzCreator(ProjectInfo project) {
		this(project, OverwriteStrategy.COVER);
	}

	public ClazzCreator(ProjectInfo project, OverwriteStrategy os) {
	    this.project = project;
	    this.os= os;
	}
	
	public List<IEvent> getFileBean(Object args) {
	    ClassInfo clazz = toClazz(args);
	    CreateFileEvent fb = new CreateFileEvent(clazz, project, os);
	    ArrayList<IEvent> list = new ArrayList<>();
        list.add(fb);
        return list;
	}
    
	public abstract ClassInfo toClazz(Object args);
	
    private static String str2ClassName(String s)
    {
        if (s == null) return s;
        s = s.toLowerCase();
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    
	public static String getClassName(String name) {
		String[] names = StringUtil.string2Array(name, "_");
		//将names转换成驼峰
		StringBuilder sb = new StringBuilder();
		for (String n : names) {
			sb.append(str2ClassName(n));
		}
		return sb.toString();
	}
	
	/**
	 * 将下划线式改成驼峰式
	 * 
	 * @param name
	 * @return
	 */
	public static String getVarName(String name) {
		String[] names = StringUtil.string2Array(name, "_");
		//将names转换成驼峰
		StringBuilder sb = new StringBuilder();
		for (String n : names) {
			if (!StringUtil.isEmpty(n))
				sb.append(ClazzCreator.str2ClassName(n));
		}
		String s = sb.toString();
		return s.substring(0, 1).toLowerCase() + s.substring(1);
	}
	
	/**
	 * 对于过长的字段名，获取其简写
	 * 如SimplePersonName ，可以简写为SPN
	 * 
	 * @param name
	 * @return
	 */
	public static String getSimpleVarName(String name) {
		String[] names = StringUtil.string2Array(name, "_");
		//将names转换成驼峰
		StringBuilder sb = new StringBuilder();
		for (String n : names) {
			if (!StringUtil.isEmpty(n))
			 sb.append(n.substring(0, 1).toUpperCase());
		}
		return sb.toString();
	}
	
    public static String firstCharUpper(String s)
    {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
	
	/**
	 * 获取数据类型的引用包
	 * 
	 * @param dataType
	 * @return
	 */
	public static String getImportFullPath(DataType dataType) {
		if (dataType == DataType.BIGDEECIMAL) return "java.math.BigDecimal";
        else if (dataType == DataType.DATE) return "java.util.Date";
		//在java.lang下不需要引用
//        else return "java.lang." + dataType.getBigName();
        else return null;
	}
}
