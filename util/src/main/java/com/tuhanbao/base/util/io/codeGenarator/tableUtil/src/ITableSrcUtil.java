package com.tuhanbao.base.util.io.codeGenarator.tableUtil.src;

import java.util.List;

import com.tuhanbao.base.util.db.conn.DBSrc;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportTable;

public interface ITableSrcUtil {
    List<ImportTable> getTables(DBSrc src) throws Exception;
    
    ImportTable getTable(DBSrc src, String tableName) throws Exception;
}
