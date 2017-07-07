package {package};

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tuhanbao.base.dataservice.SeqBean;
import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.base.dataservice.filter.Filter;
import com.tuhanbao.base.util.db.table.Table;
import com.tuhanbao.web.db.MyBatisSqlSentence;
import com.tuhanbao.web.filter.MyBatisSelectorFilter;

public interface {mapper.name}<T extends ServiceBean> {
    int insert(T model);

    int deleteByFilter(@Param("table") Table table, @Param("filter") Filter filter);

    int updateByFilter(@Param("model") T model, @Param("filter") Filter filter);

    int updateSelective(@Param("model") T model, @Param("filter") Filter filter);

    int countByFilter(MyBatisSelectorFilter selector);

    List<T> select(MyBatisSelectorFilter selector);
    
    List<Map<String, Object>> excuteSql(MyBatisSqlSentence sqlSentece);
    
    SeqBean nextVal(String seqName); 
}