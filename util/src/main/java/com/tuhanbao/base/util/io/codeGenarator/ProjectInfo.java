package com.tuhanbao.base.util.io.codeGenarator;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.objutil.StringUtil;

public abstract class ProjectInfo
{
	//代码根路径（定位到src路径下）
    protected String srcPath;
    
    //项目名称
    protected String projectName;
    
    //项目路径
    protected String rootPath;

    public ProjectInfo(String rootPath, String srcPath, String projectName) {
        this.rootPath = rootPath;
    	this.srcPath = srcPath;
    	this.projectName = projectName;
    }
    
    public String getRootPath() {
        return rootPath;
    }

    public String getSrcPath() {
		return srcPath;
	}

	public String getProjectName() {
		return projectName;
	}

	/**
     * 返回常量类的java path
     * @return
     */
	public abstract String getConstantsUrl();

    public String getConstantsPath() {
        return getPath(srcPath, getConstantsUrl());
    }
	/**
	 * 返回枚举类的java path
	 * @return
	 */
	public abstract String getEnumUrl();
	
	/**
	 * 返回枚举类的全路径
	 * @return
	 */
	public String getEnumPath() {
	    return getPath(srcPath, getEnumUrl());
	}
	
	/**
     * 文件路径
     * @param paths
     * @return
     */
	public static String getPath(String ... paths) {
	    int length = paths.length;
        for (int i = 0; i < length; i++) {
            if (StringUtil.isEmpty(paths[i])) continue;
            paths[i] = paths[i].replace(Constants.STOP_EN, Constants.FILE_SEP);
        }
        return StringUtil.appendStr(Constants.FILE_SEP, paths);
	}
	
	/**
	 * java包引用时需要用的路径，文件夹之间用.隔开
	 * @param paths
	 * @return
	 */
	public static String getUrl(String ... paths) {
	    int length = paths.length;
	    for (int i = 0; i < length; i++) {
	        if (StringUtil.isEmpty(paths[i])) continue;
	        paths[i] = paths[i].replace(Constants.FILE_SEP, Constants.STOP_EN);
	    }
	    return StringUtil.appendStr(Constants.STOP_EN, paths);
	}
}
