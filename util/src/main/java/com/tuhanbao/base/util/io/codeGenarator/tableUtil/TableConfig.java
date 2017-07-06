package com.tuhanbao.base.util.io.codeGenarator.tableUtil;

import java.util.List;

import com.tuhanbao.base.util.db.table.CacheType;
import com.tuhanbao.base.util.io.codeGenarator.sqlUtil.GenerateStrategy;

public class TableConfig {

	//生成策略  默认为融合
	protected GenerateStrategy gs = GenerateStrategy.MEGER;
	
	//需要生成的方法  默认全部
	protected int crud = 0xF;
	
	//缓存类型
	protected CacheType cacheType;
	
	//需要连表的字段，一般就是left join
	protected List<String[]> selectJoinCols;
	
	//需要根据主键查询的关键字
	private List<String[]> selectKeys;
	
	//是否需要分页
	protected boolean needCutPage = true;
	
	//有些表默认需要排序
	private String defaultOrderColName;
	
	//序列号  oracle下才会有
	private String sequence;
	
	//是否自动自增
	private boolean isAutoIncrement;

	public GenerateStrategy getGs() {
		return gs;
	}

	public int getCrud() {
		return crud;
	}

	public CacheType getCacheType() {
		return cacheType;
	}

	public List<String[]> getSelectJoinCols() {
		return selectJoinCols;
	}

	public List<String[]> getSelectKeys() {
		return selectKeys;
	}

	public boolean isNeedCutPage() {
		return needCutPage;
	}
	
	public String getDefaultOrderColName() {
		return defaultOrderColName;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public void setGs(GenerateStrategy gs) {
		this.gs = gs;
	}

	public void setCrud(int crud) {
		this.crud = crud;
	}

	public void setCacheType(CacheType cacheType) {
		this.cacheType = cacheType;
	}

	public void setSelectJoinCols(List<String[]> selectJoinCols) {
		this.selectJoinCols = selectJoinCols;
	}

	public void setSelectKeys(List<String[]> selectKeys) {
		this.selectKeys = selectKeys;
	}

	public void setNeedCutPage(boolean needCutPage) {
		this.needCutPage = needCutPage;
	}

	public void setDefaultOrderColName(String defaultOrderColName) {
		this.defaultOrderColName = defaultOrderColName;
	}

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public void setAutoIncrement(boolean isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }
}
