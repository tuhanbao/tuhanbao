package com.tuhanbao.base.util.io.codeGenarator.codeUtil;

import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.base.util.io.codeGenarator.CodeType;

public class CodeUtilManager
{
    private static final Map<CodeType, Xls2CodeUtil> UTILS = new HashMap<CodeType, Xls2CodeUtil>();
    
    //默认只注册java的
    static
    {
        register(CodeType.JAVA, Xls2JavaUtil.instance);
    }
    
    public static void register(CodeType codeType, Xls2CodeUtil util)
    {
        UTILS.put(codeType, util);
    }
    
    public static Xls2CodeUtil getCodeUtil(CodeType type)
    {
        return UTILS.get(type);
    }
}
