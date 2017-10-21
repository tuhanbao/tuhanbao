package com.tuhanbao.api.crm.constants;

import java.util.HashMap;
import java.util.Map;
import com.tuhanbao.base.util.clazz.ClazzUtil;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.Relation;
import com.tuhanbao.base.util.db.table.CacheType;
import com.tuhanbao.base.util.db.table.Column;
import com.tuhanbao.base.util.db.table.ColumnFactory;
import com.tuhanbao.base.util.db.table.Table;

/**
 * 本来为自动生成的常量类
 * 请勿擅自修改
 * @tuhanbao
 */
public class TableConstants {
    public static final Column COUNT = ColumnFactory.createColumn(null, "COUNT(1)", DataType.INT);

    public static final Map<String, Table> TABLES = new HashMap<String, Table>();

    static { init(); }

    private TableConstants() {}

    public static final void register(Table table) {
        TABLES.put(getClassName(table.getName()), table);
    }

    public static Table getTableByClassName(String name) {
        return TABLES.get(name);
    }

    private static String getClassName(String tableName) {
        if (tableName.startsWith("T_") || tableName.startsWith("I_")) {
            tableName = tableName.substring(2);
        }
        return ClazzUtil.getClassName(tableName);
    }

    public static void init() {
        register(T_COMPANY.TABLE); 
        register(T_COMPANY_SMS_INFO.TABLE); 
        register(T_CUSTOM.TABLE); 
        register(T_DIY_COL_INFO.TABLE); 
        register(T_DIY_FILTER.TABLE); 
        register(T_DIY_FILTER_ITEM.TABLE); 
        register(T_FOLLOW_RECORD.TABLE); 
        register(T_PERMISSION.TABLE); 
        register(T_ROLE.TABLE); 
        register(T_ROLE_PERMISSION.TABLE); 
        register(T_SMS_RECORD.TABLE); 
        register(T_USER.TABLE); 
    }

    public static final class T_COMPANY {
        public static final Table TABLE = new Table(9, "T_COMPANY", CacheType.CACHE_ALL, "com.tuhanbao.api.crm.model.crm.Company", null, false);

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column NAME = ColumnFactory.createColumn(TABLE, "NAME", DataType.STRING, false);
        public static final Column SHORT_NAME = ColumnFactory.createColumn(TABLE, "SHORT_NAME", DataType.STRING, false);
        public static final Column SERVER_STATE = ColumnFactory.createColumn(TABLE, "SERVER_STATE", DataType.INT, false);
        public static final Column VALID_TERM = ColumnFactory.createColumn(TABLE, "VALID_TERM", DataType.DATE, false);
        public static final Column TEST_BOOL = ColumnFactory.createColumn(TABLE, "TEST_BOOL", DataType.BOOLEAN, false);
        public static final Column TEST_FLOAT = ColumnFactory.createColumn(TABLE, "TEST_FLOAT", DataType.FLOAT, false);
        public static final Column TEST_DOUBLE = ColumnFactory.createColumn(TABLE, "TEST_DOUBLE", DataType.DOUBLE, false);
        public static final Column TEST_INT = ColumnFactory.createColumn(TABLE, "TEST_INT", DataType.INT, false);
    }

    public static final class T_COMPANY_SMS_INFO {
        public static final Table TABLE = new Table(6, "T_COMPANY_SMS_INFO", CacheType.CACHE_ALL, "com.tuhanbao.api.crm.model.crm.CompanySmsInfo", null, false);

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column COMPANY_ID = ColumnFactory.createColumn(TABLE, "COMPANY_ID", DataType.LONG, T_COMPANY.TABLE, Relation.N2One);
        public static final Column REMAIN_NUM = ColumnFactory.createColumn(TABLE, "REMAIN_NUM", DataType.INT, false);
        public static final Column TOTAL_NUM = ColumnFactory.createColumn(TABLE, "TOTAL_NUM", DataType.INT, false);
        public static final Column TOTAL_MONEY = ColumnFactory.createColumn(TABLE, "TOTAL_MONEY", DataType.LONG, false);
        public static final Column PRICE = ColumnFactory.createColumn(TABLE, "PRICE", DataType.INT, false);
    }

    public static final class T_CUSTOM {
        public static final Table TABLE = new Table(18, "T_CUSTOM", CacheType.NOT_CACHE, "com.tuhanbao.api.crm.model.crm.Custom", null, true);

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column NAME = ColumnFactory.createColumn(TABLE, "NAME", DataType.STRING, false);
        public static final Column TEL_NUM = ColumnFactory.createColumn(TABLE, "TEL_NUM", DataType.STRING, false);
        public static final Column STATE = ColumnFactory.createColumn(TABLE, "STATE", DataType.INT, false);
        public static final Column MARK = ColumnFactory.createColumn(TABLE, "MARK", DataType.STRING, false);
        public static final Column OPERATOR_ID = ColumnFactory.createColumn(TABLE, "OPERATOR_ID", DataType.LONG, T_USER.TABLE, Relation.N2One);
        public static final Column SEX = ColumnFactory.createColumn(TABLE, "SEX", DataType.INT, false);
        public static final Column BIRTHDAY = ColumnFactory.createColumn(TABLE, "BIRTHDAY", DataType.DATE, false);
        public static final Column DIY_COLUMN1 = ColumnFactory.createColumn(TABLE, "DIY_COLUMN1", DataType.STRING, false);
        public static final Column DIY_COLUMN2 = ColumnFactory.createColumn(TABLE, "DIY_COLUMN2", DataType.STRING, false);
        public static final Column DIY_COLUMN3 = ColumnFactory.createColumn(TABLE, "DIY_COLUMN3", DataType.STRING, false);
        public static final Column DIY_COLUMN4 = ColumnFactory.createColumn(TABLE, "DIY_COLUMN4", DataType.STRING, false);
        public static final Column DIY_COLUMN5 = ColumnFactory.createColumn(TABLE, "DIY_COLUMN5", DataType.STRING, false);
        public static final Column DIY_COLUMN6 = ColumnFactory.createColumn(TABLE, "DIY_COLUMN6", DataType.STRING, false);
        public static final Column DIY_COLUMN7 = ColumnFactory.createColumn(TABLE, "DIY_COLUMN7", DataType.STRING, false);
        public static final Column DIY_COLUMN8 = ColumnFactory.createColumn(TABLE, "DIY_COLUMN8", DataType.STRING, false);
        public static final Column DIY_COLUMN9 = ColumnFactory.createColumn(TABLE, "DIY_COLUMN9", DataType.STRING, false);
        public static final Column DIY_COLUMN10 = ColumnFactory.createColumn(TABLE, "DIY_COLUMN10", DataType.STRING, false);
    }

    public static final class T_DIY_COL_INFO {
        public static final Table TABLE = new Table(7, "T_DIY_COL_INFO", CacheType.CACHE_ALL, "com.tuhanbao.api.crm.model.crm.DiyColInfo", null, false);

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column COMPANY_ID = ColumnFactory.createColumn(TABLE, "COMPANY_ID", DataType.LONG, T_COMPANY.TABLE, Relation.N2One);
        public static final Column TABLE_NAME = ColumnFactory.createColumn(TABLE, "TABLE_NAME", DataType.STRING, false);
        public static final Column COLUMN_NAME = ColumnFactory.createColumn(TABLE, "COLUMN_NAME", DataType.STRING, false);
        public static final Column TYPE = ColumnFactory.createColumn(TABLE, "TYPE", DataType.INT, false);
        public static final Column NAME = ColumnFactory.createColumn(TABLE, "NAME", DataType.STRING, false);
        public static final Column OTHER_INFO = ColumnFactory.createColumn(TABLE, "OTHER_INFO", DataType.STRING, false);
    }

    public static final class T_DIY_FILTER {
        public static final Table TABLE = new Table(5, "T_DIY_FILTER", CacheType.NOT_CACHE, "com.tuhanbao.api.crm.model.crm.DiyFilter", null, false);

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column FILTER_TYPE = ColumnFactory.createColumn(TABLE, "FILTER_TYPE", DataType.INT, false);
        public static final Column NAME = ColumnFactory.createColumn(TABLE, "NAME", DataType.STRING, false);
        public static final Column SORT = ColumnFactory.createColumn(TABLE, "SORT", DataType.INT, false);
        public static final Column COMPANY_ID = ColumnFactory.createColumn(TABLE, "COMPANY_ID", DataType.LONG, T_COMPANY.TABLE, Relation.N2One);
    }

    public static final class T_DIY_FILTER_ITEM {
        public static final Table TABLE = new Table(6, "T_DIY_FILTER_ITEM", CacheType.NOT_CACHE, "com.tuhanbao.api.crm.model.crm.DiyFilterItem", null, false);

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column FILTER_ID = ColumnFactory.createColumn(TABLE, "FILTER_ID", DataType.LONG, T_DIY_FILTER.TABLE, Relation.N2One);
        public static final Column LOGIC_TYPE = ColumnFactory.createColumn(TABLE, "LOGIC_TYPE", DataType.INT, false);
        public static final Column NAME = ColumnFactory.createColumn(TABLE, "NAME", DataType.STRING, false);
        public static final Column OPERATOR = ColumnFactory.createColumn(TABLE, "OPERATOR", DataType.INT, false);
        public static final Column VALUE = ColumnFactory.createColumn(TABLE, "VALUE", DataType.STRING, false);
    }

    public static final class T_FOLLOW_RECORD {
        public static final Table TABLE = new Table(8, "T_FOLLOW_RECORD", CacheType.NOT_CACHE, "com.tuhanbao.api.crm.model.crm.FollowRecord", null, true);

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column CUSTOM_ID = ColumnFactory.createColumn(TABLE, "CUSTOM_ID", DataType.LONG, T_CUSTOM.TABLE, Relation.N2One);
        public static final Column STATE = ColumnFactory.createColumn(TABLE, "STATE", DataType.INT, false);
        public static final Column MARK = ColumnFactory.createColumn(TABLE, "MARK", DataType.STRING, false);
        public static final Column FOLLOW_TIME = ColumnFactory.createColumn(TABLE, "FOLLOW_TIME", DataType.DATE, false);
        public static final Column DIY_COLUMN1 = ColumnFactory.createColumn(TABLE, "DIY_COLUMN1", DataType.STRING, false);
        public static final Column DIY_COLUMN2 = ColumnFactory.createColumn(TABLE, "DIY_COLUMN2", DataType.STRING, false);
        public static final Column DIY_COLUMN3 = ColumnFactory.createColumn(TABLE, "DIY_COLUMN3", DataType.STRING, false);
    }

    public static final class T_PERMISSION {
        public static final Table TABLE = new Table(9, "T_PERMISSION", CacheType.NOT_CACHE, "com.tuhanbao.api.crm.model.crm.Permission", null, false);

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column PARENT_ID = ColumnFactory.createColumn(TABLE, "PARENT_ID", DataType.LONG, T_PERMISSION.TABLE, Relation.N2One);
        public static final Column NAME = ColumnFactory.createColumn(TABLE, "NAME", DataType.STRING, false);
        public static final Column IS_MENU = ColumnFactory.createColumn(TABLE, "IS_MENU", DataType.BOOLEAN, false);
        public static final Column URL = ColumnFactory.createColumn(TABLE, "URL", DataType.STRING, false);
        public static final Column SORT = ColumnFactory.createColumn(TABLE, "SORT", DataType.INT, false);
        public static final Column MARK = ColumnFactory.createColumn(TABLE, "MARK", DataType.STRING, false);
        public static final Column HTML = ColumnFactory.createColumn(TABLE, "HTML", DataType.STRING, false);
        public static final Column OTHER_INFO = ColumnFactory.createColumn(TABLE, "OTHER_INFO", DataType.STRING, false);
    }

    public static final class T_ROLE {
        public static final Table TABLE = new Table(3, "T_ROLE", CacheType.NOT_CACHE, "com.tuhanbao.api.crm.model.crm.Role", null, false);

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column ROLE_NAME = ColumnFactory.createColumn(TABLE, "ROLE_NAME", DataType.STRING, false);
        public static final Column MARK = ColumnFactory.createColumn(TABLE, "MARK", DataType.STRING, false);
    }

    public static final class T_ROLE_PERMISSION {
        public static final Table TABLE = new Table(3, "T_ROLE_PERMISSION", CacheType.NOT_CACHE, "com.tuhanbao.api.crm.model.crm.RolePermission", null, false);

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column PERMISSION_ID = ColumnFactory.createColumn(TABLE, "PERMISSION_ID", DataType.LONG, T_PERMISSION.TABLE, Relation.N2One);
        public static final Column ROLE_ID = ColumnFactory.createColumn(TABLE, "ROLE_ID", DataType.LONG, T_ROLE.TABLE, Relation.N2One);
    }

    public static final class T_SMS_RECORD {
        public static final Table TABLE = new Table(6, "T_SMS_RECORD", CacheType.NOT_CACHE, "com.tuhanbao.api.crm.model.crm.SmsRecord", null, true);

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column CUSTOM_ID = ColumnFactory.createColumn(TABLE, "CUSTOM_ID", DataType.LONG, T_CUSTOM.TABLE, Relation.N2One);
        public static final Column USER_ID = ColumnFactory.createColumn(TABLE, "USER_ID", DataType.LONG, T_USER.TABLE, Relation.N2One);
        public static final Column SEND_DATE = ColumnFactory.createColumn(TABLE, "SEND_DATE", DataType.DATE, false);
        public static final Column SEND_CONTENT = ColumnFactory.createColumn(TABLE, "SEND_CONTENT", DataType.STRING, false);
        public static final Column STATE = ColumnFactory.createColumn(TABLE, "STATE", DataType.INT, false);
    }

    public static final class T_USER {
        public static final Table TABLE = new Table(6, "T_USER", CacheType.NOT_CACHE, "com.tuhanbao.api.crm.model.crm.User", null, false);

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column LOGIN_NAME = ColumnFactory.createColumn(TABLE, "LOGIN_NAME", DataType.STRING, false);
        public static final Column PASSWORD = ColumnFactory.createColumn(TABLE, "PASSWORD", DataType.STRING, false);
        public static final Column ROLE_ID = ColumnFactory.createColumn(TABLE, "ROLE_ID", DataType.LONG, T_ROLE.TABLE, Relation.N2One);
        public static final Column MARK = ColumnFactory.createColumn(TABLE, "MARK", DataType.STRING, false);
        public static final Column COMPANY_ID = ColumnFactory.createColumn(TABLE, "COMPANY_ID", DataType.LONG, T_COMPANY.TABLE, Relation.N2One);
    }

}