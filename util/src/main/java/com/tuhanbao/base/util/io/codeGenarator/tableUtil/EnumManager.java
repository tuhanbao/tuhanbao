package com.tuhanbao.base.util.io.codeGenarator.tableUtil;

import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.base.dataservice.filter.LogicType;
import com.tuhanbao.base.dataservice.filter.operator.Operator;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.EnumClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.EnumType;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.IEnumType;

public class EnumManager {

	private static final Map<String, IEnumType> ENUMS = new HashMap<String, IEnumType>();
	
	static {
		//框架包中有一些公共枚举，在配置数据库字段枚举类型时，可以选择这些公共枚举
		register("FlowStatus", new EnumType("FlowStatus", "com.tuhanbao.thirdapi.pay.FlowStatus", EnumClassInfo.INT));
		register("PayPlatform", new EnumType("PayPlatform", "com.tuhanbao.thirdapi.pay.PayPlatform", EnumClassInfo.INT));
		register("Operator", new EnumType("Operator", Operator.class.getName(), EnumClassInfo.INT));
		register("LogicType", new EnumType("LogicType", LogicType.class.getName(), EnumClassInfo.INT));
	}
	
	public static void register(String key, IEnumType enumInfo) {
		ENUMS.put(key, enumInfo);
	}
	
	public static IEnumType getEnum(String key) {
		return ENUMS.get(key);
	}
}
