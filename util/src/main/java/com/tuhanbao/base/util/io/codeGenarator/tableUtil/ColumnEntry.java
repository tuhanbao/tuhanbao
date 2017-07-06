package com.tuhanbao.base.util.io.codeGenarator.tableUtil;



public class ColumnEntry {
    private ImportColumn col;
    private Relation relation;
    
	public ColumnEntry(ImportColumn col, Relation relation) {
		this.col = col;
		this.relation = relation;
	}
	public ImportColumn getCol() {
		return col;
	}
	public void setCol(ImportColumn col) {
		this.col = col;
	}
	public Relation getRelation() {
		return relation;
	}
	public void setRelation(Relation relation) {
		this.relation = relation;
	}
}
