package com.tuhanbao.base.util.io.codeGenarator.tableUtil;


/**
 * 数据库类型
 * 
 * @author tuhanbao
 *
 */
public enum DBType
{
    MYSQL(1),
    
    ORACLE(2),
    
    DB2(3);
    
    private int value;
    
    private DBType(int value)
    {
        this.value = value;
    }
    
    public static DBType getDBType(int value)
    {
        for (DBType temp : DBType.values())
        {
            if (temp.value == value)
            {
                return temp;
            }
        }

        return null;
    }
}
