package com.tuhanbao.base.util.db.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DBType;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.thread.TimerEvent;
import com.tuhanbao.base.util.thread.TimerThreadFactory;

public final class DBSrc
{
	private Properties properties = new Properties();
    
	private String driver;
    
	private String url;
	
	private String user;
	
	private String password;
	
	private String db_instance;

	private final List<MyConnection> connections = new ArrayList<MyConnection>();
    
    private DBType dbType;
    
    private int resultSetType;
    
    public DBSrc(String driver, String url, String user, String password) 
    {
    	this(driver, url, user, password, ConnectionManager.MIN_SIZE);
    }

    public DBSrc(String driver, String url, String user, String password, int size) 
    {
        this(driver, url, user, password, size, ConnectionManager.RESULTSET_TYPE);
    }

	public DBSrc(String driver, String url, String user, String password, int minSize, int resultSetType) 
    {
    	this.driver = driver;
    	this.url = url;
    	this.user = user;
    	this.password = password;
    	this.resultSetType = resultSetType;
    	properties.setProperty(Constants.USER, user);
    	properties.setProperty(Constants.PASSWORD, password);
    	properties.setProperty(Constants.USE_UNICODE, ConnectionManager.DB_USEUNICODE);
    	properties.setProperty(Constants.CHARACTER_ENCODING, ConnectionManager.DB_ENCODE);
    	if (driver.contains("oracle")) {
    		this.dbType = DBType.ORACLE;
    		int start = this.url.indexOf("service_name=");
    		int end = this.url.indexOf(")", start + "service_name=".length());
    		this.db_instance = this.url.substring(start + "service_name=".length(), end);
    	} 
    	else {
    		this.dbType = DBType.MYSQL;
    		int start = this.url.lastIndexOf("/");
    		this.db_instance = this.url.substring(start + 1);
    	}
    	
    	init(minSize);
    	
    	if (ConnectionManager.AUTO_CLEAR_CONN) {
	    	TimerThreadFactory.addTimerEvent(new TimerEvent() {
				@Override
				protected void run() throws Exception {
					clear();
				}
			}, ConnectionManager.DB_OVERDUE_TIME, TimeUnit.MINUTES);
    	}
    }
    
    private void init(int size)
    {
        try
        {
            Class.forName(driver);
            //只创建一个连接
            createMyConnection(size);
        }
        catch (Exception e)
        {
            LogManager.error(e);
            System.exit(0);
        }
    }
    
    public int size()
    {
        return connections.size();
    }

    private synchronized MyConnection createMyConnection() throws SQLException
    {
        Connection conn = createConnection();
        MyConnection esConn = new MyConnection(conn, this, connections.size() + 1);
        connections.add(esConn);
        return esConn;
    }

    /**
     * 严禁私自调用
     * @return
     * @throws SQLException
     */
    protected Connection createConnection() throws SQLException
    {
        Connection conn = DriverManager.getConnection(url, this.properties);
        conn.setAutoCommit(true);
        return conn;
    }

    private void createMyConnection(int num)
    {
        int failNum = 0;
        for (int i = 0; i < num; i++)
        {
            try
            {
                createMyConnection();
            }
            catch (SQLException e)
            {
                LogManager.error(e);
                failNum++;
            }
        }
        
        //一个都没创建成功
        if (num > 0 && failNum == num) throw new MyException(BaseErrorCode.DB_CREATE_CONNECTION_ERROR);
    }

    /**
     * 外部不允许使用
     * 
     * 除非你很清楚什么时候该释放连接
     * @return
     * @throws SQLException
     */
    public synchronized MyConnection getConnection() throws SQLException
    {
        for (MyConnection conn : this.connections)
        {
            if (!conn.isUsed())
            {
                conn.use();
                //每条出去的连接都设成为自动提交
                conn.setAutoCommit(true);
                return conn;
            }
        }
        if (this.size() >= ConnectionManager.MAX_SIZE)
        {
            LogManager.warn("db connection is full: " + this.url);
            waitForConnection();
            return getConnection();
        }
        else
        {
            MyConnection conn = this.createMyConnection();
            conn.use();
            
            //每条出去的连接都设成为自动提交
            conn.setAutoCommit(true);
            return conn;
        }
        
    }
    
    /**
     * 清理闲置连接
     */
    private void clear() {
    	for (MyConnection conn : this.connections) {
    		if (conn.isOverdue()) {
    			conn.destroy();
    		}
    	}
    }

    private void waitForConnection()
    {
        try
        {
            this.wait();
        }
        catch (InterruptedException e)
        {
        }
    }

    public synchronized void notifyNext()
    {
        this.notify();
    }
    
    public String toString() {
    	return this.url;
    }
    
    public DBType getDBType() {
    	return this.dbType;
    }
    
    public String getDb_instance() {
		return db_instance;
	}

	public String getDriver() {
		return driver;
	}

	public String getUrl() {
		return url;
	}

	public DBType getDbType() {
		return dbType;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}
    
    public int getResultSetType() {
		return resultSetType;
	}

	public void setResultSetType(int resultSetType) {
		this.resultSetType = resultSetType;
	}
}
