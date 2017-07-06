package com.tuhanbao.web.filter;

import java.util.ArrayList;
import java.util.List;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.dataservice.IDataGroup;
import com.tuhanbao.base.dataservice.filter.Filter;
import com.tuhanbao.base.dataservice.filter.IFilterItem;
import com.tuhanbao.base.dataservice.filter.OrderField;
import com.tuhanbao.base.dataservice.filter.operator.Operator;
import com.tuhanbao.base.dataservice.filter.page.Page;
import com.tuhanbao.base.util.db.IField;

/**
 * 由于mybatis使用的是mapper文件配置的方法，为了不污染filter类，特创建此类
 * 
 * 条件查询
 * 
 * <p>
 * 应用于mybatis的map.xml时只支持条件语句的顺序写法，
 * 如id=1，但是不支持1=id
 * </P>
 * 
 * <p>
 * mybatis示例写法，其中变量名item不允许改变
 * <sql id="Filter_Where_Clause" >
    <where>
      <foreach collection="items" item="item" separator="" >
        ${item.getSql()}
        <if test="item.isList()">
       		<foreach collection="item.listValue()" item="listItem" open="(" close=")" separator="," >
				#{listItem}
           	</foreach>
        </if>
      </foreach>
    </where>
  </sql>
  </p>
 * 
 * @author Administrator
 *
 */
public class MyBatisFilter extends Filter {
    
    private final Filter filter;

    private static final String ORDERBY = "order by";
    
    private MyBatisFilter(Filter filter) {
        this.filter = filter;
    }
    
    public static MyBatisFilter getInstance(Filter filter) {
        if (filter == null) return null;
        if (filter instanceof MyBatisFilter) return (MyBatisFilter)filter;
        return new MyBatisFilter(filter);
    }
    
    /**
     * arg2 为list，代表in,
     * arg2 位二维数组，代表between
     * 其余默认为等于
     * 
     * @param arg1
     * @param arg2
     */
    public Filter andFilter(Object ... args)
    {
        this.filter.andFilter(args);
        return this;
    }
    
    public Filter orFilter(Object ... args)
    {
        this.orFilter(args);
        return this;
    }
    
    public Filter andFilter(Operator operator, Object ... args)
    {
        this.filter.andFilter(operator, args);
        return this;
    }
    
    public Filter orFilter(Operator operator, Object ... args)
    {
        this.filter.orFilter(operator, args);
        return this;
    }
    
    public Filter addOrderField(IField field) {
        this.filter.addOrderField(field);
        return this;
    }
    
    public Filter addOrderField(IField field, boolean isDesc) {
        this.filter.addOrderField(field, isDesc);
        return this;
    }
    
    public Filter addLeftParenthese()
    {
        this.filter.addLeftParenthese();
        return this;
    }
    
    public Filter andLeftParenthese() {
        this.filter.andLeftParenthese();
        return this;
    }
    
    public Filter orLeftParenthese() {
        this.filter.orLeftParenthese();
        return this;
    }
    
    public Filter addRightParenthese() {
        this.filter.addRightParenthese();
        return this;
    }
    
    public Page getPage() {
        return this.filter.getPage();
    }

    public void setPage(Page page) {
        this.filter.setPage(page);
    }

    public boolean isDistinct() {
        return this.filter.isDistinct();
    }

    public void setDistinct(boolean distinct) {
        this.filter.setDistinct(distinct);
    }
    
    public List<IFilterItem> getItems() {
        List<IFilterItem> items = this.filter.getItems();
        if (items == null) return null;
        List<IFilterItem> list = new ArrayList<>();
        for (IFilterItem item : items) {
            list.add(new MyBatisFilterItem(item));
        }
        return list;
    }
    
    public IDataGroup<?> getDataGroup() {
        return this.filter.getDataGroup();
    }

    public boolean isEmpty() {
        return this.filter.isEmpty();
    }

    private int getSize() {
        if (this.getItems() == null) return 0;
        return this.getItems().size();
    }
    
    public String getSelectSql()
    {
        if (getSize() >= 1) {
            StringBuilder sb = new StringBuilder();
            List<IFilterItem> items = this.getItems();
            sb.append(items.get(0).toString());
            for (int i = 1, size = getSize(); i < size; i++)
            {
                sb.append(Constants.BLANK).append(items.get(i));
            }
            return sb.toString();
        }
        else {
            return Constants.EMPTY;
        }
    }
    
    public String getOrderSql()
    {
        List<OrderField> orderFields = this.filter.getOrderFields();
        if (orderFields != null && orderFields.size() >= 1) {
            StringBuilder sb = new StringBuilder();
            sb.append(Constants.BLANK).append(ORDERBY);
            sb.append(Constants.BLANK).append(getOrderFieldSql(orderFields.get(0)));
            
            int size = orderFields.size();
            for (int i = 1; i < size; i++) {
                sb.append(Constants.COMMA).append(Constants.BLANK).append(getOrderFieldSql(orderFields.get(i)));
            }
            return sb.toString();
        }
        else {
            return Constants.EMPTY;
        }
    }
    
    public String getOrderFieldSql(OrderField orderField) {
        return orderField.getField().getNameWithDataGroup() + Constants.BLANK + (orderField.isDesc() ? "desc" : "asc");
    }
}
