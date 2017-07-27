/**
 * 
 */
package com.sztx.se.dataaccess.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sztx.se.dataaccess.mysql.config.FilterRule;
import com.sztx.se.dataaccess.mysql.config.PageQuery;

/**
 * 
 * @author zhihongp
 * 
 * @param <T>
 */
public interface BaseMysqlDAO<T> {

	/**
	 * 新增一条数据
	 * 
	 * @param t
	 * @return
	 */
	Integer save(T t);

	/**
	 * 更新一条数据
	 * 
	 * @param t
	 * @return
	 */
	int update(T t);

	/**
	 * 删除一条数据
	 * 
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 根据id查找一条数据
	 * 
	 * @param id
	 * @return
	 */
	T findById(Integer id);

	/**
	 * 查找所有
	 * 
	 * @return
	 */
	List<T> findAll(@Param("filterRules") List<FilterRule> filterRules);

	/**
	 * 删除所有
	 * 
	 * @return
	 */
	void deleteAll(@Param("filterRules") List<FilterRule> filterRules);

	/**
	 * 分页查找
	 * 
	 * @param filterRules
	 * @param pageQuery
	 * @return
	 */
	List<T> findByPage(@Param("filterRules") List<FilterRule> filterRules, @Param("pageQuery") PageQuery pageQuery);

	/**
	 * 获取个数
	 * 
	 * @param filterRules
	 * @return
	 */
	Integer getTotalCount(@Param("filterRules") List<FilterRule> filterRules);
}
