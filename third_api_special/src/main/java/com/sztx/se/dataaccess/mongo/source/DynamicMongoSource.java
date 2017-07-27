package com.sztx.se.dataaccess.mongo.source;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.DbCallback;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import com.mongodb.WriteResult;
import com.sztx.se.dataaccess.mongo.config.PageQuery;
import com.sztx.se.dataaccess.mongo.template.MongoTemplateProxy;
import com.tuhanbao.base.util.exception.MyException;

/**
 * 动态mongodb
 * 
 * @author zhihongp
 * @param <T>
 * 
 */
public class DynamicMongoSource {

	private MongoTemplateProxy defaultTargetMongoSource;

	private Map<String, MongoTemplateProxy> targetMongoSources;

	public MongoTemplateProxy getDefaultTargetMongoSource() {
		return defaultTargetMongoSource;
	}

	public void setDefaultTargetMongoSource(MongoTemplateProxy defaultTargetMongoSource) {
		this.defaultTargetMongoSource = defaultTargetMongoSource;
	}

	public Map<String, MongoTemplateProxy> getTargetMongoSources() {
		return targetMongoSources;
	}

	public void setTargetMongoSources(Map<String, MongoTemplateProxy> targetMongoSources) {
		this.targetMongoSources = targetMongoSources;
	}

	public void afterPropertiesSet() {
		Set<Entry<String, MongoTemplateProxy>> set = targetMongoSources.entrySet();

		for (Map.Entry<String, MongoTemplateProxy> entry : set) {
			MongoTemplateProxy mongoTemplateProxy = entry.getValue();
			MongoTemplate mongoTemplate = mongoTemplateProxy.getMongoTemplate();
			Assert.notNull(mongoTemplate, " a valid mongod db is required");
		}
	}

	/**
	 * 保存或更新一个对象
	 * 
	 * @param <T>
	 * 
	 * @param bean
	 * @return
	 */
	public <T> void save(T bean) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		mongoTemplate.save(bean);
	}

	/**
	 * 新增一个对象
	 * 
	 * @param <T>
	 * 
	 * @param bean
	 */
	public <T> void insert(T bean) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		mongoTemplate.insert(bean);
	}

	/**
	 * 批量新增
	 * 
	 * @param <T>
	 * 
	 * @param beans
	 */
	public <T> void insertAll(List<T> beans) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		mongoTemplate.insertAll(beans);
	}

	/**
	 * 更新对象，不存在则新增，存在则更新
	 * 
	 * @param <T>
	 * 
	 * @param <T>
	 * 
	 * @param query
	 * @param update
	 */
	public <T> WriteResult upsert(Query query, Update update, Class<T> type) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		return mongoTemplate.upsert(query, update, type);
	}

	/**
	 * 更新对象，不存在不更新，存在则更新
	 * 
	 * @param query
	 * @param update
	 */
	public <T> T update(Query query, Update update, Class<T> type) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		return mongoTemplate.findAndModify(query, update, type);
	}

	/**
	 * 删除对象
	 * 
	 * @param <T>
	 * 
	 * @param query
	 */
	public <T> WriteResult delete(Query query, Class<T> type) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		return mongoTemplate.remove(query, type);
	}

	/**
	 * 删除全部
	 * 
	 * @param <T>
	 */
	public <T> void deleteAll(Class<T> type) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		mongoTemplate.dropCollection(type);
	}

	/**
	 * 通过ID获取记录
	 * 
	 * @param <T>
	 * 
	 * @param id
	 * @return
	 */
	public <T> T get(String id, Class<T> type) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		return mongoTemplate.findById(id, type);
	}

	/**
	 * 通过一定的条件查询一个实体
	 * 
	 * @param <T>
	 * 
	 * @param query
	 * @return
	 */
	public <T> T findOne(Query query, Class<T> type) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		return mongoTemplate.findOne(query, type);
	}

	/**
	 * 根据条件查询
	 * 
	 * @param <T>
	 * 
	 * @param query
	 * @return
	 */
	public <T> List<T> find(Query query, Class<T> type) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		return mongoTemplate.find(query, type);
	}

	/**
	 * 分页查询
	 * 
	 * @param <T>
	 * 
	 * @param query
	 * @param pageQuery
	 * @return
	 */
	public <T> List<T> findByPage(Query query, PageQuery pageQuery, Class<T> type) {
		MongoTemplate mongoTemplate = getMongoTemplate();

		if (pageQuery != null) {
			query.skip(pageQuery.getStart());
			query.limit(pageQuery.getPageSize());
			List<Sort> sortList = pageQuery.getSortList();

			if (sortList != null && !sortList.isEmpty()) {
				for (Sort sort : sortList) {
					query.with(sort);
				}
			}
		}

		return mongoTemplate.find(query, type);
	}

	/**
	 * 查询所有
	 * 
	 * @param <T>
	 * 
	 * @return
	 */
	public <T> List<T> findAll(Class<T> type) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		return mongoTemplate.findAll(type);
	}

	/**
	 * 查询总数
	 * 
	 * @param <T>
	 * 
	 * @param query
	 * @return
	 */
	public <T> long count(Query query, Class<T> type) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		return mongoTemplate.count(query, type);
	}

	/**
	 * 执行原生的mongo命令
	 * 
	 * @param action
	 * @return
	 */
	public <T> T execute(DbCallback<T> action) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		return mongoTemplate.execute(action);
	}

	private MongoTemplate getMongoTemplate() {
		String mongoSourceType = MongoSourceSwitcher.getMongoSourceType();
		MongoTemplate mongoTemplate = null;

		if (StringUtils.isNotBlank(mongoSourceType)) {
			MongoTemplateProxy mongoTemplateProxy = targetMongoSources.get(mongoSourceType);

			if (mongoTemplateProxy != null) {
				mongoTemplate = mongoTemplateProxy.getMongoTemplate();
			}
		}

		if (mongoTemplate == null) {
			mongoTemplate = defaultTargetMongoSource.getMongoTemplate();
		}

		if (mongoTemplate == null) {
			throw new MyException("Can not get a mongoTemplate!");
		}

		return mongoTemplate;
	}

}
