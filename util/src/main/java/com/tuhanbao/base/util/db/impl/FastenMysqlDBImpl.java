package com.tuhanbao.base.util.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.tuhanbao.base.util.db.IDBImpl;
import com.tuhanbao.base.util.db.conn.ConnectionManager;
import com.tuhanbao.base.util.db.conn.MyConnection;
import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.io.codeGenarator.sqlUtil.DBUtil;

/**
 * 固定的一个数据库连接，长期使用，不会断开
 * 
 * 临时使用数据库勿使用此工具类
 * 
 * 此类不能支持多线程操作....切记
 * @author tuhanbao
 *
 */
public class FastenMysqlDBImpl implements IDBImpl
{
    private MyConnection conn = null;

    public FastenMysqlDBImpl()
    {
        try
        {
            conn = ConnectionManager.getConnection();
        }
        catch (SQLException e)
        {
            throw new MyException(BaseErrorCode.DB_ERROR, e.getMessage());
        }
    }
    
    public void close()
    {
        DBUtil.release(conn);
    }
    
    public void addBatch(String sql) throws SQLException
    {
        conn.addBatch(sql);
    }
    
    public void excuteBatch() throws SQLException
    {
        conn.excuteBatch();
    }

	public ResultSet executeQuery(String sql) throws SQLException {
		return conn.executeQuery(sql);
	}

	public void excute(String sql) throws SQLException {
		conn.excute(sql);
	}
}
