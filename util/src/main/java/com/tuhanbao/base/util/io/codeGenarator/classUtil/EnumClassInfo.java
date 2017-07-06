package com.tuhanbao.base.util.io.codeGenarator.classUtil;

import com.tuhanbao.base.util.io.codeGenarator.ProjectInfo;



public class EnumClassInfo extends ClassInfo implements IEnumType
{
    private int type;
    
    public static final int INT = 0, STRING = 1;

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

	@Override
	public String getFullClassName(ProjectInfo projectInfo) {
		return projectInfo.getConstantsUrl() + ".enums." + this.getClassName();
	}
}
