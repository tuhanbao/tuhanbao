package com.tuhanbao.base.util.io.codeGenarator.sqlUtil.excutor;

import java.io.IOException;
import java.util.List;

import com.tuhanbao.base.util.db.conn.DBSrc;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportTable;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.XlsTable;

public abstract class DBExcutor
{
    public abstract String getSql(List<XlsTable> tables) throws IOException;
    
    //因为需要对比老数据库，所以这里需要传入dbSrc
    public abstract String getSql(ImportTable table, DBSrc dbSrc) throws IOException;
}
