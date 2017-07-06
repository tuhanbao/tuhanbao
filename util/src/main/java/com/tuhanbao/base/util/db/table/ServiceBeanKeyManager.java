package com.tuhanbao.base.util.db.table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tuhanbao.base.util.clazz.ClazzUtil;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.Relation;

public class ServiceBeanKeyManager {
    private static final Map<Table, Set<String>> SINGLE_KEY = new HashMap<Table, Set<String>>();

    private static Map<Column, Column> AS_COLUMN;

    private static final Map<Table, Map<String, Class<?>>> KEY_COLUMN_MAP = new HashMap<Table, Map<String, Class<?>>>();
    private static final Map<Table, Map<Column, String>> COLUMN_KEY_MAP = new HashMap<Table, Map<Column, String>>();
    
    public static String init(Table table, Column column) {
        String name;
        if (column.getTable() == table) {
            name = column.getName().toLowerCase();
            //外键命名必须规范
            if (name.endsWith("_id")) name = name.substring(0, name.length() - 3);
            name = ClazzUtil.getVarName(name);
            addSingleKey(table, name, column);
            
            //自己连自己
            if (column.getFK() == table) {
                name = column.getTable().getName().toLowerCase();
                //表名必须规范
                if (name.startsWith("t_") || name.startsWith("v_") || name.startsWith("i_")) {
                    name = name.substring(2);
                }
                
                //外键关系必须明确
                if (column.getRelation() != Relation.One2One) {
                    name += "s";
                    addKey(table, name, getAsColumn(column));
                }
                else {
                    addSingleKey(table, name, getAsColumn(column));
                }
            }
        }
        else {
            name = column.getTable().getName().toLowerCase();
            //表名必须规范
            if (name.startsWith("t_") || name.startsWith("v_") || name.startsWith("i_")) {
                name = name.substring(2);
            }
            
            //外键关系必须明确
            if (column.getRelation() != Relation.One2One) {
                name += "s";
                addKey(table, name, column);
            }
            else {
                addSingleKey(table, name, column);
            }
        }
        return name;
    }
    
    public static void addSingleKey(Table table, String key, Column col) {
        if (!SINGLE_KEY.containsKey(table)) {
            SINGLE_KEY.put(table, new HashSet<String>());
        }
        SINGLE_KEY.get(table).add(key);
        
        addKey(table, key, col);
    }
    
    public static void addKey(Table table, String key, Column col) {
        if (!KEY_COLUMN_MAP.containsKey(table)) {
            KEY_COLUMN_MAP.put(table, new HashMap<String, Class<?>>());
        }
        Class<?> clazz = null;
        try {
            if (col.getTable() == table) {
                if (col.getFK() != null) {
                    clazz = Class.forName(col.getFK().getModelName());
                }
            }
            else {
                clazz = Class.forName(col.getTable().getModelName());
            }
        }
        catch (ClassNotFoundException e) {
        }
        if (clazz != null) KEY_COLUMN_MAP.get(table).put(key, clazz);

        if (!COLUMN_KEY_MAP.containsKey(table)) {
            COLUMN_KEY_MAP.put(table, new HashMap<Column, String>());
        }
        COLUMN_KEY_MAP.get(table).put(col, key);
    }
    
    public static boolean isSingleKey(Table table, String key) {
        if (!SINGLE_KEY.containsKey(table)) return false;
        return SINGLE_KEY.get(table).contains(key);
    }
    
    public static Class<?> getFKModelClassByKey(Table table, String key) {
        if (!KEY_COLUMN_MAP.containsKey(table)) return null;
        return KEY_COLUMN_MAP.get(table).get(key);
    }
    
    public static String getKey(Table table, Column col, boolean isSureNotThisTableFk) {
        //这种情况，肯定是一个表自己连自己，而且现在要添加的是child，并非parent
        if (isSureNotThisTableFk && col.getTable() == table) {
            Column asColumn = getAsColumn(col);
            if (!COLUMN_KEY_MAP.containsKey(table) || !COLUMN_KEY_MAP.get(table).containsKey(asColumn)) {
                return getKey(table, col, false);
            }
            return COLUMN_KEY_MAP.get(table).get(asColumn);
        }
        else {
            if (!COLUMN_KEY_MAP.containsKey(table) || !COLUMN_KEY_MAP.get(table).containsKey(col)) {
                init(table, col);
            }
            return COLUMN_KEY_MAP.get(table).get(col);
        }
    }

    private static Column getAsColumn(Column col) {
        if (AS_COLUMN == null) AS_COLUMN = new HashMap<Column, Column>();
        if (!AS_COLUMN.containsKey(col)) AS_COLUMN.put(col, col.clone());
        return AS_COLUMN.get(col);
    }
    
}
