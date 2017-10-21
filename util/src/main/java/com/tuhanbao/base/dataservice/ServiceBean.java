package com.tuhanbao.base.dataservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.db.table.Column;
import com.tuhanbao.base.util.db.table.DataValueFactory;
import com.tuhanbao.base.util.db.table.ServiceBeanKeyManager;
import com.tuhanbao.base.util.db.table.Table;
import com.tuhanbao.base.util.db.table.data.DataValue;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;
import com.tuhanbao.base.util.json.JsonUtil;

public class ServiceBean implements ISyncData
{
    protected MetaObject mo;
    
    //外键关联的对象, 其实Object应该为外键对应的Column，便于扩展，允许业务开发自定义
    /**
     * 外键对应的Column  
     * 1.多对一，column为自己本身外键关联其他表的column
     * 2.一对多，其他表外键到自己本身的column
     */
    private Map<String, List<ServiceBean>> fkBeans;
    
    protected ServiceBean(Table table) 
    {
    	this(new MetaObject(table));
    }
    
    protected ServiceBean(MetaObject mo)
    {
        this.mo = mo;
        init();
    }
	
	public Table getTable() {
		return mo.table;
	}
    
    /**
     * 刷新ServiceBean类变量的方法
     * 子类对类变量进行初始化必须写在此处
     */
    public void init()
    {
        
    }
    
    public DataValue getValue(Column column)
    {
        return mo.get(column);
    }
    
    public void setValue(Column column, DataValue dataValue)
    {
        mo.put(column, dataValue);
    }
    
    @Override
    public String toString()
    {
        return this.mo.toString();
    }
    
    public void setKeyValue(Object value)
    {
        Column pk = mo.table.getPK();
		setValue(pk, DataValueFactory.toDataValue(pk.getDataType(), value));
    }

    @Override
    public DataValue getKeyValue()
    {
        return getValue(mo.table.getPK());
    }
    
    public void addFKBean(Column key, ServiceBean bean) {
        if (bean == null) return;
        String newKey = getKey(key);
        addFKBean(newKey, bean);
    }
    
    public void addFKBean(Column key, ServiceBean bean, boolean isSureNotMyFk) {
        //不确定的情况下，一律使用默认
        if (!isSureNotMyFk) {
            addFKBean(key, bean);
        }
        else {
            if (bean == null) return;
            String newKey = getKey(key, isSureNotMyFk);
            addFKBean(newKey, bean);
        }
    }
    
    protected void addFKBean(Column key, Collection<? extends ServiceBean> beans) {
    	for (ServiceBean bean : beans) {
    		addFKBean(key, bean);
    	}
    }
    
    protected void setFKBean(Column key, ServiceBean bean) {
        setFKBean(key, bean, false);
    }
    
    protected void setFKBean(Column key, ServiceBean bean, boolean isSureNotMyFk) {
        if (bean == null) return;
        //不确定的情况下，一律使用默认
        String newKey = getKey(key, isSureNotMyFk);
        setFKBean(newKey, bean);
    }
    
    protected void setFKBean(Column key, Collection<? extends ServiceBean> beans) {
        setFKBean(key, beans, false);
    }
    
    protected void setFKBean(Column key, Collection<? extends ServiceBean> beans, boolean isSureNotMyFk) {
        if (beans == null) return;
        String newKey = getKey(key, isSureNotMyFk);
        setFKBean(newKey, beans);
    }
    
    /**
     * 请谨慎调用
     * 
     * 私自调用在json转对象时可能会有遗漏
     * 
     * @param key
     * @param bean
     */
    public void addFKBean(String key, ServiceBean bean) {
        if (bean == null) return;
        
        if (fkBeans == null) fkBeans = new HashMap<String, List<ServiceBean>>();
        if (!this.fkBeans.containsKey(key)) {
            ArrayList<ServiceBean> list = new ArrayList<ServiceBean>();
            list.add(bean);
            this.fkBeans.put(key, list);
        }
        else {
            List<ServiceBean> list = this.fkBeans.get(key);
            int index = list.indexOf(bean);
            if (index >= 0) {
                list.get(index).mergerFKBean(bean);
            }
            else {
                list.add(bean);
            }
        }
    }
    
    /**
     * 请谨慎调用
     * 
     * 私自调用在json转对象时可能会有遗漏
     * 
     * @param key
     * @param bean
     */
    public void addFKBean(String key, Collection<? extends ServiceBean> beans) {
        //不能批量加，必须一个一个加，这里涉及到融合，即同一个serviceBean只会加一次，但是会并集他们的fkBeans。
        for (ServiceBean bean : beans) {
            addFKBean(key, bean);
        }
    }
    
    /**
     * 与 addFKBean不同的是，这里不会做任何校验，直接set，性能会好一些
     * 
     * @param key
     * @param bean
     */
    public void setFKBean(String key, Collection<? extends ServiceBean> beans) {
        if (beans == null) return;
        
        if (fkBeans == null) fkBeans = new HashMap<String, List<ServiceBean>>();
        ArrayList<ServiceBean> list = new ArrayList<ServiceBean>(beans);
        this.fkBeans.put(key, list);
    }
    
    /**
     * 与 addFKBean不同的是，这里不会做任何校验，直接set，性能会好一些
     * 
     * @param key
     * @param bean
     */
    public void setFKBean(String key, ServiceBean bean) {
        if (bean == null) return;
        
        if (fkBeans == null) fkBeans = new HashMap<String, List<ServiceBean>>();
        ArrayList<ServiceBean> list = new ArrayList<ServiceBean>(1);
        list.add(bean);
        this.fkBeans.put(key, list);
    }
    
    /**
     * 把两个bean的关联bean进行并集
     * @param serviceBean
     */
    public void mergerFKBean(ServiceBean serviceBean) {
    	if (serviceBean.fkBeans == null) return ;
    	
    	for (Map.Entry<String, List<ServiceBean>> entry : serviceBean.fkBeans.entrySet()) {
    		this.addFKBean(entry.getKey(), entry.getValue());
    	}
    }
    
    public List<ServiceBean> getFKBean(Column key) {
        return getFKBean(getKey(key));
    }
    
    public List<ServiceBean> getFKBean(Column key, boolean isSureNotMyFk) {
        return getFKBean(getKey(key, isSureNotMyFk));
    }

    public List<ServiceBean> removeFKBean(Column key) {
        return removeFKBean(getKey(key));
    }
    
    public List<ServiceBean> removeFKBean(Column key, boolean isSureNotMyFk) {
        return removeFKBean(getKey(key, isSureNotMyFk));
    }  
    
    private List<ServiceBean> getFKBean(String key) {
        if (fkBeans == null) return null;
        return fkBeans.get(key);
    }
    
    private List<ServiceBean> removeFKBean(String key) {
        if (fkBeans == null) return null;
        return fkBeans.remove(key);
    }
    
	/**
	 * ------------------------------------------------------------
	 * mybatis用到的
	 */
	public String getColumns() {
		StringBuilder sb = new StringBuilder();
		for (Column col : getTable().getColumns()) {
			sb.append(col.getName()).append(Constants.COMMA);
		}
		return sb.substring(0, sb.length() - 1);
	}
    
    public String getMyBatisColumnValueStr() {
        StringBuilder sb = new StringBuilder();
        for (Column col : getTable().getColumns()) {
            sb.append("#{").append(col.getName()).append(",jdbcType=")
                .append(getJDBCType(col.getDataType())).append("}").append(Constants.COMMA);
        }
        return sb.substring(0, sb.length() - 1);
    }
    
    public String getMyBatisUpdateValueStr() {
        StringBuilder sb = new StringBuilder();
        for (Column col : getTable().getColumns()) {
            //主键是不允许更新的
            if (col == getTable().getPK()) continue;
            sb.append(col.getName()).append(" = #{model.").append(col.getName()).append(",jdbcType=")
                .append(getJDBCType(col.getDataType())).append("}").append(Constants.COMMA);
        }
        return sb.substring(0, sb.length() - 1);
    }
    
    public String getMyBatisUpdateValueStrSelected() {
        StringBuilder sb = new StringBuilder();
        for (Column col : getTable().getColumns()) {
            //主键是不允许更新的
            if (col == getTable().getPK()) continue;
            //没有值的不需要更新
            if (this.getValue(col) == null) continue;
            sb.append(col.getName()).append(" = #{model.").append(col.getName()).append(",jdbcType=")
                .append(getJDBCType(col.getDataType())).append("}").append(Constants.COMMA);
        }
        return sb.substring(0, sb.length() - 1);
    }
    
    private static String getJDBCType(DataType dataType) {
        if ("String".equals(dataType.getName())) return "VARCHAR";
        else if (dataType == DataType.BIGDEECIMAL) return "DECIMAL";
        else if (dataType == DataType.INT) return "INTEGER";
        else if (dataType == DataType.LONG) return "BIGINT";
        else if (dataType == DataType.OBJECT) return "OTHER";
        else if (dataType == DataType.DATE) return "TIMESTAMP";
        else return dataType.name();
    }
	
	public int hashCode() {
		return this.getTable().hashCode() * 13 + this.getKeyValue().hashCode();
	}
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof ServiceBean) {
			ServiceBean temp = (ServiceBean) o;
			if (temp.getTable() != this.getTable() || this.getKeyValue() == null || temp.getKeyValue() == null) return false;
			return this.getKeyValue().equals(temp.getKeyValue());
		}
		return false;
	}

	public Map<Column, DataValue> getProperties() {
		return this.mo.getProperties();
	}
	
	public void sync(ISyncData bean) {
	    if (bean == null) return;
	    this.mo.syncMo(((ServiceBean)bean).mo);
	    this.init();
	}

	public Map<String, List<ServiceBean>> getFkBeans() {
		return fkBeans;
	}

	/**
	 * 请不要使用此方法转化json，
	 * @see com.tuhanbao.web.util.JsonUtil
	 * @return
	 */
	public JSONObject toJson() {
		Map<String, Object> values = new HashMap<String, Object>();
		for (Map.Entry<Column, DataValue> entry : this.getProperties().entrySet()) {
			values.put(entry.getKey().getName().toLowerCase(), entry.getValue().getValue4DB());
		}
		JSONObject json = (JSONObject) JSON.toJSON(values);
		
		if (this.fkBeans != null) {
			for (Map.Entry<String, List<ServiceBean>> entry : this.fkBeans.entrySet()) {
				putJson(json, entry.getKey(), entry.getValue());
			}
		}
		return json;
	}

	private void putJson(JSONObject json, String key, List<ServiceBean> beans) {
		if (beans == null || beans.isEmpty()) return;
		
		if (ServiceBeanKeyManager.isSingleKey(this.getTable(), key)) {
		    ServiceBean otherBean = beans.get(0);
		    //外键关系只存在1对1,多对1，
		    json.put(key, JsonUtil.toJSON(otherBean));
		}
		else {
		    json.put(key, JsonUtil.toJSON(beans));
		}
	}

    @SuppressWarnings("unchecked")
    public void addFKBeanFromJson(String key, JSON value) {
        Class<?> serviceBeanClass = ServiceBeanKeyManager.getFKModelClassByKey(getTable(), key);
        if (serviceBeanClass == null) return;
        
        if (value instanceof JSONObject) {
            ServiceBean result = (ServiceBean)JsonUtil.getBean((JSONObject)value, serviceBeanClass);
            this.addFKBean(key, (ServiceBean)result);
        }
        else {
            List<ServiceBean> result = (List<ServiceBean>)JsonUtil.getServiceBeans((JSONArray)value, serviceBeanClass);
            this.addFKBean(key, result);
        }
    }
    
    private String getKey(Column col) {
        return getKey(col, false);
    }
    
    private String getKey(Column col, boolean isSureNotMyFk) {
        return ServiceBeanKeyManager.getKey(this.getTable(), col, isSureNotMyFk);
    }

    @Override
    public Table getDataGroup() {
        return this.getTable();
    }
}
