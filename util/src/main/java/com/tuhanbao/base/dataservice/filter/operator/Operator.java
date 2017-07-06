package com.tuhanbao.base.dataservice.filter.operator;

import com.tuhanbao.base.Constants;

public enum Operator
{
    EQUAL(1, "="), NOT_EQUAL(2, "<>"), LESS_EQUAL(3, "<="), GREATER_EQUAL(4, ">="),
    LESS(5, "<"), GREATER(6, ">"), LIKE(7, "like"), BETWEEN(8, "between"), 
    IN(9, "in"), NOT_LIKE(10, "not like"), NOT_IN(11, "not in"),
    IS_NULL(12, "is null"), NOT_NULL(13, "is not null");
    
    public int value;
    
    private String opt;
    
    private Operator(int value, String opt) {
        this.value = value;
    	this.opt = Constants.BLANK + opt + Constants.BLANK;
    }
    
    public static Operator getOperator(int value) {
        for (Operator op : Operator.values()) {
            if (op.value == value) return op;
        }
        return null;
    }
    
    public String toString() {
    	return opt;
    }
}
