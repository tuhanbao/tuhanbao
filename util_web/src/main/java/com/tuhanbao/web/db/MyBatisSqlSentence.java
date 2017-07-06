package com.tuhanbao.web.db;

import java.lang.reflect.Method;

import com.tuhanbao.base.util.db.table.data.DataValue;
import com.tuhanbao.base.util.exception.MyException;

public class MyBatisSqlSentence {
    private String sql;

    private DataValue[] args;

    public MyBatisSqlSentence(String sql, DataValue... args) {
        this.sql = sql;
        this.args = args;
    }

    public String getSql() {
        int length = args == null ? 0 : args.length;
        String sql = this.sql;
        for (int i = 0; i < length; i++) {
            Method method;
            try {
                method = args[i].getClass().getMethod("getValue4DB");
            }
            catch (NoSuchMethodException | SecurityException e) {
                throw MyException.getMyException(e);
            }
            sql = sql.replace("{" + i + "}", "#{_parameter.args[" + i + "], javaType=" + method.getReturnType().getName() + "}");
        }
        return sql;
    }

    public DataValue getArg(int index) {
        // 不检查边界及null，业务自行保证
        return args[index];
    }

    public DataValue[] getArgs() {
        return args;
    }
}
