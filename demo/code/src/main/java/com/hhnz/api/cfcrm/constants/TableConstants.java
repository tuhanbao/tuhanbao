package com.hhnz.api.cfcrm.constants;

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
        register(C_EXTEND_INFO.TABLE); 
        register(OFFICE.TABLE); 
        register(OFFICE_RELATION.TABLE); 
        register(RELATION.TABLE); 
        register(ROLE.TABLE); 
        register(T_AREA.TABLE); 
        register(T_AWARD_ITEM.TABLE); 
        register(T_AWARD_VIP.TABLE); 
        register(T_DIY_FILTER.TABLE); 
        register(T_DIY_FILTER_ITEM.TABLE); 
        register(T_MSG_AUTO_SEND.TABLE); 
        register(T_MSG_GROUP.TABLE); 
        register(T_MSG_MODEL.TABLE); 
        register(T_PAYMENT_COLLECTION.TABLE); 
        register(T_PERMISSION.TABLE); 
        register(T_ROLE.TABLE); 
        register(T_ROLE_PERMISSION.TABLE); 
        register(T_SEND_HISTORY_VIP.TABLE); 
        register(T_SEND_TO_VIP.TABLE); 
        register(T_SEPARATE_BILLS_APPLY.TABLE); 
        register(T_USER.TABLE); 
        register(T_VIP_AWARD_MANAGE.TABLE); 
        register(T_VIP_INVEST_FLOW.TABLE); 
        register(T_VIP_INVEST_INFO.TABLE); 
        register(T_VIP_MARKETING_RECORD.TABLE); 
        register(T_VIP_MSG_SENDING_HISTORY.TABLE); 
        register(T_VIP_PRIMARY_INFO.TABLE); 
        register(USERS_BAN.TABLE); 
    }

    public static final class C_EXTEND_INFO {
        public static final Table TABLE = new Table(7, "C_EXTEND_INFO", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.fz.CExtendInfo");

        public static final Column NONGZHUANG_ID = ColumnFactory.createColumn(TABLE, "NONGZHUANG_ID", DataType.LONG, true, true);
        public static final Column CUSTOMER = ColumnFactory.createColumn(TABLE, "CUSTOMER", DataType.STRING, false);
        public static final Column OFFICE = ColumnFactory.createColumn(TABLE, "OFFICE", DataType.STRING, false);
        public static final Column POSITION = ColumnFactory.createColumn(TABLE, "POSITION", DataType.STRING, false);
        public static final Column ID_NUMBER = ColumnFactory.createColumn(TABLE, "ID_NUMBER", DataType.STRING, false);
        public static final Column DETAIL_ADDRESS = ColumnFactory.createColumn(TABLE, "DETAIL_ADDRESS", DataType.STRING, false);
        public static final Column COMPLETE_ADDRESS = ColumnFactory.createColumn(TABLE, "COMPLETE_ADDRESS", DataType.STRING, false);
    }

    public static final class OFFICE {
        public static final Table TABLE = new Table(6, "OFFICE", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.fz.Office");

        public static final Column OFFICE = ColumnFactory.createColumn(TABLE, "OFFICE", DataType.STRING, false, true);
        public static final Column OFFICE_ID = ColumnFactory.createColumn(TABLE, "OFFICE_ID", DataType.STRING, true);
        public static final Column AREA_ID = ColumnFactory.createColumn(TABLE, "AREA_ID", DataType.STRING, false);
        public static final Column DETAIL_ADDRESS = ColumnFactory.createColumn(TABLE, "DETAIL_ADDRESS", DataType.STRING, false);
        public static final Column COMPLETE_ADDRESS = ColumnFactory.createColumn(TABLE, "COMPLETE_ADDRESS", DataType.STRING, false);
        public static final Column IS_START = ColumnFactory.createColumn(TABLE, "IS_START", DataType.BOOLEAN, false);
    }

    public static final class OFFICE_RELATION {
        public static final Table TABLE = new Table(2, "OFFICE_RELATION", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.fz.OfficeRelation");

        public static final Column OFFICE_ID = ColumnFactory.createColumn(TABLE, "OFFICE_ID", DataType.STRING, OFFICE.TABLE, Relation.N2One, true);
        public static final Column NONGZHUANG_ID = ColumnFactory.createColumn(TABLE, "NONGZHUANG_ID", DataType.LONG, C_EXTEND_INFO.TABLE, Relation.N2One);
    }

    public static final class RELATION {
        public static final Table TABLE = new Table(5, "RELATION", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.fz.Relation");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true, true);
        public static final Column CHILD = ColumnFactory.createColumn(TABLE, "CHILD", DataType.LONG, false, true);
        public static final Column FATHER = ColumnFactory.createColumn(TABLE, "FATHER", DataType.LONG, false, true);
        public static final Column YEAR = ColumnFactory.createColumn(TABLE, "YEAR", DataType.INT, false);
        public static final Column MONTH = ColumnFactory.createColumn(TABLE, "MONTH", DataType.INT, false);
    }

    public static final class ROLE {
        public static final Table TABLE = new Table(7, "ROLE", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.fz.Role");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true, true);
        public static final Column NONGZHUANG_ID = ColumnFactory.createColumn(TABLE, "NONGZHUANG_ID", DataType.LONG, C_EXTEND_INFO.TABLE, Relation.N2One, true);
        public static final Column ROLE = ColumnFactory.createColumn(TABLE, "ROLE", DataType.STRING, false);
        public static final Column TYPE = ColumnFactory.createColumn(TABLE, "TYPE", DataType.STRING, false);
        public static final Column HIGHEST_ROLE = ColumnFactory.createColumn(TABLE, "HIGHEST_ROLE", DataType.STRING, false);
        public static final Column YEAR = ColumnFactory.createColumn(TABLE, "YEAR", DataType.STRING, false);
        public static final Column MONTH = ColumnFactory.createColumn(TABLE, "MONTH", DataType.STRING, false);
    }

    public static final class T_AREA {
        public static final Table TABLE = new Table(3, "T_AREA", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.Area");

        public static final Column AREA_ID = ColumnFactory.createColumn(TABLE, "AREA_ID", DataType.STRING, true);
        public static final Column AREA_NAME = ColumnFactory.createColumn(TABLE, "AREA_NAME", DataType.STRING, false);
        public static final Column PARENT_ID = ColumnFactory.createColumn(TABLE, "PARENT_ID", DataType.STRING, T_AREA.TABLE, Relation.N2One);
    }

    public static final class T_AWARD_ITEM {
        public static final Table TABLE = new Table(5, "T_AWARD_ITEM", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.AwardItem");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column AWARD_ID = ColumnFactory.createColumn(TABLE, "AWARD_ID", DataType.LONG, T_VIP_AWARD_MANAGE.TABLE, Relation.N2One);
        public static final Column AWARD_TYPE_NAME = ColumnFactory.createColumn(TABLE, "AWARD_TYPE_NAME", DataType.STRING, false);
        public static final Column AWARD_NUM = ColumnFactory.createColumn(TABLE, "AWARD_NUM", DataType.LONG, false);
        public static final Column AWARD_CONTENT = ColumnFactory.createColumn(TABLE, "AWARD_CONTENT", DataType.STRING, false);
    }

    public static final class T_AWARD_VIP {
        public static final Table TABLE = new Table(5, "T_AWARD_VIP", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.AwardVip");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column CUSTOM_ID = ColumnFactory.createColumn(TABLE, "CUSTOM_ID", DataType.LONG, T_VIP_PRIMARY_INFO.TABLE, Relation.N2One);
        public static final Column AWARD_ID = ColumnFactory.createColumn(TABLE, "AWARD_ID", DataType.LONG, T_VIP_AWARD_MANAGE.TABLE, Relation.N2One);
        public static final Column SEND_STATUS = ColumnFactory.createColumn(TABLE, "SEND_STATUS", DataType.INT, false);
        public static final Column SEND_TYPE = ColumnFactory.createColumn(TABLE, "SEND_TYPE", DataType.INT, false);
    }

    public static final class T_DIY_FILTER {
        public static final Table TABLE = new Table(4, "T_DIY_FILTER", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.DiyFilter");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column FILTER_TYPE = ColumnFactory.createColumn(TABLE, "FILTER_TYPE", DataType.INT, false);
        public static final Column NAME = ColumnFactory.createColumn(TABLE, "NAME", DataType.STRING, false);
        public static final Column SORT = ColumnFactory.createColumn(TABLE, "SORT", DataType.INT, false);
    }

    public static final class T_DIY_FILTER_ITEM {
        public static final Table TABLE = new Table(6, "T_DIY_FILTER_ITEM", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.DiyFilterItem");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column FILTER_ID = ColumnFactory.createColumn(TABLE, "FILTER_ID", DataType.LONG, T_DIY_FILTER.TABLE, Relation.N2One);
        public static final Column LOGIC_TYPE = ColumnFactory.createColumn(TABLE, "LOGIC_TYPE", DataType.INT, false);
        public static final Column NAME = ColumnFactory.createColumn(TABLE, "NAME", DataType.STRING, false);
        public static final Column OPERATOR = ColumnFactory.createColumn(TABLE, "OPERATOR", DataType.INT, false);
        public static final Column VALUE = ColumnFactory.createColumn(TABLE, "VALUE", DataType.STRING, false);
    }

    public static final class T_MSG_AUTO_SEND {
        public static final Table TABLE = new Table(9, "T_MSG_AUTO_SEND", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.MsgAutoSend");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column TYPE = ColumnFactory.createColumn(TABLE, "TYPE", DataType.INT, false);
        public static final Column NAME = ColumnFactory.createColumn(TABLE, "NAME", DataType.STRING, false);
        public static final Column SEND_DATE = ColumnFactory.createColumn(TABLE, "SEND_DATE", DataType.DATE, false);
        public static final Column TIME = ColumnFactory.createColumn(TABLE, "TIME", DataType.INT, false);
        public static final Column CONTENT = ColumnFactory.createColumn(TABLE, "CONTENT", DataType.STRING, false);
        public static final Column IS_OPEN = ColumnFactory.createColumn(TABLE, "IS_OPEN", DataType.BOOLEAN, false);
        public static final Column SEND_STATUS = ColumnFactory.createColumn(TABLE, "SEND_STATUS", DataType.INT, false);
        public static final Column SEND_TYPE = ColumnFactory.createColumn(TABLE, "SEND_TYPE", DataType.INT, false);
    }

    public static final class T_MSG_GROUP {
        public static final Table TABLE = new Table(3, "T_MSG_GROUP", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.MsgGroup");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column GROUP_NAME = ColumnFactory.createColumn(TABLE, "GROUP_NAME", DataType.STRING, false);
        public static final Column SORT = ColumnFactory.createColumn(TABLE, "SORT", DataType.INT, false);
    }

    public static final class T_MSG_MODEL {
        public static final Table TABLE = new Table(3, "T_MSG_MODEL", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.MsgModel");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column GROUP_ID = ColumnFactory.createColumn(TABLE, "GROUP_ID", DataType.LONG, T_MSG_GROUP.TABLE, Relation.N2One);
        public static final Column CONTENT = ColumnFactory.createColumn(TABLE, "CONTENT", DataType.STRING, false);
    }

    public static final class T_PAYMENT_COLLECTION {
        public static final Table TABLE = new Table(8, "T_PAYMENT_COLLECTION", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.PaymentCollection");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column CUSTOM_ID = ColumnFactory.createColumn(TABLE, "CUSTOM_ID", DataType.LONG, T_VIP_PRIMARY_INFO.TABLE, Relation.N2One);
        public static final Column PROJECT_NAME = ColumnFactory.createColumn(TABLE, "PROJECT_NAME", DataType.STRING, false);
        public static final Column CAPITAL = ColumnFactory.createColumn(TABLE, "CAPITAL", DataType.INT, false);
        public static final Column PROFIT = ColumnFactory.createColumn(TABLE, "PROFIT", DataType.INT, false);
        public static final Column STATUS = ColumnFactory.createColumn(TABLE, "STATUS", DataType.INT, false);
        public static final Column PAYMENT_COLLECTION_DATE = ColumnFactory.createColumn(TABLE, "PAYMENT_COLLECTION_DATE", DataType.DATE, false);
        public static final Column PAYMENT_COLLECTION = ColumnFactory.createColumn(TABLE, "PAYMENT_COLLECTION", DataType.LONG, false);
    }

    public static final class T_PERMISSION {
        public static final Table TABLE = new Table(9, "T_PERMISSION", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.Permission");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column PARENT_ID = ColumnFactory.createColumn(TABLE, "PARENT_ID", DataType.LONG, T_PERMISSION.TABLE, Relation.N2One);
        public static final Column NAME = ColumnFactory.createColumn(TABLE, "NAME", DataType.STRING, false);
        public static final Column IS_MENU = ColumnFactory.createColumn(TABLE, "IS_MENU", DataType.BOOLEAN, false);
        public static final Column URL = ColumnFactory.createColumn(TABLE, "URL", DataType.STRING, false);
        public static final Column CREATE_DATE = ColumnFactory.createColumn(TABLE, "CREATE_DATE", DataType.DATE, false);
        public static final Column MODIFY_DATE = ColumnFactory.createColumn(TABLE, "MODIFY_DATE", DataType.DATE, false);
        public static final Column SORT = ColumnFactory.createColumn(TABLE, "SORT", DataType.INT, false);
        public static final Column MARK = ColumnFactory.createColumn(TABLE, "MARK", DataType.STRING, false);
    }

    public static final class T_ROLE {
        public static final Table TABLE = new Table(5, "T_ROLE", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.Role");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column ROLE_NAME = ColumnFactory.createColumn(TABLE, "ROLE_NAME", DataType.STRING, false);
        public static final Column MARK = ColumnFactory.createColumn(TABLE, "MARK", DataType.STRING, false);
        public static final Column CREATE_DATE = ColumnFactory.createColumn(TABLE, "CREATE_DATE", DataType.DATE, false);
        public static final Column CREATE_USER_ID = ColumnFactory.createColumn(TABLE, "CREATE_USER_ID", DataType.LONG, T_USER.TABLE, Relation.N2One);
    }

    public static final class T_ROLE_PERMISSION {
        public static final Table TABLE = new Table(3, "T_ROLE_PERMISSION", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.RolePermission");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column PERMISSION_ID = ColumnFactory.createColumn(TABLE, "PERMISSION_ID", DataType.LONG, T_PERMISSION.TABLE, Relation.N2One);
        public static final Column ROLE_ID = ColumnFactory.createColumn(TABLE, "ROLE_ID", DataType.LONG, T_ROLE.TABLE, Relation.N2One);
    }

    public static final class T_SEND_HISTORY_VIP {
        public static final Table TABLE = new Table(5, "T_SEND_HISTORY_VIP", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.SendHistoryVip");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column MSG_ID = ColumnFactory.createColumn(TABLE, "MSG_ID", DataType.LONG, T_VIP_MSG_SENDING_HISTORY.TABLE, Relation.N2One);
        public static final Column VIP_PHONE = ColumnFactory.createColumn(TABLE, "VIP_PHONE", DataType.STRING, false);
        public static final Column CUSTOM_ID = ColumnFactory.createColumn(TABLE, "CUSTOM_ID", DataType.LONG, T_VIP_PRIMARY_INFO.TABLE, Relation.N2One);
        public static final Column SEND_STATUS = ColumnFactory.createColumn(TABLE, "SEND_STATUS", DataType.INT, false);
    }

    public static final class T_SEND_TO_VIP {
        public static final Table TABLE = new Table(4, "T_SEND_TO_VIP", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.SendToVip");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column MSG_ID = ColumnFactory.createColumn(TABLE, "MSG_ID", DataType.LONG, T_MSG_AUTO_SEND.TABLE, Relation.N2One);
        public static final Column VIP_PHONE = ColumnFactory.createColumn(TABLE, "VIP_PHONE", DataType.STRING, false);
        public static final Column CUSTOM_ID = ColumnFactory.createColumn(TABLE, "CUSTOM_ID", DataType.LONG, T_VIP_PRIMARY_INFO.TABLE, Relation.N2One);
    }

    public static final class T_SEPARATE_BILLS_APPLY {
        public static final Table TABLE = new Table(4, "T_SEPARATE_BILLS_APPLY", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.fz.SeparateBillsApply");

        public static final Column APPLY_ID = ColumnFactory.createColumn(TABLE, "APPLY_ID", DataType.LONG, C_EXTEND_INFO.TABLE, Relation.N2One, true);
        public static final Column PHONE = ColumnFactory.createColumn(TABLE, "PHONE", DataType.STRING, true, true);
        public static final Column ADDR = ColumnFactory.createColumn(TABLE, "ADDR", DataType.STRING, false, true);
        public static final Column ADDR_PCD = ColumnFactory.createColumn(TABLE, "ADDR_PCD", DataType.STRING, false, true);
    }

    public static final class T_USER {
        public static final Table TABLE = new Table(7, "T_USER", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.User");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column LOGIN_NAME = ColumnFactory.createColumn(TABLE, "LOGIN_NAME", DataType.STRING, false);
        public static final Column PASSWORD = ColumnFactory.createColumn(TABLE, "PASSWORD", DataType.STRING, false);
        public static final Column CREATE_DATE = ColumnFactory.createColumn(TABLE, "CREATE_DATE", DataType.DATE, false);
        public static final Column CREATE_USER_ID = ColumnFactory.createColumn(TABLE, "CREATE_USER_ID", DataType.LONG, T_USER.TABLE, Relation.N2One);
        public static final Column ROLE_ID = ColumnFactory.createColumn(TABLE, "ROLE_ID", DataType.LONG, T_ROLE.TABLE, Relation.N2One);
        public static final Column MARK = ColumnFactory.createColumn(TABLE, "MARK", DataType.STRING, false);
    }

    public static final class T_VIP_AWARD_MANAGE {
        public static final Table TABLE = new Table(7, "T_VIP_AWARD_MANAGE", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.VipAwardManage");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column AWARD_NAME = ColumnFactory.createColumn(TABLE, "AWARD_NAME", DataType.STRING, false);
        public static final Column USER_ID = ColumnFactory.createColumn(TABLE, "USER_ID", DataType.LONG, T_USER.TABLE, Relation.N2One);
        public static final Column CREATING_TIME = ColumnFactory.createColumn(TABLE, "CREATING_TIME", DataType.DATE, false);
        public static final Column AWARD_DATE = ColumnFactory.createColumn(TABLE, "AWARD_DATE", DataType.DATE, false);
        public static final Column SEND_STATUS = ColumnFactory.createColumn(TABLE, "SEND_STATUS", DataType.INT, false);
        public static final Column SEND_TYPE = ColumnFactory.createColumn(TABLE, "SEND_TYPE", DataType.INT, false);
    }

    public static final class T_VIP_INVEST_FLOW {
        public static final Table TABLE = new Table(26, "T_VIP_INVEST_FLOW", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.VipInvestFlow");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column CUSTOM_ID = ColumnFactory.createColumn(TABLE, "CUSTOM_ID", DataType.LONG, T_VIP_PRIMARY_INFO.TABLE, Relation.N2One);
        public static final Column NAME = ColumnFactory.createColumn(TABLE, "NAME", DataType.STRING, false, true);
        public static final Column INVEST_SOURCE = ColumnFactory.createColumn(TABLE, "INVEST_SOURCE", DataType.LONG, false);
        public static final Column EXPECTED_BACK_RATE = ColumnFactory.createColumn(TABLE, "EXPECTED_BACK_RATE", DataType.STRING, false);
        public static final Column INVESTOR_REG_DATE = ColumnFactory.createColumn(TABLE, "INVESTOR_REG_DATE", DataType.DATE, false);
        public static final Column INVEST_AMOUNT = ColumnFactory.createColumn(TABLE, "INVEST_AMOUNT", DataType.LONG, false);
        public static final Column INVEST_TERM = ColumnFactory.createColumn(TABLE, "INVEST_TERM", DataType.INT, false);
        public static final Column ANNUALIZED_INVEST = ColumnFactory.createColumn(TABLE, "ANNUALIZED_INVEST", DataType.STRING, false);
        public static final Column DISCOUNT_CODE = ColumnFactory.createColumn(TABLE, "DISCOUNT_CODE", DataType.STRING, false);
        public static final Column DISCOUNT_CUSTOM_ID = ColumnFactory.createColumn(TABLE, "DISCOUNT_CUSTOM_ID", DataType.LONG, false);
        public static final Column DISCOUNT_CUSTOM_NAME = ColumnFactory.createColumn(TABLE, "DISCOUNT_CUSTOM_NAME", DataType.STRING, false);
        public static final Column DISCOUNT_CUSTOM_CREATE_DATE = ColumnFactory.createColumn(TABLE, "DISCOUNT_CUSTOM_CREATE_DATE", DataType.DATE, false);
        public static final Column INVITATION_LEVEL = ColumnFactory.createColumn(TABLE, "INVITATION_LEVEL", DataType.LONG, false);
        public static final Column INVITATION_MONEY = ColumnFactory.createColumn(TABLE, "INVITATION_MONEY", DataType.STRING, false);
        public static final Column STANDER_TYPE = ColumnFactory.createColumn(TABLE, "STANDER_TYPE", DataType.STRING, false);
        public static final Column STANDER_STATUS = ColumnFactory.createColumn(TABLE, "STANDER_STATUS", DataType.STRING, false);
        public static final Column SETTLEMENT_STATUS = ColumnFactory.createColumn(TABLE, "SETTLEMENT_STATUS", DataType.LONG, false);
        public static final Column SETTLEMENT_DATE = ColumnFactory.createColumn(TABLE, "SETTLEMENT_DATE", DataType.DATE, false);
        public static final Column REPAYMENT_TYEP = ColumnFactory.createColumn(TABLE, "REPAYMENT_TYEP", DataType.STRING, false);
        public static final Column DISCOUNT_PERSON_INVITATION_ID = ColumnFactory.createColumn(TABLE, "DISCOUNT_PERSON_INVITATION_ID", DataType.LONG, false);
        public static final Column DISCOUNT_PERSON_INVITATION_NAME = ColumnFactory.createColumn(TABLE, "DISCOUNT_PERSON_INVITATION_NAME", DataType.STRING, false);
        public static final Column DISCOUNT_PERSON_INVITATION_GROUP = ColumnFactory.createColumn(TABLE, "DISCOUNT_PERSON_INVITATION_GROUP", DataType.STRING, false);
        public static final Column STANDER_TAG = ColumnFactory.createColumn(TABLE, "STANDER_TAG", DataType.STRING, false);
        public static final Column INVEST_INVITATION = ColumnFactory.createColumn(TABLE, "INVEST_INVITATION", DataType.STRING, false);
        public static final Column INVEST_DATE = ColumnFactory.createColumn(TABLE, "INVEST_DATE", DataType.DATE, false);
    }

    public static final class T_VIP_INVEST_INFO {
        public static final Table TABLE = new Table(19, "T_VIP_INVEST_INFO", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.VipInvestInfo");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column CUSTOM_ID = ColumnFactory.createColumn(TABLE, "CUSTOM_ID", DataType.LONG, T_VIP_PRIMARY_INFO.TABLE, Relation.N2One);
        public static final Column DAY_INVEST_BALANCE = ColumnFactory.createColumn(TABLE, "DAY_INVEST_BALANCE", DataType.LONG, false, true);
        public static final Column DAY_ACCOUNT_BALANCE = ColumnFactory.createColumn(TABLE, "DAY_ACCOUNT_BALANCE", DataType.LONG, false, true);
        public static final Column DAY_ADD_INVEST_AMOUNT = ColumnFactory.createColumn(TABLE, "DAY_ADD_INVEST_AMOUNT", DataType.LONG, false, true);
        public static final Column TOTAL_INVEST_TIMES = ColumnFactory.createColumn(TABLE, "TOTAL_INVEST_TIMES", DataType.INT, false, true);
        public static final Column ACC_INV_AMOUNT = ColumnFactory.createColumn(TABLE, "ACC_INV_AMOUNT", DataType.LONG, false, true);
        public static final Column ACC_INV_YEAR_AMOUNT = ColumnFactory.createColumn(TABLE, "ACC_INV_YEAR_AMOUNT", DataType.LONG, false, true);
        public static final Column ACC_INVI_INVEST_PERSONS = ColumnFactory.createColumn(TABLE, "ACC_INVI_INVEST_PERSONS", DataType.INT, false, true);
        public static final Column ACC_INVI_INVEST_AMOUNT = ColumnFactory.createColumn(TABLE, "ACC_INVI_INVEST_AMOUNT", DataType.LONG, false, true);
        public static final Column ACC_INVI_INVEST_AMOUNT_YEAR = ColumnFactory.createColumn(TABLE, "ACC_INVI_INVEST_AMOUNT_YEAR", DataType.LONG, false, true);
        public static final Column DAILY_DECREASEMENT = ColumnFactory.createColumn(TABLE, "DAILY_DECREASEMENT", DataType.INT, false, true);
        public static final Column OFF_DAY = ColumnFactory.createColumn(TABLE, "OFF_DAY", DataType.INT, false, true);
        public static final Column RANK = ColumnFactory.createColumn(TABLE, "RANK", DataType.INT, false);
        public static final Column VIP_TYPE = ColumnFactory.createColumn(TABLE, "VIP_TYPE", DataType.INT, false, true, "VipType");
        public static final Column YEAR = ColumnFactory.createColumn(TABLE, "YEAR", DataType.INT, false);
        public static final Column MONTH = ColumnFactory.createColumn(TABLE, "MONTH", DataType.INT, false);
        public static final Column YEAR_MONTH_INVEST_AMOUNT = ColumnFactory.createColumn(TABLE, "YEAR_MONTH_INVEST_AMOUNT", DataType.LONG, false, true);
        public static final Column RANK_ACC = ColumnFactory.createColumn(TABLE, "RANK_ACC", DataType.INT, false);
    }

    public static final class T_VIP_MARKETING_RECORD {
        public static final Table TABLE = new Table(9, "T_VIP_MARKETING_RECORD", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.VipMarketingRecord");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column CUSTOM_ID = ColumnFactory.createColumn(TABLE, "CUSTOM_ID", DataType.LONG, T_VIP_PRIMARY_INFO.TABLE, Relation.N2One);
        public static final Column FOLLOWING_STAGE = ColumnFactory.createColumn(TABLE, "FOLLOWING_STAGE", DataType.INT, false);
        public static final Column CONTENTS = ColumnFactory.createColumn(TABLE, "CONTENTS", DataType.STRING, false);
        public static final Column USER_ID = ColumnFactory.createColumn(TABLE, "USER_ID", DataType.LONG, T_USER.TABLE, Relation.N2One);
        public static final Column EDITOR_ID = ColumnFactory.createColumn(TABLE, "EDITOR_ID", DataType.LONG, false);
        public static final Column CREATE_DATE = ColumnFactory.createColumn(TABLE, "CREATE_DATE", DataType.DATE, false);
        public static final Column EDIT_DATE = ColumnFactory.createColumn(TABLE, "EDIT_DATE", DataType.DATE, false);
        public static final Column FOLLOWING_DATE = ColumnFactory.createColumn(TABLE, "FOLLOWING_DATE", DataType.DATE, false);
    }

    public static final class T_VIP_MSG_SENDING_HISTORY {
        public static final Table TABLE = new Table(5, "T_VIP_MSG_SENDING_HISTORY", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.cfcrm.VipMsgSendingHistory");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column MSG_ID = ColumnFactory.createColumn(TABLE, "MSG_ID", DataType.LONG, T_MSG_AUTO_SEND.TABLE, Relation.N2One);
        public static final Column MSG_NAME = ColumnFactory.createColumn(TABLE, "MSG_NAME", DataType.STRING, false);
        public static final Column SEND_DATE = ColumnFactory.createColumn(TABLE, "SEND_DATE", DataType.DATE, false);
        public static final Column SEND_CONTENT = ColumnFactory.createColumn(TABLE, "SEND_CONTENT", DataType.STRING, false);
    }

    public static final class T_VIP_PRIMARY_INFO {
        public static final Table TABLE = new Table(19, "T_VIP_PRIMARY_INFO", CacheType.CACHE_ALL, "com.hhnz.api.cfcrm.model.cfcrm.VipPrimaryInfo");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true);
        public static final Column NONGZHUANG_ID = ColumnFactory.createColumn(TABLE, "NONGZHUANG_ID", DataType.LONG, false);
        public static final Column NAME = ColumnFactory.createColumn(TABLE, "NAME", DataType.STRING, false, true);
        public static final Column PHONE = ColumnFactory.createColumn(TABLE, "PHONE", DataType.STRING, false, true);
        public static final Column SEX = ColumnFactory.createColumn(TABLE, "SEX", DataType.INT, false, true, "Sex");
        public static final Column INVITATE_NUM = ColumnFactory.createColumn(TABLE, "INVITATE_NUM", DataType.STRING, false, true);
        public static final Column BIRTHDAY = ColumnFactory.createColumn(TABLE, "BIRTHDAY", DataType.DATE, false);
        public static final Column MONTH = ColumnFactory.createColumn(TABLE, "MONTH", DataType.INT, false);
        public static final Column DAY = ColumnFactory.createColumn(TABLE, "DAY", DataType.INT, false);
        public static final Column VIP_PROVINCE_ID = ColumnFactory.createColumn(TABLE, "VIP_PROVINCE_ID", DataType.STRING, T_AREA.TABLE, Relation.N2One);
        public static final Column VIP_CITY_ID = ColumnFactory.createColumn(TABLE, "VIP_CITY_ID", DataType.STRING, T_AREA.TABLE, Relation.N2One);
        public static final Column VIP_DISTRICT_ID = ColumnFactory.createColumn(TABLE, "VIP_DISTRICT_ID", DataType.STRING, T_AREA.TABLE, Relation.N2One);
        public static final Column DETAIL_ADDRESS = ColumnFactory.createColumn(TABLE, "DETAIL_ADDRESS", DataType.STRING, false);
        public static final Column IS_DEL = ColumnFactory.createColumn(TABLE, "IS_DEL", DataType.BOOLEAN, false);
        public static final Column CUSTOM_TYPE = ColumnFactory.createColumn(TABLE, "CUSTOM_TYPE", DataType.INT, false, true, "CustomType");
        public static final Column OFFICE_TYPE = ColumnFactory.createColumn(TABLE, "OFFICE_TYPE", DataType.INT, false);
        public static final Column BE_VIP = ColumnFactory.createColumn(TABLE, "BE_VIP", DataType.BOOLEAN, false);
        public static final Column VIP_START = ColumnFactory.createColumn(TABLE, "VIP_START", DataType.DATE, false);
        public static final Column IS_RESET = ColumnFactory.createColumn(TABLE, "IS_RESET", DataType.BOOLEAN, false);
    }

    public static final class USERS_BAN {
        public static final Table TABLE = new Table(5, "USERS_BAN", CacheType.NOT_CACHE, "com.hhnz.api.cfcrm.model.fz.UsersBan");

        public static final Column ID = ColumnFactory.createColumn(TABLE, "ID", DataType.LONG, true, true);
        public static final Column NONGZHUANG_ID = ColumnFactory.createColumn(TABLE, "NONGZHUANG_ID", DataType.LONG, C_EXTEND_INFO.TABLE, Relation.N2One, true);
        public static final Column YEAR = ColumnFactory.createColumn(TABLE, "YEAR", DataType.INT, false);
        public static final Column MONTH = ColumnFactory.createColumn(TABLE, "MONTH", DataType.INT, false);
        public static final Column SALT = ColumnFactory.createColumn(TABLE, "SALT", DataType.BOOLEAN, false);
    }

}