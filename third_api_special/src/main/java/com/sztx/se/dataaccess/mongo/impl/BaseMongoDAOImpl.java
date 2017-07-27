package com.sztx.se.dataaccess.mongo.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.DbCallback;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;
import com.sztx.se.dataaccess.mongo.BaseMongoDAO;
import com.sztx.se.dataaccess.mongo.config.PageQuery;
import com.sztx.se.dataaccess.mongo.source.DynamicMongoSource;

public abstract class BaseMongoDAOImpl<T> implements BaseMongoDAO<T> {

	@Autowired(required = false)
	protected DynamicMongoSource mongoTemplate;

	/**
	 * 保存或更新一个对象
	 * 
	 * @param bean
	 * @return
	 */
	@Override
	public void save(T bean) {
		mongoTemplate.save(bean);
	}

	/**
	 * 新增一个对象
	 * 
	 * @param bean
	 */
	@Override
	public void insert(T bean) {
		mongoTemplate.insert(bean);
	}

	/**
	 * 批量新增
	 * 
	 * @param beans
	 */
	@Override
	public void insertAll(List<T> beans) {
		mongoTemplate.insertAll(beans);
	}

	/**
	 * 更新对象，不存在则新增，存在则更新
	 * 
	 * @param query
	 * @param update
	 */
	@Override
	public WriteResult upsert(Query query, Update update) {
		return mongoTemplate.upsert(query, update, getEntityClass());
	}

	/**
	 * 更新对象，不存在不更新，存在则更新
	 * 
	 * @param query
	 * @param update
	 */
	@Override
	public T update(Query query, Update update) {
		return mongoTemplate.update(query, update, getEntityClass());
	}

	/**
	 * 删除对象
	 * 
	 * @param query
	 * @return
	 */
	@Override
	public WriteResult delete(Query query) {
		return mongoTemplate.delete(query, getEntityClass());
	}

	/**
	 * 删除全部
	 */
	@Override
	public void deleteAll() {
		mongoTemplate.deleteAll(getEntityClass());
	}

	/**
	 * 通过ID获取记录
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public T get(String id) {
		return mongoTemplate.get(id, getEntityClass());
	}

	/**
	 * 通过一定的条件查询一个实体
	 * 
	 * @param query
	 * @return
	 */
	@Override
	public T findOne(Query query) {
		return mongoTemplate.findOne(query, getEntityClass());
	}

	/**
	 * 根据条件查询
	 * 
	 * @param query
	 * @return
	 */
	@Override
	public List<T> find(Query query) {
		return mongoTemplate.find(query, getEntityClass());
	}

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @param pageQuery
	 * @return
	 */
	@Override
	public List<T> findByPage(Query query, PageQuery pageQuery) {
		return mongoTemplate.findByPage(query, pageQuery, getEntityClass());
	}

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	@Override
	public List<T> findAll() {
		return mongoTemplate.findAll(getEntityClass());
	}

	/**
	 * 查询总数
	 * 
	 * @param query
	 * @return
	 */
	@Override
	public long count(Query query) {
		return mongoTemplate.count(query, getEntityClass());
	}

	/**
	 * 执行原生的mongo命令
	 * 
	 * @param action
	 * @return
	 */
	@Override
	public T execute(DbCallback<T> action) {
		return mongoTemplate.execute(action);
	}

	/**
	 * 只有子类实例化后才能使用
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Class<T> getEntityClass() {
		Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return entityClass;
	}
}
