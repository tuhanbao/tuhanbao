package com.tuhanbao.autotool.mvc.excel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.tuhanbao.Constants;
import com.tuhanbao.autotool.mvc.J2EETable;
import com.tuhanbao.autotool.mvc.ModuleManager;
import com.tuhanbao.base.chain.Context;
import com.tuhanbao.base.chain.FilterAnnotation;
import com.tuhanbao.base.util.db.conn.DBSrc;
import com.tuhanbao.base.util.db.table.CacheType;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.EnumClassInfo;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.IEnumType;
import com.tuhanbao.base.util.io.codeGenarator.codeUtil.Xls2CodeUtil;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DBType;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.EnumManager;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportTable;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.TableConfig;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.TableManager;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.src.TableSrcUtilFactory;
import com.tuhanbao.base.util.objutil.ArrayUtil;
import com.tuhanbao.base.util.objutil.StringUtil;

@FilterAnnotation("db")
public class DBGeneratorFilter extends ExcelAGCFilter {

    @Override
    public void filter(Context context) {
        Map<String, String[][]> dbProperties = removeAllConfig(context, DB);
        
        for (Entry<String, String[][]> entry : dbProperties.entrySet()) {
            String[][] arrays = entry.getValue();
            String name = entry.getKey();
            int length = arrays.length;
            for (int i = 1; i < length; i++) {
                String[] array = arrays[i];
                String module = null;
                if (!StringUtil.isEmpty(array[0])) {
                    module = array[0];
                }
                DBSrc src = new DBSrc(getDriver(array[1]), array[2], array[3], array[4], 0);
                ModuleManager.addModule(getConfigPattern(name), module, src);
            }
        }
        List<ImportTable> tables = initTables(context);
        Collections.sort(tables);
        context.putAttr(TABLES, tables);
    }
    
    private List<ImportTable> initTables(Context context) {
        List<ImportTable> list = new ArrayList<>();
        Map<String, String[]> tableConfigs = getTableConfigs(context);
        for (String tableName : tableConfigs.keySet()) {
            String[][] arrays = removeConfig(context, tableName);
            ImportTable table = getTable(tableName, tableConfigs.get(tableName), arrays);
            list.add(table);
            //必须先将table放入tableManager，不然fk到时候可能找不到
            TableManager.addTable(table);
        }
        
        //这里需要初始化一下所有的table外键
        for (ImportTable table : list) {
            List<ImportColumn> cols = table.getColumns();
            for (ImportColumn ic : cols) {
                ic.getFkColumn();
            }
        }
        return list;
    }
    
    private ImportTable getTable(String tableName, String[] tableConfig, String[][] arrays) {
        //tableConfig
        //#表名 中文描述    缓存(NOT/ALL/AUTO)默认为NO   序列号（oracle数据库可配置，非必填，填autocreate，会根据表名自动生成序列）   默认排序字段（可不填）
        //T_CRAWLER_CLASS     NO  autocreate  
        String comment = ArrayUtil.indexOf(tableConfig, 1);
        String module = ArrayUtil.indexOf(tableConfig, 2);
        CacheType cacheType = CacheType.getCacheType(ArrayUtil.indexOf(tableConfig, 3));
        String seq = ArrayUtil.indexOf(tableConfig, 4);
        String orderCol = ArrayUtil.indexOf(tableConfig, 5);
        DBType dbType = ModuleManager.getDBSrc(module).getDbType();

        TableConfig tcfg = new TableConfig();
        tcfg.setCacheType(cacheType);
        tcfg.setDefaultOrderColName(orderCol);
        if (dbType == DBType.ORACLE) {
            tcfg.setSequence(getSeqName(seq, tableName));
        }
        else if (dbType == DBType.MYSQL) {
            tcfg.setAutoIncrement(Constants.AUTOCREATE.equalsIgnoreCase(seq));
        }
        ImportTable table = new J2EETable(tableName, module, tcfg);
        initColumns(table, arrays, dbType);
        table.setComment(comment);
        return table;
    }
    
    private static String getSeqName(String seqName, String tableName) {
        if (Constants.AUTOCREATE.equalsIgnoreCase(seqName)) {
            tableName = tableName.toUpperCase();
            if (tableName.startsWith("T_") || tableName.startsWith("V_")) tableName = tableName.substring(2);
            return "SEQ_" + tableName;
        }
        else {
            return seqName;
        }
    }
    
    private static String getDriver(String dbType) {
        if ("oracle".equalsIgnoreCase(dbType)) {
            return "oracle.jdbc.driver.OracleDriver";
        }
        return "com.mysql.jdbc.Driver";
    }
    
    private void initColumns(ImportTable table, String[][] arrays, DBType dbType) {
        int length = arrays.length;
        //首行是列标
        for (int i = 1; i < length; i++) {
            String[] array = arrays[i];
            if (Xls2CodeUtil.isEmptyLine(array)) continue;
            ImportColumn column = getColumn(table, array, dbType);
            if (column.isPK()) table.setPK(column);
            else table.addColumn(column);
        }
    }
    
    private static ImportColumn getColumn(ImportTable table, String[] array, DBType dbType) {
        String colName = array[0];
        String dataType = array[1];
        String dataLengthStr = ArrayUtil.indexOf(array, 2);
        String canFilterStr = ArrayUtil.indexOf(array, 3);
        long dataLength = 0;
        
        if (!StringUtil.isEmpty(dataLengthStr)) dataLength = Long.valueOf(dataLengthStr);
        
        IEnumType enumInfo = EnumManager.getEnum(dataType);
        ImportColumn col = null;
        if (enumInfo != null) {
            int enumDt = enumInfo.getType();
            if (enumDt == EnumClassInfo.INT) {
                dataType = "int";
                if (dataLength == 0) dataLength = 4;
            }
            else {
                dataType = "String";
                if (dataLength == 0) dataLength = 63;
            }
            
            col = new ImportColumn(table, colName, TableSrcUtilFactory.getDBDataType(dataType, dbType), dataLength);
            col.setEnumInfo(enumInfo);
        }
        else {
            DataType dt = DataType.valueOf(dataType.toUpperCase());
            col = new ImportColumn(table, colName, dt, TableSrcUtilFactory.getDBDataType(dataType, dbType), dataLength);
        }
        String pkfkInfo = ArrayUtil.indexOf(array, 4);
        if (!StringUtil.isEmpty(pkfkInfo)) {
            if (pkfkInfo.startsWith("PK")) {
                col.setPK(true);
            } 
            else if (pkfkInfo.startsWith("FK")) {
                int start = pkfkInfo.indexOf(Constants.LEFT_PARENTHESE);
                int end = pkfkInfo.indexOf(Constants.RIGHT_PARENTHESE);
                String FK = pkfkInfo.substring(start + 1, end);
                col.setFK(FK);
            }
        }
        col.setDefaultValue(ArrayUtil.indexOf(array, 5));
        col.setComment(ArrayUtil.indexOf(array, 6));
        col.setCanFilter("true".equalsIgnoreCase(canFilterStr) || "1".equalsIgnoreCase(canFilterStr));
        return col;
    }

}
