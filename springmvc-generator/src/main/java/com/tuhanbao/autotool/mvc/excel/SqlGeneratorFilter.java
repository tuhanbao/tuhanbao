package com.tuhanbao.autotool.mvc.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.tuhanbao.autotool.mvc.ModuleManager;
import com.tuhanbao.autotool.mvc.ProjectConfig;
import com.tuhanbao.autotool.mvc.SpringMvcProjectInfo;
import com.tuhanbao.base.chain.Context;
import com.tuhanbao.base.chain.FilterAnnotation;
import com.tuhanbao.base.chain.event.CreateFileEvent;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.config.ConfigPattern;
import com.tuhanbao.base.util.db.conn.DBSrc;
import com.tuhanbao.base.util.io.codeGenarator.sqlUtil.DBUtil;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportTable;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.FileUtil;

@FilterAnnotation("sql")
public class SqlGeneratorFilter extends ExcelAGCFilter {

    @Override
    public void filter(Context context) {
        try {
            //生成sql语句
            if (ProjectConfig.NEED_SYNC_DB) {
                for (ConfigPattern cp : ConfigManager.getAllConfigPattern()) {
                    Map<String, StringBuilder> moduleSb = new HashMap<String, StringBuilder>();
                    for (String module : ModuleManager.getAllModules().keySet()) {
                        moduleSb.put(module, new StringBuilder());
                    }
                    
                    Map<DBSrc, List<ImportTable>> newTables = new HashMap<DBSrc, List<ImportTable>>();
                    for (ImportTable table : getTables(context)) {
                        String module = table.getModule();
                        DBSrc dbSrc = ModuleManager.getDBSrc(cp, module);
                        if (!newTables.containsKey(dbSrc)) newTables.put(dbSrc, new ArrayList<ImportTable>());
                        newTables.get(dbSrc).add(table);
                        moduleSb.get(module).append(DBUtil.getSql(table, dbSrc));
                    }
                    
                    //删除多余的表
                    for (Entry<DBSrc, List<ImportTable>> entry : newTables.entrySet()) {
                        List<ImportTable> oldTables = DBUtil.getTables(entry.getKey());
                        
                        A : for (ImportTable oldTable : oldTables) {
                            for (ImportTable newTable : entry.getValue()) {
                                if (oldTable.getTableName().equals(newTable.getTableName())) {
                                    continue A;
                                }
                            }
                            
                            moduleSb.get(ModuleManager.getModule(cp, entry.getKey())).append(DBUtil.getDropTableSql(oldTable, entry.getKey().getDBType()));
                        }
                    }
                    
                    SpringMvcProjectInfo project = this.getProject(context);
                    for (Entry<String, StringBuilder> entry : moduleSb.entrySet()) {
                        context.addEvent(new CreateFileEvent(getSqlName(project, cp, entry.getKey()), entry.getValue().toString()));
                    }
                }
            }
        }
        catch (Exception e) {
            LogManager.error(e);
        }
    }

    private String getSqlName(SpringMvcProjectInfo project, ConfigPattern cp, String module) {
        return FileUtil.appendPath(project.getRootPath(), project.getResUrl(), "init_" + module + ".sql");
    }

}
