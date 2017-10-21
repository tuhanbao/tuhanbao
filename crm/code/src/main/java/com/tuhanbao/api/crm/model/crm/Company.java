package com.tuhanbao.api.crm.model.crm;

import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.base.dataservice.ICTBean;
import com.tuhanbao.base.util.db.table.CTTable;
import com.tuhanbao.base.util.db.table.Table;

public class Company extends CompanyMO implements ICTBean {
	
	private static final Map<Table, CTTable> CT_TABLES = new HashMap<Table, CTTable>();
	
    public Company() {

    }

	@Override
	public String getTag() {
		return this.getShortName();
	}

	@Override
	public Table getTable(Table table) {
		if (!table.isCTTable() || table instanceof CTTable) return table;
		CTTable ctTable = CT_TABLES.get(table);
		if (ctTable == null) {
			ctTable = new CTTable(this, table);
			CT_TABLES.put(table, ctTable);
		}
		return ctTable;
	}

}