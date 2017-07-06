package com.tuhanbao.autotool.mvc.excel;

import java.util.List;
import java.util.Map;

import com.tuhanbao.autotool.mvc.SpringMvcProjectInfo;
import com.tuhanbao.base.chain.Context;
import com.tuhanbao.base.chain.IChainFilter;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportTable;

/**
 * 通过excel模板生成代码的filter模板父类
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
public abstract class ExcelAGCFilter implements IChainFilter {

    protected static final String PROJECT = "project";
    protected static final String CONFIG = "config";
    protected static final String TABLE_CONFIG = "table_config";
    
    //以下为系统内置配置文件，区分大小写。 除这些配置外，均为表或者自定义配置文件
    protected static final String LANGUAGE = "language";
    protected static final String TYPES = "types";
    protected static final String ERROR_CODE = "error_code";
    protected static final String DB = "db";
    protected static final String BASE = "base";
    protected static final String TABLES = "tables";
    protected static final String README = "readme";

    public SpringMvcProjectInfo getProject(Context context) {
        return (SpringMvcProjectInfo)context.get(PROJECT);
    }
    
    public Map<String, String[]> getTableConfigs(Context context) {
        return (Map<String, String[]>)context.get(TABLE_CONFIG);
    }
    
    public List<ImportTable> getTables(Context context) {
        return (List<ImportTable>)context.get(TABLES);
    }

    public String[][] getConfig(Context context, String key) {
        return ((Map<String, String[][]>)context.get(CONFIG)).get(key);
    }

    public String[][] removeConfig(Context context, String key) {
        return ((Map<String, String[][]>)context.get(CONFIG)).remove(key);
    }

}
