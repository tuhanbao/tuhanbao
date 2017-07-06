package com.tuhanbao.base.util.io.codeGenarator.tableUtil;

import com.tuhanbao.base.util.objutil.StringUtil;

/**
 * 在生成代码时如果源文件存在的处理策略
 * 
 * @author Administrator
 *
 */
public enum Relation {
	/**
	 * 一对一
	 */
	One2One, 
	/**
	 * 一对多
	 */
	One2N, 
	/**
	 * 多对1
	 */
	N2One, 
	/**
	 * 不做任何处理
	 */
	N2N;
	
	private static String ONE2ONESTR = "1:1";
	private static String ONE2NSTR = "1:n";
	@SuppressWarnings("unused")
	private static String N2ONESTR = "n:1";
	private static String N2NSTR = "n:n";
	
	public static Relation getRelation(String s) {
		//默认一对多
		if (StringUtil.isEmpty(s)) return N2One;
		s = s.trim();
		if (ONE2ONESTR.equalsIgnoreCase(s)) return One2One;
		if (ONE2NSTR.equalsIgnoreCase(s)) return One2N;
		if (N2NSTR.equalsIgnoreCase(s)) return N2N;
		return N2One;
	}
}
