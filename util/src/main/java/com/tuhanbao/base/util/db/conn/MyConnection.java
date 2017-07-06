package com.tuhanbao.base.util.db.conn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.base.util.objutil.TimeUtil;

public class MyConnection
{
    private Connection connection;
    
    private Statement st;

    private boolean used;
    
    private long lastConnectTime;
    
    private boolean isAutoCommit = true;
    
    private DBSrc dbSrc;
    
    private int no;

    public MyConnection(Connection c, DBSrc dbSrc, int no) throws SQLException
    {
        this.connection = c;
        this.dbSrc = dbSrc;
        this.no = no;
        this.st = this.connection.createStatement(dbSrc.getResultSetType(), ResultSet.CONCUR_UPDATABLE);
        refreshLastConnTime();
    }
    
    public synchronized boolean isUsed()
    {
        return this.used;
    }

    public boolean isActive()
    {
        try
        {
            return this.connection != null && !this.connection.isClosed();
        }
        catch (Exception localException)
        {
        }
        return false;
    }

    public boolean isDestoryed() throws SQLException
    {
        return this.connection.isClosed();
    }

    public void setAutoCommit(boolean b) throws SQLException
    {
    	this.isAutoCommit = b;
    	if (this.connection != null) this.connection.setAutoCommit(b);
    }

    public void use()
    {
        this.used = true;
    }

    public void commit() throws SQLException
    {
        if (this.connection != null) this.connection.commit();
        else LogManager.warn("why commit a null connection!!!");
    }

    public void rollback() throws SQLException
    {
    	if (this.connection != null) this.connection.rollback();
    	else LogManager.warn("why roll back a null connection!!!");
    }

    public void release()
    {
        this.used = false;
        dbSrc.notifyNext();
    }

    public boolean destroy()
    {
        try
        {
        	if (this.st != null) this.st.close();
        	if (this.connection != null) this.connection.close();
            return true;
        }
        catch (SQLException e)
        {
            LogManager.error(e);
            return false;
        }
        finally {
        	this.st = null;
        	this.connection = null;
        }
    }
    
    public ResultSet executeQuery(String sql) throws SQLException
    {
        checkConn();
        refreshLastConnTime();
        return this.st.executeQuery(sql);
    }
    
    public int executeUpdate(String sql) throws SQLException
    {
        checkConn();
        refreshLastConnTime();
        return this.st.executeUpdate(sql);
    }
    
    public void addBatch(String sql) throws SQLException
    {
    	checkConn();
        if (!StringUtil.isEmpty(sql))
        {
            st.addBatch(sql);
        }
    }
    
    public void excuteBatch() throws SQLException
    {
    	if (ConfigManager.isDebug()) return;
    	//批量执行是不允许重连的，如果有重连需要报错
	    // checkConn();
    	refreshLastConnTime();
    	st.executeBatch();
    }
    
    public void excute(String sql) throws SQLException
    {
    	if (ConfigManager.isDebug()) return;
    	checkConn();
    	refreshLastConnTime();
        st.execute(sql);
    }

    private void checkConn() throws SQLException
    {
        if (!this.isActive() || isOverdue())
        {
            recreateConn();
        }
    }

    private void recreateConn() throws SQLException
    {
        //先摧毁现有链接，再重建
        destroy();
        this.connection = dbSrc.createConnection();
        this.connection.setAutoCommit(this.isAutoCommit);
        this.st = this.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        refreshLastConnTime();
    }

    private void refreshLastConnTime()
    {
        this.lastConnectTime = TimeUtil.now();
    }

    public boolean isOverdue()
    {
        //小于0表示不检查
        if (ConnectionManager.DB_OVERDUE_TIME < 0) return false;
        return !this.used && TimeUtil.isOverdue(lastConnectTime, ConnectionManager.DB_OVERDUE_TIME, TimeUnit.MINUTES);
    }
    
    public int getNo() {
    	return this.no;
    }

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		checkConn();
        refreshLastConnTime();
		return this.connection.prepareStatement(sql);
	}
}
