package com.tuhanbao.base.util.io.codeGenarator.classUtil;

import com.tuhanbao.base.util.io.codeGenarator.codeUtil.Xls2CodeUtil;

/**
 * 不考虑内部类
 * @author tuhanbao
 *
 */
public class EnumVarInfo extends VarInfo
{
    public EnumVarInfo(String[] array)
    {
        super();
        name = array[0].trim();
        value = Xls2CodeUtil.getString(array, 1);
        this.setNote(Xls2CodeUtil.getNotesInfo(Xls2CodeUtil.getString(array, 2), 1));
    }
}
