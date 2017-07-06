package com.tuhanbao.base.dataservice.filter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.tuhanbao.base.dataservice.IDataGroup;
import com.tuhanbao.base.util.db.IField;

/**
 * 查询的字段和表
 * 
 * @author Administrator
 * @param <L>
 *
 */
public class Selector<T extends IDataGroup<L>, L extends IField> {

    private Set<L> selectFields = new HashSet<>();
    
    private T dataGroup;
    
    public Set<L> getFields() {
        return selectFields;
    }
    
    public void addField(L field) {
        if (selectFields == null) selectFields = new HashSet<>();
        selectFields.add(field);
    }

    public Selector(T dataGroup) {
        this.dataGroup = dataGroup;
    }

    public Collection<L> getSelectFields() {
        if (this.selectFields == null) {
            this.selectFields = new HashSet<L>();
            this.selectFields.addAll(dataGroup.getFields());
        }
        return this.selectFields;
    }

    public T getDataGroup() {
        return dataGroup;
    }
}
