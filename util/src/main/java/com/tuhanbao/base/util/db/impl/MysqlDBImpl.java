package com.tuhanbao.base.util.db.impl;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tuhanbao.base.util.db.IDBImpl;
import com.tuhanbao.base.util.db.conn.ConnectionManager;
import com.tuhanbao.base.util.db.conn.DBSrc;
import com.tuhanbao.base.util.db.conn.MyConnection;
import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.gm.CMDResult;
import com.tuhanbao.base.util.gm.RuntimeUtil;
import com.tuhanbao.base.util.io.codeGenarator.sqlUtil.DBUtil;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.FileUtil;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.base.util.objutil.TimeUtil;

/**
 * 
 * 这里不用加锁，由于一个链接只可能同时被一个线程使用。
 * @author tuhanbao
 *
 */
public class MysqlDBImpl implements IDBImpl
{
    public static final MysqlDBImpl instance = new MysqlDBImpl();

    private MysqlDBImpl()
    {

    }
    
    public static MysqlDBImpl getInstance() {
    	return instance;
    }
    
    public void backUp(DBSrc src, int maxNum, String dbBackupUrl)
    {
//        String dbBackupUrl = ;
        File file = new File(dbBackupUrl);
        if (!file.exists())
        {
            try
            {
                FileUtil.createDir(file);
            }
            catch (IOException e)
            {
                LogManager.error(new MyException(BaseErrorCode.ERROR, "create db backup url fail : " + dbBackupUrl));
                return;
            }
        }
        
        String url = dbBackupUrl + getUrl() + ".sql";
        StringBuilder sb = new StringBuilder();
        sb.append("mysqldump -u").append(src.getUser()).append(" -p")
                .append(src.getPassword()).append(" ")
                .append(src.getDb_instance()).append(" --default-character-set=utf8 --result-file=").append(url);
        CMDResult result = RuntimeUtil.excuteCmd(sb.toString());
        if (result.getCode() != 0) LogManager.error(new MyException(BaseErrorCode.EXCUTE_CMD_ERROR, sb.toString()));
        else LogManager.info("backup db success!");
        
        //如果备份目录下文件过多，删除早期的文件
        //只保留三天的文件
        int length = file.listFiles().length;
        if (length > maxNum)
        {
            List<String> fileNames = new ArrayList<String>();
            for (String fileName : file.list()) fileNames.add(fileName);
            Collections.sort(fileNames);
            
            for (int i = 0; i < length - maxNum; i++)
            {
                new File(dbBackupUrl + fileNames.get(i)).delete();
            }
        }
    }
    
    private static String getUrl()
    {
        int[] todayYearMonthDayHour = TimeUtil.getTodayYearMonthDayHour();
        int length = todayYearMonthDayHour.length;
        String[] str = new String[length];
        for (int i = 0; i < length; i++)
        {
            if (todayYearMonthDayHour[i] < 10) str[i] = "0" + todayYearMonthDayHour[i];
            else str[i] = "" + todayYearMonthDayHour[i];
        }
        return StringUtil.array2String(str, "");
    }
    
    public void excute(String sql) throws SQLException
    {
    	MyConnection conn = null;
        try
        {
            conn = ConnectionManager.getConnection();
            conn.setAutoCommit(true);
            conn.excute(sql);
        }
        finally
        {
            DBUtil.release(conn);
        }
    }

    /**
     * 无法实现，因为返回的ResultSet，我们根本无法控制业务端什么时候使用完
     * 不知道在什么时候release connection
     * 
     * 除非将rs处理完后转换成map
     * @param sql
     * @return
     */
	public ResultSet executeQuery(String sql) {
		throw new MyException(BaseErrorCode.DB_ERROR, "cannot invoke this method : executeQuery!");
	}
}
