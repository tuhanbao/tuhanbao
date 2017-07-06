package com.tuhanbao.web.filter;

import java.util.Collection;

import com.tuhanbao.base.dataservice.filter.FilterItem;
import com.tuhanbao.base.dataservice.filter.IFilterItem;
import com.tuhanbao.base.dataservice.filter.operator.Operator;
import com.tuhanbao.base.util.db.IField;
import com.tuhanbao.base.util.db.table.Column;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;
import com.tuhanbao.base.util.objutil.StringUtil;

/**
 * 过滤表达式 常见的过滤表达式都是二元比较，但是也有类似与between，in等三元的比较符
 * 
 * @author tuhanbao
 *
 */
public class MyBatisFilterItem implements IFilterItem {

    private IFilterItem filterItem;
    
    public MyBatisFilterItem(IFilterItem item) {
        if (item == null) throw new MyException("Cannot init MyBatisFilterItem item with a null object.");
        this.filterItem = item;
    }

    private Object getArgForMybatis(Object arg, int index) {
        if (arg instanceof IFilterField) return ((IFilterField)arg).getSqlName();
        else if (arg instanceof IField) return arg;
        else return "#{item.filterItem.arg" + index + "}";
    }

    /**
     * 给mapper.xml文件使用
     * @return
     */
    public String getMyBatisSql() {
        if (this.filterItem instanceof FilterItem) {
            FilterItem item = (FilterItem)this.filterItem;
            StringBuilder sb = new StringBuilder();
            Operator operator = item.getOperator();
            sb.append(getArgForMybatis(item.getArg1(), 1)).append(operator);
            if (Operator.BETWEEN == operator) {
                sb.append(getArgForMybatis(item.getArg2(), 2)).append(" and ").append(getArgForMybatis(item.getArg3(), 3));
            }
            else if (Operator.IN == operator || Operator.NOT_IN == operator) {
            }
            else if (Operator.IS_NULL != operator && Operator.NOT_NULL != operator) {
                sb.append(getArgForMybatis(item.getArg2(), 2));
            }
            return sb.toString();
        }
        else {
            return toString();
        }
    }

    public String toString() {
        if (this.filterItem instanceof FilterItem) {
            FilterItem item = (FilterItem)this.filterItem;
            StringBuilder sb = new StringBuilder(item.getArg1().toString());
            Operator operator = item.getOperator();
            sb.append(operator);
            boolean isArgStr = isArgStrCol(item.getArg1());
            if (Operator.BETWEEN == operator) {
                sb.append(getArg(item.getArg2(), isArgStr)).append(" and ").append(getArg(item.getArg3(), isArgStr));
            }
            else if (Operator.IN == operator || Operator.NOT_IN == operator) {
                if (isArgStr) {
                    sb.append("('").append(StringUtil.array2String((Collection<?>)item.getArg2(), "','")).append("')");
                }
                else {
                    sb.append(StringUtil.array2String((Collection<?>)item.getArg2()));
                }
            }
            else if (Operator.IS_NULL != operator && Operator.NOT_NULL != operator) {
                sb.append(getArg(item.getArg2(), isArgStr));
            }
            return sb.toString();
        }
        else {
            return this.filterItem.toString();
        }
    }

    @Override
    public Collection<Object> listValue() {
        return this.filterItem.listValue();
    }

    @Override
    public boolean isList() {
        return this.filterItem.isList();
    } 
    
    private Object getArg(Object arg, boolean isArgStr) {
        if (isArgStr) {
            return '\'' + arg.toString() + '\'';
        }
        
        return arg;
    }

    private boolean isArgStrCol(Object arg1) {
        if (arg1 instanceof Column) {
            Column col = (Column)arg1;
            if (col.getDataType().getName().equals(DataType.STRING.getName())) {
                return true;
            }
        }
        else if (arg1 instanceof FilterField) {
            Column col = ((FilterField)arg1).getColumn();
            if (col.getDataType().getName().equals(DataType.STRING.getName())) {
                return true;
            }
        }
        
        return false;
    }
}
