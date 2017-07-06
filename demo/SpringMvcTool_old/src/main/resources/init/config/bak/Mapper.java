package {package};

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tuhanbao.util.db.table.Table;
import com.tuhanbao.web.db.MybatisSqlSentence;
import com.tuhanbao.web.filter.SelectorFilter;
import com.tuhanbao.base.dataservice.SeqBean;
import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.base.dataservice.filter.Filter;

public interface {mapper.name}<T extends ServiceBean> {
    int insert(T model);

    int deleteByFilter(@Param("table") Table table, @Param("filter") Filter filter);

    int updateByFilter(@Param("model") T model, @Param("filter") Filter filter);

    int updateSelective(@Param("model") T model, @Param("filter") Filter filter);

    int countByFilter(SelectorFilter selector);

    List<T> select(SelectorFilter selector);
    
    List<Map<String, Object>> excuteSql(MybatisSqlSentence sqlSentece);
    
    SeqBean nextVal(String seqName); 
}