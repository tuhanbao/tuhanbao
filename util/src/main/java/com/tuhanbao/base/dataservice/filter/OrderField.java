package com.tuhanbao.base.dataservice.filter;

import com.tuhanbao.base.util.db.IField;

public class OrderField {

    private IField field;
    
    private boolean isDesc = false;
    
    public OrderField(IField field, boolean isDesc) {
    	this.field = field;
    	this.isDesc = isDesc;
    }
    
    public IField getField() {
        return field;
    }

    public boolean isDesc() {
        return isDesc;
    }
}
