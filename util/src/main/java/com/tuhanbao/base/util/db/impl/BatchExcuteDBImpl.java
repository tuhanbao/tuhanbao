package com.tuhanbao.base.util.db.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tuhanbao.base.util.db.conn.ConnectionManager;
import com.tuhanbao.base.util.db.conn.MyConnection;
import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.io.codeGenarator.sqlUtil.DBUtil;
import com.tuhanbao.base.util.log.LogManager;

/**
 * 批量执行器，
 * close后请销毁此对象
 * @author tuhanbao
 *
 */
public class BatchExcuteDBImpl
{
	private SqlExcutor[] impls;
	
	private List<SqlExcutor> runningExcutor = new ArrayList<SqlExcutor>();
	
	private int size;
	
	private int nowIndex = 0;
	
	private static final int DEFALUT_SIZE = 20;
	
	public BatchExcuteDBImpl()
	{
		this(DEFALUT_SIZE);
	}
	
    public BatchExcuteDBImpl(int size)
    {
    	this.size = size;
    	initExcutors();
    }
    
    public void addBatch(String sql) throws SQLException
    {
    	impls[nowIndex].addBatch(sql);
    	nowIndex++;
    	if (nowIndex == size) nowIndex = 0;
    }
    
    public void excuteBatch() throws SQLException
    {
    	int i = 0;
    	synchronized (runningExcutor) {
    		for (SqlExcutor excutor : impls) {
    			runningExcutor.add(excutor);
    			new Thread(excutor, "sqlExcuter" + i++).start();
    		}
    		impls = null;
		}
	}

	public void initExcutors() {
		//SqlExcuter执行完后会自己释放，这里另起一批新的SqlExcuter即可
    	//频繁调用可能会导致数据库连接不足
		if (impls == null) {
			impls = new SqlExcutor[size];
	        for (int i = 0 ; i < size; i++)
	        {
	        	impls[i] = new SqlExcutor(this);
	        }
		}
	}

	public void waitFinish() throws InterruptedException {
		synchronized (runningExcutor) {
			while (this.runningExcutor.size() > 0)
			{
				runningExcutor.wait();
			}
		}
	}
	
	public void finish(SqlExcutor excutor) {
		synchronized (runningExcutor) {
			this.runningExcutor.remove(excutor);
			runningExcutor.notifyAll();
		}
	}
}

class SqlExcutor implements Runnable {
	private MyConnection conn = null;
	
	private boolean hasSql = false;
	
	private BatchExcuteDBImpl father;
	
	public SqlExcutor(BatchExcuteDBImpl father) {
		this.father = father;
		try
        {
            conn = ConnectionManager.getConnection();
            conn.setAutoCommit(false);
        }
        catch (SQLException e)
        {
            throw new MyException(BaseErrorCode.DB_ERROR, e.getMessage());
        }
	}
	
	public void close() {
		DBUtil.release(conn);
	}
	
    public void addBatch(String sql) throws SQLException
    {
        conn.addBatch(sql);
        hasSql = true;
    }
    
    private void excuteBatch() throws SQLException
    {
    	if (hasSql) {
    		conn.excuteBatch();
    		conn.commit();
    	}
    }
    
//	public synchronized void waitFinish() throws InterruptedException {
//		if (!isUse) {
//			return;
//		}
//		else {
//			this.wait();
//			waitFinish();
//		}
//	}
	
	@Override
	public void run() {
		try {
			excuteBatch();
		} catch (SQLException e) {
			LogManager.error(e);
		}
		finally {
			close();
			father.finish(this);
			hasSql = false;
			conn = null;
		}
	}
}
