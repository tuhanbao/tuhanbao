package com.tuhanbao.web.filter;

import java.util.ArrayList;
import java.util.List;

public class DiyFilterUtil {
    
    /**
     * 获取所有可以过滤的字段
     * @param filterType
     * @return
     */
    public static List<WebFilterItem> getItems(MyBatisSelector selector) {
        return getItems(selector, null);
    }
    
    /**
     * 获取所有可以过滤的字段
     * @param filterType
     * @return
     */
    public static List<WebFilterItem> getItems(MyBatisSelector selector, ISpecialFilterItemHandler handler) {
        if (selector == null) return null;
        
        List<WebFilterItem> list = new ArrayList<WebFilterItem>();
        for (SelectTable selectTable : selector.getAllTables().values()) {
            for (AsColumn col : selectTable.getColumns()) {
                if (col.isCanFilter()) {
                    WebFilterItem item = new WebFilterItem(col);
                    if (handler != null) handler.handle(item, col);
                    list.add(item);
                }
            }
        }
        return list;
    }
}