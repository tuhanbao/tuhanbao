package com.td.ca.base.dataservice;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.td.ca.base.Constants;
import com.td.ca.base.dataservice.cache.ICacheGroupKey;
import com.td.ca.base.util.db.table.Column;
import com.td.ca.base.util.db.table.DataValueFactory;
import com.td.ca.base.util.db.table.ServiceBeanKeyManager;
import com.td.ca.base.util.db.table.Table;
import com.td.ca.base.util.db.table.data.AbstractDataValue;
import com.td.ca.base.util.io.codegenarator.table.DataType;
import com.td.ca.base.util.json.JSONUtil;
import com.td.ca.base.util.objutil.ArrayUtil;
import com.td.ca.base.util.objutil.MapUtil;
import com.td.ca.base.util.objutil.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wangbing
 *
 */
public class ServiceBean implements ISyncData, IGroupKeyValueCacheBean {
    private MetaObject mo;

    // 外键关联的对象, 其实Object应该为外键对应的Column，便于扩展，允许业务开发自定义
    /**
     * 外键对应的Column 1.多对一，column为自己本身外键关联其他表的column 2.一对多，其他表外键到自己本身的column
     */
    private Map<String, List<ServiceBean>> fkBeans;

    /**
     * 存ServiceBean的旧的分组值，虽然这个字段能够从属性中获取，但是如果不做缓存，会无法知道旧的分组是多少。
     */
    private Map<ICacheGroupKey, String> groupKeyValues;

    private static final String STRING = "String";

    public ServiceBean(Table table) {
        this(new MetaObject(table));
    }

    protected ServiceBean(MetaObject mo) {
        this.mo = mo;
        init();
    }

    @JSONField(serialize = false)
    public Table getTable() {
        return getMoTable();
    }

    @JSONField(serialize = false)
    public Table getMoTable() {
        return mo.table;
    }

    /**
     * 刷新ServiceBean类变量的方法, 子类对类变量进行初始化必须写在此处
     * 切忌做一些开启线程等无法回收的事情
     *
     * 在关联查询时，有些重复对象会分别执行init，但是最终只会保留一个。如果开启了线程等，会浪费资源而且无法回收。
     */
    public void init() {
    }

    /**
     * init而且递归init fkBeans
     */
    public void initRecursion() {
        this.init();
        if (this.fkBeans != null) {
            for (List<ServiceBean> list : this.fkBeans.values()) {
                for (ServiceBean sb : list) {
                    sb.initRecursion();
                }
            }
        }
    }

    public AbstractDataValue getValue(Column column) {
        return mo.get(column);
    }

    public void setValue(Column column, AbstractDataValue dataValue) {
        mo.put(column, dataValue);
    }

    @Override
    public String toString() {
        return this.mo.toString();
    }

    @JSONField(serialize = false)
    public String getAllColsInfo() {
        return this.mo.getDetail();
    }

    @Override
    public void setKeyValue(Object value) {
        Column pk = mo.table.getPK();
        setValue(pk, DataValueFactory.toDataValue(pk.getDataType(), value));
    }

    @Override
    public AbstractDataValue getKeyValue() {
        return getValue(mo.table.getPK());
    }

    public void addFKBean(Column key, ServiceBean bean) {
        if (bean == null) {
            return;
        }
        String newKey = getKey(key);
        addFKBean(newKey, bean);
    }

    public void addFKBean(Column key, ServiceBean bean, boolean isSureNotMyFk) {
        // 不确定的情况下，一律使用默认
        if (!isSureNotMyFk) {
            addFKBean(key, bean);
        } else {
            if (bean == null) {
                return;
            }
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
        if (bean == null) {
            this.removeFKBean(key, isSureNotMyFk);
            return;
        }
        // 不确定的情况下，一律使用默认
        String newKey = getKey(key, isSureNotMyFk);
        setFKBean(newKey, bean);
    }

    protected void setFKBean(Column key, List<? extends ServiceBean> beans) {
        setFKBean(key, beans, false);
    }

    protected void setFKBean(Column key, List<? extends ServiceBean> beans, boolean isSureNotMyFk) {
        if (beans == null) {
            this.removeFKBean(key, isSureNotMyFk);
            return;
        }
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
        if (bean == null) {
            return;
        }

        if (fkBeans == null) {
            fkBeans = new HashMap<>();
        }
        if (!this.fkBeans.containsKey(key)) {
            List<ServiceBean> list = new ArrayList<>();
            list.add(bean);
            this.fkBeans.put(key, list);
        } else {
            List<ServiceBean> list = this.fkBeans.get(key);
            int index = list.indexOf(bean);
            if (index >= 0) {
                list.get(index).mergerFKBean(bean);
            } else {
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
     * @param beans
     */
    public void addFKBean(String key, Collection<? extends ServiceBean> beans) {
        // 不能批量加，必须一个一个加，这里涉及到融合，即同一个serviceBean只会加一次，但是会并集他们的fkBeans。
        for (ServiceBean bean : beans) {
            addFKBean(key, bean);
        }
    }

    /**
     * 与 addFKBean不同的是，这里不会做任何校验，直接set，性能会好一些
     *
     * @param key
     * @param beans
     */
    public void setFKBean(String key, List<? extends ServiceBean> beans) {
        if (beans == null) {
            beans = new ArrayList<>();
        }

        if (fkBeans == null) {
            fkBeans = new HashMap<>();
        }
        this.fkBeans.put(key, (List<ServiceBean>) beans);
    }

    /**
     * 与 addFKBean不同的是，这里不会做任何校验，直接set，性能会好一些
     *
     * @param key
     * @param bean
     */
    public void setFKBean(String key, ServiceBean bean) {
        if (bean == null) {
            return;
        }

        if (fkBeans == null) {
            fkBeans = new HashMap<String, List<ServiceBean>>();
        }
        ArrayList<ServiceBean> list = new ArrayList<ServiceBean>(1);
        list.add(bean);
        this.fkBeans.put(key, list);
    }

    /**
     * 把两个bean的关联bean进行并集
     *
     * @param serviceBean
     */
    public void mergerFKBean(ServiceBean serviceBean) {
        if (serviceBean.fkBeans == null) {
            return;
        }

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
        if (fkBeans == null) {
            return null;
        }
        return fkBeans.get(key);
    }

    private List<ServiceBean> removeFKBean(String key) {
        if (fkBeans == null) {
            return null;
        }
        return fkBeans.remove(key);
    }

    /**
     * ------------------------------------------------------------ mybatis用到的
     */

    @JSONField(serialize = false)
    public String getMyBatisColumnValueStr() {
        return getMyBatisColumnValueStr(null);
    }

    public String getMyBatisColumnValueStr(String modeName) {
        StringBuilder cols = new StringBuilder();
        StringBuilder values = new StringBuilder();
        String prefix = "#{";
        if (!StringUtil.isEmpty(modeName)) {
            prefix = prefix + modeName + ".";
        }
        for (Column col : getTable().getColumns()) {
            if (this.containsKey(col)) {
                // 使用"`"包裹可以兼容sql的关键字
                cols.append("`" + col.getName() + "`").append(Constants.COMMA);

                // mysql的一个很特殊的字段，这里会有兼容性问题
                // 这里的colName必须大写：ibatis会根据属性名找getter方法，比如sex字段ibatis会找到getter方法的返回值是枚举，它会有EnumTypeHandler处理枚举字段的IntegerData导致报错
                if (col.getDataType() == DataType.GIS) {
                    values.append("ST_GeomFromText(").append(prefix).append(col.getName().toUpperCase()).append(",javaType=java.lang.String})").append(Constants.COMMA);
                } else {
                    values.append(prefix).append(col.getName().toUpperCase()).append(",jdbcType=").append(getJDBCType(col.getDataType())).append("}").append(Constants.COMMA);
                }
            }
        }

        StringBuilder result = new StringBuilder();
        result.append("(").append(StringUtil.deleteLastChar(cols)).append(") values (").append(StringUtil.deleteLastChar(values)).append(")");
        return result.toString();
    }

    @JSONField(serialize = false)
    public String getMyBatisUpdateValueStr() {
        return getMyBatisUpdateValueStr(null, false, false);
    }

    public String getMyBatisUpdateValueStr(String modeName) {
        return this.getMyBatisUpdateValueStr(modeName, false, false);
    }

    @JSONField(serialize = false)
    public String getMyBatisUpdateValueStrSelected() {
        return this.getMyBatisUpdateValueStr(null, true, false);
    }

    public String getMyBatisUpdateValueStrSelected(String modeName) {
        return this.getMyBatisUpdateValueStr(modeName, true, false);
    }

    public String getMyBatisUpdateValueStrSelected(String modeName, boolean withTableName) {
        return this.getMyBatisUpdateValueStr(modeName, true, withTableName);
    }

    private String getMyBatisUpdateValueStr(String modeName, boolean isSelective, boolean withTableName) {
        StringBuilder sb = new StringBuilder();
        String prefix = "#{";
        if (!StringUtil.isEmpty(modeName)) {
            prefix = prefix + modeName + ".";
        } else {
            // 需要区分model
            prefix = prefix + "model.";
        }
        for (Column col : getTable().getColumns()) {
            // 主键不允许更新
            if (col == getTable().getPK()) {
                continue;
            }
            // 没有包含的值的不需要更新
            if (isSelective && !this.containsKey(col)) {
                continue;
            }
            // 使用"`"包裹可以兼容sql的关键字
            if (withTableName) {
                sb.append(col.getNameWithAs() + " = ");
            } else {
                sb.append("`" + col.getName() + "` = ");
            }

            // mysql的一个很特殊的字段，这里会有兼容性问题
            if (col.getDataType() == DataType.GIS) {
                sb.append("ST_GeomFromText(").append(prefix).append(col.getName().toUpperCase()).append(",javaType=java.lang.String})").append(Constants.COMMA);
            } else {
                // 使用"`"包裹可以兼容sql的关键字
                sb.append(prefix)
                        .append(col.getName().toUpperCase())
                        .append(",jdbcType=")
                        .append(getJDBCType(col.getDataType()))
                        .append("}")
                        .append(Constants.COMMA);
            }
        }
        return StringUtil.deleteLastChar(sb);
    }

    public boolean containsKey(Column col) {
        return this.mo.getProperties().containsKey(col);
    }

    private static String getJDBCType(DataType dataType) {
        if (STRING.equals(dataType.getName())) {
            return "VARCHAR";
        } else if (dataType == DataType.BIGDEECIMAL) {
            return "DECIMAL";
        } else if (dataType == DataType.INT) {
            return "INTEGER";
        } else if (dataType == DataType.LONG) {
            return "BIGINT";
        } else if (dataType == DataType.OBJECT) {
            return "OTHER";
        } else if (dataType == DataType.GIS) {
            return "GEOMETRY";
        } else if (dataType == DataType.DATE) {
            return "TIMESTAMP";
        } else if (dataType == DataType.BIGBLOB) {
            return "BLOB";
        } else {
            return dataType.name();
        }
    }

    @Override
    public int hashCode() {
        return this.getTable().hashCode() * 13 + this.getKeyValue().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof ServiceBean) {
            ServiceBean temp = (ServiceBean) o;
            if (temp.getTable() != this.getTable() || this.getKeyValue() == null || temp.getKeyValue() == null) {
                return false;
            }
            return this.getKeyValue().equals(temp.getKeyValue());
        }
        return false;
    }

    public Map<Column, AbstractDataValue> getProperties() {
        return this.mo.getProperties();
    }

    @Override
    public void sync(ISyncData bean) {
        if (bean == null) {
            return;
        }
        this.mo.syncMo(((ServiceBean) bean).mo);
        this.init();
    }

    @Override
    public void syncAll(ISyncData bean) {
        if (bean == null) {
            return;
        }
        this.mo.syncMoAll(((ServiceBean) bean).mo);
        this.init();
    }

    public Map<String, List<ServiceBean>> getFkBeans() {
        return fkBeans;
    }

    private void putJson(JSONObject json, String key, List<ServiceBean> beans) {
        if (beans == null || beans.isEmpty()) {
            return;
        }

        ServiceBean childBean = beans.get(0);
        if (ServiceBeanKeyManager.isSingleKey(this.getTable(), key)) {
            // 外键关系只存在1对1,多对1，比如A表的B_ID外键关联B表，B_ID必须为B的唯一属性（在框架系统里面，只能是主键）
            json.put(key, JSONUtil.toJSON(childBean));
        } else {
            // 关系表一定会走到此处，关系表一定是N:N的关系
            if (childBean.getMoTable().isRelationTable()) {
                Map<String, List<ServiceBean>> fkBeans = childBean.getFkBeans();
                if (fkBeans != null && fkBeans.size() > 0) {
                    String childKey = fkBeans.keySet().stream().findFirst().get();

                    List<ServiceBean> newChildBeans = new ArrayList<>();
                    for (ServiceBean rtBean : beans) {
                        newChildBeans.addAll(rtBean.getFKBean(childKey));
                    }

                    json.put(childKey + "s", JSONUtil.toJSON(newChildBeans));
                    return;
                }
            }

            json.put(key, JSONUtil.toJSON(beans));
        }
    }

    protected List<ServiceBean> getRtFKBean(Column col) {
        List<ServiceBean> result = new ArrayList<>();
        List<ServiceBean> fkBean = this.getFKBean(getKey(col));
        if (fkBean != null) {
            for (ServiceBean rtBean : fkBean) {
                Map<String, List<ServiceBean>> beans = rtBean.getFkBeans();
                if (!MapUtil.isEmpty(beans)) {
                    result.addAll(beans.values().stream().findFirst().get());
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public void addFKBeanFromJson(String key, Object value) {
        Class<?> serviceBeanClass = ServiceBeanKeyManager.getFKModelClassByKey(getTable(), key);
        if (serviceBeanClass == null) {
            Table fkTable = ServiceBeanKeyManager.getFKTableByKey(getTable(), key);
            if (fkTable == null) {
                return;
            }
            if (value instanceof JSONObject) {
                ServiceBean result = ServiceBeanParser.getServiceBean((JSONObject) value, fkTable);
                this.addFKBean(key, result);
            } else {
                List<ServiceBean> result = ServiceBeanParser.getServiceBeans((JSONArray) value, fkTable);
                this.addFKBean(key, result);
            }
        } else {
            if (value instanceof JSONObject) {
                ServiceBean result = (ServiceBean) JSONUtil.getBean(value, serviceBeanClass);
                this.addFKBean(key, result);
            } else {
                List<ServiceBean> result = (List<ServiceBean>) JSONUtil.getArray((JSONArray) value, serviceBeanClass);
                this.addFKBean(key, result);
            }
        }
    }

    private String getKey(Column col) {
        return getKey(col, false);
    }

    private String getKey(Column col, boolean isSureNotMyFk) {
        return ServiceBeanKeyManager.getKey(this.getMoTable(), col, isSureNotMyFk);
    }

    @Override
    @JSONField(serialize = false)
    public Table getDataGroup() {
        return this.getTable();
    }

    @Override
    public String getGroupKeyValue(ICacheGroupKey key) {
        if (this.groupKeyValues == null) {
            return null;
        }
        return this.groupKeyValues.get(key);
    }

    @Override
    public void setGroupKeyValue(ICacheGroupKey key, String value) {
        if (this.groupKeyValues == null) {
            this.groupKeyValues = new HashMap<>(1);
        }
        this.groupKeyValues.put(key, value);
    }

    /**
     * 如果serveriBean只有主键信息或者一个属性值都没有，返回true
     * @return
     */
    public boolean isEmpty() {
        Map<Column, AbstractDataValue> properties = this.getProperties();
        return properties.isEmpty() || (properties.size() == 1 && this.getKeyValue() != null);
    }

    public void clearNullValue() {
        Map<Column, AbstractDataValue> properties = this.getProperties();
        for (Iterator<Map.Entry<Column, AbstractDataValue>> it = properties.entrySet().iterator(); it.hasNext();) {
            if (it.next().getValue() == null) {
                it.remove();
            }
        }

        Map<String, List<ServiceBean>> beans = this.getFkBeans();
        if (!MapUtil.isEmpty(beans)) {
            beans.values().forEach(item -> {
                if (!ArrayUtil.isEmpty(item)) {
                    item.forEach(sb -> sb.clearNullValue());
                }
            });
        }
    }
}
