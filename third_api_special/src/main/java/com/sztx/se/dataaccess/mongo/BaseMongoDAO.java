package com.sztx.se.dataaccess.mongo;

import java.util.List;

import org.springframework.data.mongodb.core.DbCallback;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;
import com.sztx.se.dataaccess.mongo.config.PageQuery;

/**
 * 
 * @author zhihongp
 *
 */
public interface BaseMongoDAO<T> {
	
	void save(T bean);
	
	void insert(T bean);
	
	void insertAll(List<T> beans);
	
	WriteResult upsert(Query query, Update update);
	
	T update(Query query, Update update);
	
	WriteResult delete(Query query);
	
	void deleteAll();
	
	T get(String id);
	
	T findOne(Query query);
	
	List<T> find(Query query);
	
	List<T> findByPage(Query query, PageQuery pageQuery);
	
	List<T> findAll();
	
	long count(Query query);
	
	T execute(DbCallback<T> action);
}
