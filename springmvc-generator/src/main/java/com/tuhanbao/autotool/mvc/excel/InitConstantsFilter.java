package com.tuhanbao.autotool.mvc.excel;

import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.base.chain.Context;
import com.tuhanbao.base.chain.FilterAnnotation;
import com.tuhanbao.base.util.io.codeGenarator.codeUtil.Xls2CodeUtil;
import com.tuhanbao.base.util.objutil.ArrayUtil;
import com.tuhanbao.base.util.objutil.StringUtil;

@FilterAnnotation("init")
public class InitConstantsFilter extends ExcelAGCFilter {
    @Override
    public void filter(Context context) {
        removeConfig(context, README);
        String[][] arrays = removeConfig(context, TABLES);
        int length = arrays.length;
        
        Map<String, String[]> tableConfigs = new HashMap<String, String[]>();
        for (int i = 1; i < length; i++) {
            if (Xls2CodeUtil.isEmptyLine(arrays[i])) continue;
            String tableName = ArrayUtil.indexOf(arrays[i], 0);
            if (!StringUtil.isEmpty(tableName)) {
                tableConfigs.put(tableName, arrays[i]);
            }
        }
        context.putAttr(TABLE_CONFIG, tableConfigs);
    }

}
