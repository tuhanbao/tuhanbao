package com.tuhanbao.base.util.io.codeGenarator.classUtil;

import com.tuhanbao.base.util.io.codeGenarator.ProjectInfo;

public class EnumType implements IEnumType {
	private String name;
	
	private String fullName;
	
	private int type;
	
	public EnumType(String name, String fullName, int type) {
		this.type = type;
		this.fullName = fullName;
		this.name = name;
	}
	
	@Override
	public String getClassName() {
		return name;
	}

	@Override
	public String getFullClassName(ProjectInfo projectInfo) {
		return fullName;
	}

	@Override
	public int getType() {
		return type;
	}

}
