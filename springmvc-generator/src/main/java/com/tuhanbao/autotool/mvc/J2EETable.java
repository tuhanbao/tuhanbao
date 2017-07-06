package com.tuhanbao.autotool.mvc;

import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportTable;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.TableConfig;
import com.tuhanbao.base.util.objutil.BinaryUtil;

public class J2EETable extends ImportTable {

    private boolean hasBlobCol = false;
	
    public J2EETable(String name, String module) {
        this(name, module, new TableConfig());
    }
    
    public J2EETable(String name, String module, TableConfig tableConfig) {
        super(name, module, tableConfig);
    }
    

	public void addColumn(ImportColumn col) {
		super.addColumn(col);
		
		if (col.isBlob()) {
			this.hasBlobCol = true;
		}
	}

	public String getExampleName() {
		return this.modelName + "Example";
	}

	public String getMapperName() {
		return this.modelName + "Mapper";
	}
	
	public String getIServiceName() {
		return "I" + this.modelName + "Service";
	}

	public String getServiceName() {
		return this.modelName + "ServiceImpl";
	}
	
	public String getControllerName() {
		return this.modelName + "Controller";
	}
	
	public boolean needC() {
		return !BinaryUtil.isZero(getTableConfig().getCrud(), 3);
	}
	public boolean needR() {
		return !BinaryUtil.isZero(getTableConfig().getCrud(), 2);
	}
	public boolean needU() {
		return !BinaryUtil.isZero(getTableConfig().getCrud(), 1);
	}
	public boolean needD() {
		return !BinaryUtil.isZero(getTableConfig().getCrud(), 0);
	}
	
	public boolean hasBlobCol() {
		return this.hasBlobCol;
	}

	public boolean needCutPage() {
		return getTableConfig().isNeedCutPage();
	}

	public ImportColumn getDefaultOrderCol() {
		return getColumn(getTableConfig().getDefaultOrderColName());
	}
}
