package com.tuhanbao.base.dataservice.filter;

import java.util.Collection;

import com.tuhanbao.base.Constants;

public enum ParentheseItem implements IFilterItem {
	
	LEFT_PARENTHESE(1, Constants.LEFT_PARENTHESE),
	RIGHT_PARENTHESE(2, Constants.RIGHT_PARENTHESE);
    
    public int value;
	
	private String name;
	
	private ParentheseItem(int value, String name) {
	    this.value = value;
		this.name = name;
	}
	
    /**
     * isList = true时请重写
     * @return
     */
    public Collection<Object> listValue() {
        return null;
    }
    

    public boolean isList() {
        return false;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    public static ParentheseItem getParentheseItem(int value) {
        for (ParentheseItem pi : ParentheseItem.values()) {
            if (pi.value == value) return pi;
        }
        return null;
    }
}
