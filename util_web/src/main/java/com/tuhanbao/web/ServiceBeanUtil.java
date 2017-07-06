package com.tuhanbao.web;

import org.apache.ibatis.type.JdbcType;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.base.util.db.table.Column;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;

public class ServiceBeanUtil
{
    public static String getMyBatisColumnValueStr(ServiceBean serviceBean) {
        StringBuilder sb = new StringBuilder();
        for (Column col : serviceBean.getTable().getColumns()) {
            sb.append("#{").append(col.getName()).append(",jdbcType=")
                .append(getJDBCType(col.getDataType())).append("}").append(Constants.COMMA);
        }
        return sb.substring(0, sb.length() - 1);
    }
    
    public static String getMyBatisUpdateValueStr(ServiceBean serviceBean) {
        StringBuilder sb = new StringBuilder();
        for (Column col : serviceBean.getTable().getColumns()) {
            //主键是不允许更新的
            if (col == serviceBean.getTable().getPK()) continue;
            sb.append(col.getName()).append(" = #{model.").append(col.getName()).append(",jdbcType=")
                .append(getJDBCType(col.getDataType())).append("}").append(Constants.COMMA);
        }
        return sb.substring(0, sb.length() - 1);
    }
    
    public static String getMyBatisUpdateValueStrSelected(ServiceBean serviceBean) {
        StringBuilder sb = new StringBuilder();
        for (Column col : serviceBean.getTable().getColumns()) {
            //主键是不允许更新的
            if (col == serviceBean.getTable().getPK()) continue;
            //没有值的不需要更新
            if (serviceBean.getValue(col) == null) continue;
            sb.append(col.getName()).append(" = #{model.").append(col.getName()).append(",jdbcType=")
                .append(getJDBCType(col.getDataType())).append("}").append(Constants.COMMA);
        }
        return sb.substring(0, sb.length() - 1);
    }
    
    private static JdbcType getJDBCType(DataType dataType) {
        if (DataType.STRING.getName().equals(dataType.getName())) return JdbcType.VARCHAR;
        else if (dataType == DataType.BIGDEECIMAL) return JdbcType.DECIMAL;
        else if (dataType == DataType.INT) return JdbcType.INTEGER;
        else if (dataType == DataType.LONG) return JdbcType.BIGINT;
        else if (dataType == DataType.OBJECT) return JdbcType.OTHER;
        else if (dataType == DataType.DATE) return JdbcType.TIMESTAMP;
        else return JdbcType.valueOf(dataType.name());
    }
}
