package com.tuhanbao.base.util.io.codeGenarator;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.objutil.FileUtil;
import com.tuhanbao.base.util.objutil.StringUtil;

public class ProjectInfo4MemCache extends ProjectInfo
{
	private static final String PROJECT_HEAD = "com.hhbao.";
	
    public ProjectInfo4MemCache(String srcPath, String projectName) {
    	super(srcPath, srcPath, projectName);
    }
    
    public String getServiceBeanPath()
    {
        return StringUtil.appendStr(".", PROJECT_HEAD, "service.cms.bean");
    }
    
    public String getFullServiceBeanUrl()
    {
    	return FileUtil.appendPath(srcPath, (getServiceBeanPath()).replace(".", Constants.FILE_SEP));
    }
    
    public String getBaseBeanPath()
    {
        return StringUtil.appendStr(".", PROJECT_HEAD, "base.cms.bean");
    }
    
    public String getFullBaseBeanUrl()
    {
    	return FileUtil.appendPath(srcPath, (getBaseBeanPath()).replace(".", Constants.FILE_SEP));
    }
    
    public String getSqlUrl()
    {
        return FileUtil.appendPath(srcPath, "init/sql/init.sql");
    }

	@Override
	public String getConstantsUrl() {
		return StringUtil.appendStr(".", PROJECT_HEAD, "base.constants");
	}

	@Override
	public String getEnumUrl() {
		return StringUtil.appendStr(".", getConstantsUrl(), "enums");
	}
}
