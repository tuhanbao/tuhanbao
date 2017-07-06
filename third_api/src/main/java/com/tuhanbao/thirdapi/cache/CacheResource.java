package com.tuhanbao.thirdapi.cache;

import java.util.Collection;
import java.util.List;

import com.tuhanbao.base.dataservice.IData;
import com.tuhanbao.base.dataservice.IDataGroup;
import com.tuhanbao.base.dataservice.ISyncData;
import com.tuhanbao.base.dataservice.filter.Filter;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.rm.IResource;
import com.tuhanbao.base.util.rm.IResourceManager;

public abstract class CacheResource extends IResource implements ICacheResource {
    
    public CacheResource(IResourceManager manager) {
        super(manager);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(IDataGroup<?> dg, Object key, Class<T> clazz) {
        Object result = get(dg, key);
        if (result == null) return null;
        return (T)result;
    }

    @Override
    public boolean exists(IDataGroup<?> dg, Object key) {
        return get(dg, key) != null;
    }

    @Override
    public int len(IDataGroup<?> dg) {
        //有过期要求的dg，无法准确统计其长度，可能这时候已经有好多数据已经过期了
        if (isExpire(dg)) {
            throw new MyException("you cannot get length for a expire CacheKey");
        }
        else {
            List<Object> list = this.get(dg);
            if (list == null) return 0;
            return list.size();
        }
    }

    @Override
    public boolean hasCacheDataGroup(IDataGroup<?> dg) {
        return this.get(dg) != null;
    }

    @Override
    public void destroy() {
        this.release();
    }
    
    protected static int getExpireTime(IDataGroup<?> dg) {
        if (isExpire(dg)) { 
            return ((ICacheKey)dg).getExpireTime();
        }
        
        return 0;
    }

    protected static boolean isExpire(IDataGroup<?> dg) {
        return ((dg instanceof ICacheKey) && ((ICacheKey)dg).isExpire());
    }

    @Override
    public void add(IData record) {
        this.save(record);
    }

    @Override
    public int delete(Filter filter) {
        throw new MyException("you canot use filter in CacheResource Methods.");
    }

    @Override
    public int deleteByKey(IDataGroup<?> dg, Object key) {
        return this.del(dg, key);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void delete(Collection list) {
        for (Object o : list) {
            this.del((IData)o);
        }
    }

    @Override
    public int update(IData record, Filter filter) {
        throw new MyException("you canot use filter in CacheResource Methods.");
    }

    @Override
    public int update(IData data) {
        return this.save(data);
    }

    @Override
    public int updateSelective(IData record, Filter filter) {
        throw new MyException("you canot use filter in CacheResource Methods.");
    }

    @Override
    public int updateSelective(IData data) {
        if (data instanceof ISyncData) {
            IDataGroup<?> dg = data.getDataGroup();
            ISyncData oldValue = (ISyncData)get(dg, data.getKeyValue());
            if (oldValue == null) save(data);
            else {
                oldValue.sync((ISyncData)data);
                save(oldValue);
            }
            return 0;
        }
        else {
            //没办法
            return this.update(data);
        }
    }

    @Override
    public IData selectByKey(IDataGroup<?> dg, Object key) {
        return (IData)this.get(dg, key);
    }

    @Override
    public List<IData> select(Filter filter) {
        throw new MyException("you canot use filter in CacheResource Methods.");
    }

    @Override
    public int del(IData data) {
        if (data == null) return 0;
        return this.del(data.getDataGroup(), data.getKeyValue());
    }

    protected int save(IData data) {
        if (data == null) return 0;
        return this.set(data.getDataGroup(), data.getKeyValue().toString(), data);
    }

}
