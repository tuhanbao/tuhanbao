package com.tuhanbao.base.util.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tuhanbao.base.util.db.conn.ConnectionManager;
import com.tuhanbao.base.util.db.conn.DBSrc;
import com.tuhanbao.base.util.db.conn.MyConnection;
import com.tuhanbao.base.util.log.LogManager;

public abstract class BeanParser<T> {
	private DBSrc dbSrc;
	
	public BeanParser() {
	}
	
	public BeanParser(DBSrc dbSrc) {
		this.dbSrc = dbSrc;
	}
	
	public DBSrc getDbSrc() {
		return dbSrc;
	}

	public void setDbSrc(DBSrc dbSrc) {
		this.dbSrc = dbSrc;
	}

	public abstract T parser(ResultSet rs) throws SQLException;
    
    public final List<T> executeQuery(String sql) throws SQLException
    {
    	MyConnection conn = null;
    	if (dbSrc == null) conn = ConnectionManager.getConnection();
    	else conn = dbSrc.getConnection();
    	
    	List<T> list = new ArrayList<T>();
    	int num = 0;
    	try
    	{
			ResultSet rs = conn.executeQuery(sql);
			while (rs.next())
			{
				num++;
				T t = this.parser(rs);
				if (t == null) continue;
				list.add(t);
			}
	    	rs.close();
    	}
    	finally{
    		if (conn != null) conn.release();
    	}
    	LogManager.debug(sql);
    	LogManager.debug("size : " + num + "  list size : " + list.size());
    	return list;
    }
}
