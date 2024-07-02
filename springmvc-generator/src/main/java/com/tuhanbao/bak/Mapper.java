package {package};

import {base_project_head}.base.dataservice.SeqBean;
import {base_project_head}.base.dataservice.ServiceBean;
import {base_project_head}.base.dataservice.cache.CacheEntry;
import {base_project_head}.base.dataservice.filter.Filter;
import {base_project_head}.base.util.db.table.Column;
import {base_project_head}.base.util.db.table.MathFunc;
import {base_project_head}.base.util.db.table.Table;
import {base_project_head}.web.db.MyBatisSqlSentence;
import {base_project_head}.web.filter.MyBatisSelectorFilter;
import {base_project_head}.web.filter.TableSelector;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface {mapper.name}<T extends ServiceBean> {
    int insert(T model);

    int deleteByFilter(@Param("table") Table table, @Param("filter") Filter filter);

    int deleteRelative(MyBatisSelectorFilter selector);

    int updateByFilter(@Param("model") T model, @Param("tableName") String tableName, @Param("filter") Filter filter);

    int updateSelective(@Param("model") T model, @Param("tableName") String tableName, @Param("filter") Filter filter);

    int updateRelative(@Param("selector") TableSelector selector, @Param("filter") Filter filter, @Param("models") T... models);

    int countByFilter(MyBatisSelectorFilter selector);

    Object executeMathFuncByFilter(@Param("func") MathFunc func, @Param("col") Column col,
                                   @Param("selector") TableSelector selector, @Param("filter") Filter filter);

    List<Map<?, ?>> select(MyBatisSelectorFilter selector);

    void deleteByFilterCutTable(@Param("table") Table table, @Param("filter") Filter filter);

    void updateByFilterCutTable(@Param("model") T model, @Param("table") Table table, @Param("filter") Filter filter);

    void updateSelectiveCutTable(@Param("model") T model, @Param("table") Table table, @Param("filter") Filter filter);

    List<Object> countByFilterCutTable(MyBatisSelectorFilter selector);

    List<Object> executeMathFuncByFilterCutTable(@Param("func") MathFunc func, @Param("col") Column col,
                                   @Param("selector") TableSelector selector, @Param("filter") Filter filter);

    List<Map<?, ?>> selectCutTable(MyBatisSelectorFilter selector);
    
    List<Map<String, Object>> excuteSql(MyBatisSqlSentence sqlSentece);
    
    SeqBean nextVal(String seqName);

    void excuteBatch(List<CacheEntry<ServiceBean>> list);
}