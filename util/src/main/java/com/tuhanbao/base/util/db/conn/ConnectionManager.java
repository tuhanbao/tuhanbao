package com.tuhanbao.base.util.db.conn;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.tuhanbao.base.util.config.BaseConfigConstants;
import com.tuhanbao.base.util.config.Config;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.objutil.StringUtil;

public final class ConnectionManager
{
    public static int MIN_SIZE = 1;

    public static int MAX_SIZE = 10;

    public static int DB_OVERDUE_TIME = 30;

    public static String DB_USEUNICODE = "true";
    
    public static String DB_ENCODE = "utf8";
    
    public static boolean HAS_DEFAULT_DB = false;
    
    public static boolean AUTO_CLEAR_CONN = false;
    
    public static DBSrc DEFAULT_DB;
    
    public static int RESULTSET_TYPE = ResultSet.TYPE_SCROLL_SENSITIVE;;

    static
    {
    	Config baseConfig = ConfigManager.getBaseConfig();
    	if (baseConfig != null) {
    		if (baseConfig.containsKey(BaseConfigConstants.DB_MIN_CONN_NUM))
    			MIN_SIZE = baseConfig.getInt(BaseConfigConstants.DB_MIN_CONN_NUM);
    		if (baseConfig.containsKey(BaseConfigConstants.DB_MAX_CONN_NUM))
    			MAX_SIZE = baseConfig.getInt(BaseConfigConstants.DB_MAX_CONN_NUM);
    		if (baseConfig.containsKey(BaseConfigConstants.DB_USEUNICODE))
    			DB_USEUNICODE = baseConfig.getString(BaseConfigConstants.DB_USEUNICODE);
    		if (baseConfig.containsKey(BaseConfigConstants.DB_ENCODE))
    			DB_ENCODE = baseConfig.getString(BaseConfigConstants.DB_ENCODE);
    		if (baseConfig.containsKey(BaseConfigConstants.DB_OVERDUE_TIME))
    			DB_OVERDUE_TIME = baseConfig.getInt(BaseConfigConstants.DB_OVERDUE_TIME);
    		if (baseConfig.containsKey(BaseConfigConstants.AUTO_CLEAR_CONN))
    			AUTO_CLEAR_CONN = baseConfig.getInt(BaseConfigConstants.AUTO_CLEAR_CONN) == 1;
	    	
	    	String resultSetTypeStr = baseConfig.getString(BaseConfigConstants.RESULTSET_TYPE);
	    	if (!StringUtil.isEmpty(resultSetTypeStr)) {
	    		RESULTSET_TYPE = baseConfig.getInt(BaseConfigConstants.RESULTSET_TYPE);
	    	}
	    	//有可能未设置数据库
	    	String driver = baseConfig.getString(BaseConfigConstants.DB_DRIVER);
	    	if (driver != null)
	    	{
	    		String url = baseConfig.getString(BaseConfigConstants.DB_URL);
	    		String user = baseConfig.getString(BaseConfigConstants.DB_USER);
	    		String password = baseConfig.getString(BaseConfigConstants.DB_PASSWORD);
	    		
	    		HAS_DEFAULT_DB = true;
	    		
	    		DEFAULT_DB = new DBSrc(driver, url, user, password);
	    	}
	    	else
	    	{
	    		HAS_DEFAULT_DB = false;
	    	}
    	}
    }
    
    private ConnectionManager()
    {
        
    }

    /**
     * 外部不允许使用
     * 
     * 除非你很清楚什么时候该释放连接
     * @return
     * @throws SQLException
     */
    public static MyConnection getConnection() throws SQLException
    {
        if (!HAS_DEFAULT_DB) return null;
        
        return DEFAULT_DB.getConnection();
    }
    
    public static void setDefaulDBSrc(DBSrc dbSrc) {
    	DEFAULT_DB = dbSrc;
    	if (dbSrc != null) HAS_DEFAULT_DB = true;
    }
}
