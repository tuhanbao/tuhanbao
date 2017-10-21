package com.tuhanbao.base.util.db.table;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.dataservice.ICTBean;
import com.tuhanbao.base.util.objutil.StringUtil;

/**
 * 业务项目用到的Table
 * 与ImportTable不同
 * 
 * @author Administrator
 *
 */
public class CTTable extends Table
{
	protected ICTBean ctBean;
	 
    public CTTable(ICTBean ctBean, Table table) {
    	list = table.list;
    	this.name = table.name + Constants.UNDER_LINE + ctBean.getTag();
    	this.cacheType = table.cacheType;
    	this.PK = table.PK;
		this.modelName = table.modelName;
		if (!StringUtil.isEmpty(table.seqName)) {
			this.seqName = table.seqName + Constants.UNDER_LINE + ctBean.getTag();
		}
    	this.ctBean = ctBean;
    }

	public CacheType getCacheType() {
		return CacheType.NOT_CACHE;
	}
	
	public ICTBean getCTBean() {
		return ctBean;
	}
	
	public boolean isCTTable() {
		return true;
	}

}
