package com.tuhanbao.base.dataservice;

import com.tuhanbao.base.util.db.table.Table;

public class CTServiceBean extends ServiceBean
{
	
	protected ICTBean ctBean;
    
    protected CTServiceBean(ICTBean ctBean, Table table) 
    {
    	this(ctBean, new MetaObject(table));
    }
    
    protected CTServiceBean(ICTBean ctBean, MetaObject mo)
    {
    	super(mo);
    	this.ctBean = ctBean;
    }
    
    public ICTBean getCTBean() {
    	return this.ctBean;
    }
	
	public Table getTable() {
		return ctBean.getTable(super.getTable());
	}

    @Override
    public Table getDataGroup() {
        return this.getTable();
    }
}
