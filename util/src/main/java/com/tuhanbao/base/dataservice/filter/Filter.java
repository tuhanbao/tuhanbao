package com.tuhanbao.base.dataservice.filter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.tuhanbao.base.dataservice.IDataGroup;
import com.tuhanbao.base.dataservice.filter.operator.Operator;
import com.tuhanbao.base.dataservice.filter.page.Page;
import com.tuhanbao.base.util.db.IField;

/**
 * 过滤条件
 * 
 * @author Administrator
 *
 */
public class Filter
{
    private List<IFilterItem> items;

    private List<OrderField> orderFields;

    private Page page;
    
    private boolean distinct = false;
    
    private IDataGroup<?> dataGroup;

	public Filter() {
    }
	
	public Filter(IDataGroup<?> dataGroup) {
		this.dataGroup = dataGroup;
    }
    
    /**
     * arg2 为list，代表in,
     * arg2 位二维数组，代表between
     * 其余默认为等于
     * 
     * @param arg1
     * @param arg2
     */
    public Filter andFilter(Object ... args)
    {
    	return this.andFilter(getDefaultOperator(args), args);
    }
    
    public Filter orFilter(Object ... args)
    {
        return this.orFilter(getDefaultOperator(args), args);
    }

	private Operator getDefaultOperator(Object ... args) {
		if (args.length == 2) {
		    if (args[1] == null) return Operator.IS_NULL;
			if (args[1] instanceof Collection) {
			    if (((Collection<?>)args[1]).isEmpty()) {
			        return Operator.IS_NULL;
			    }
			    else return Operator.IN;
			}
			if (args[1].getClass().isArray()) {
				List<Object> list = new ArrayList<Object>();
				int len = Array.getLength(args[1]);
				if (len == 0) return Operator.IS_NULL;
	            for (int i = 0; i < len; ++i) {
	                Object item = Array.get(args[1], i);
	                list.add(item);
	            }
				args[1] = list;
				return Operator.IN;
			}
		}
    	else if (args.length == 3) return Operator.BETWEEN;
    	else if (args.length == 1) return Operator.IS_NULL;
    	else if (args.length > 3) {
    		List<Object> list = new ArrayList<Object>();
			for (int i = 1; i < args.length; i++) {
				list.add(args[i]);
			}
			args[1] = list;
			return Operator.IN;
    	}
		return Operator.EQUAL;
	}
    
    public Filter andFilter(Operator operator, Object ... args)
    {
    	addLogicType(LogicType.AND);
		return addItem(new FilterItem(operator, args));
    }
    
    public Filter orFilter(Operator operator, Object ... args)
    {
    	addLogicType(LogicType.OR);
		return addItem(new FilterItem(operator, args));
    }
    
    public Filter addOrderField(IField field) {
    	return this.addOrderField(field, false);
    }
    
    public Filter addOrderField(IField field, boolean isDesc) {
    	if (orderFields == null) orderFields = new ArrayList<OrderField>();
    	orderFields.add(new OrderField(field, isDesc));
    	
    	return this;
    }
    
    public List<OrderField> getOrderFields() {
        return orderFields;
    }

    public Filter addLeftParenthese()
    {
    	return addItem(ParentheseItem.LEFT_PARENTHESE);
    }
    
    public Filter andLeftParenthese()
    {
        addLogicType(LogicType.AND);
        return addLeftParenthese();
    }
    
    public Filter orLeftParenthese()
    {
        addLogicType(LogicType.OR);
        return addLeftParenthese();
    }
    
    public Filter addRightParenthese()
    {
    	return addItem(ParentheseItem.RIGHT_PARENTHESE);
    }

	private void addLogicType(LogicType logicType) {
		int size = getSize();
		if (size >= 1 && items.get(size - 1) != ParentheseItem.LEFT_PARENTHESE) addItem(logicType);
	}

	private int getSize() {
		if (items == null) return 0;
		return items.size();
	}
    
    private Filter addItem(IFilterItem item) {
    	if (item == null) return this;
    	if (items == null) items = new ArrayList<IFilterItem>();
    	items.add(item);
    	return this;
    }
    
    public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}
	
	public List<IFilterItem> getItems() {
		return items;
	}
	
	public IDataGroup<?> getDataGroup() {
		return this.dataGroup;
	}

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }
}
