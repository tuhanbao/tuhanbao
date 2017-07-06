package com.tuhanbao.base.util.io.codeGenarator.tableUtil;

import com.tuhanbao.base.util.io.codeGenarator.sqlUtil.OrderType;


public class OrderColumn extends ImportColumn
{
    private OrderType orderType;
    
    public OrderColumn(ImportTable table, ImportColumn column, OrderType orderType) {
    	super(table, column.name, column.dbDataType, column.length);
		this.orderType = orderType;
	}

	public OrderType getOrderType() {
		return orderType;
	}
}
