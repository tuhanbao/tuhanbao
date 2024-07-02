package com.td.ca.web.util.mq;

import com.td.ca.base.util.db.table.Table;
import com.td.ca.web.util.spring.SpringUtil;

public class MQUtil {

    public static String getTopic(Table table) {
        return getTopic(table.getAliasName(), SpringUtil.getServiceName());
    }

    public static String getTopic(String tableName, String serviceName) {
        return "topic-" + serviceName + "-" + tableName + "-msg";
    }
}
