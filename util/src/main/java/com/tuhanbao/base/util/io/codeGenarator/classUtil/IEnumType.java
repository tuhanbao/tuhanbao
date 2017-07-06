package com.tuhanbao.base.util.io.codeGenarator.classUtil;

import com.tuhanbao.base.util.io.codeGenarator.ProjectInfo;

public interface IEnumType {

	int getType();

	String getFullClassName(ProjectInfo projectInfo);

	String getClassName();

}
