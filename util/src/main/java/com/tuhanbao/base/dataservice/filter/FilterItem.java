package com.tuhanbao.base.dataservice.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.tuhanbao.base.dataservice.filter.operator.Operator;
import com.tuhanbao.base.util.db.IField;
import com.tuhanbao.base.util.db.table.DataValueFactory;
import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;

/**
 * 过滤表达式 常见的过滤表达式都是二元比较，但是也有类似与between，in等三元的比较符
 * 
 * @author tuhanbao
 *
 */
public class FilterItem implements IFilterItem {
    protected Object arg1, arg2, arg3;

    protected Operator operator;

    public FilterItem() {
        
    }

    /**
     * args必须为DataValue或Column
     * 
     * @param operator
     * @param args
     */
    protected FilterItem(Operator operator, Object... args) {
        this.operator = operator;
        if (args == null || args.length == 0 || args[0] == null) throw new MyException(BaseErrorCode.ILLEGAL_ARGUMENT, "FilterItem args is null");
        this.arg1 = args[0];

        this.arg2 = args.length > 1 ? args[1] : null;
        if (operator == Operator.BETWEEN) {
            if (args.length < 3 || args[2] == null) throw new MyException(BaseErrorCode.ILLEGAL_ARGUMENT, "FilterItem args is null");
            this.arg3 = args[2];
        }

        changeValue();
    }

    /**
     * 将非dataValue的转换为DataValue
     * 
     * @return
     */
    private void changeValue() {
        // 如果 arg1是column，只能修改arg2和arg3的值
        if (arg1 instanceof IField) {
            DataType dataType = ((IField)arg1).getDataType();
            // 暂时只有这两个需要处理
            if (dataType == DataType.BOOLEAN || dataType == DataType.DATE) {
                if (!(arg2 instanceof IField)) {
                    arg2 = toDataValue(dataType, arg2);
                }
                if (!(arg3 instanceof IField)) {
                    arg3 = toDataValue(dataType, arg3);
                }
            }
        }

    }

    private static Object toDataValue(DataType dataType, Object value) {
        if (value == null) return null;
        if (value instanceof Collection<?>) {
            Collection<?> col = (Collection<?>)value;
            List<Object> list = new ArrayList<Object>();
            for (Object item : col) {
                list.add(DataValueFactory.toDataValue(dataType, item));
            }
            return list;
        }

        return DataValueFactory.toDataValue(dataType, value);
    }

    public Object getArg1() {
        return arg1;
    }

    public Object getArg2() {
        return arg2;
    }

    public Object getArg3() {
        return arg3;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public boolean isList() {
        return Operator.IN == operator || Operator.NOT_IN == operator;
    }

    /**
     * isList = true时请重写
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public Collection<Object> listValue() {
        return (Collection<Object>)arg2;
    }
}
