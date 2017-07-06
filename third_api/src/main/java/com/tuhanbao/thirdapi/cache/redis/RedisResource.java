package com.tuhanbao.thirdapi.cache.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONObject;
import com.tuhanbao.base.dataservice.IDataGroup;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.json.JsonUtil;
import com.tuhanbao.thirdapi.cache.CacheResource;

import redis.clients.jedis.Jedis;

/**
 * redis的hget和hset有两级key，但是无法做到expire控制
 * 
 * 所以很多两级相关的方法对于设置过期的IDataGroup来说无效
 * 
 * @author Administrator
 *
 */
public class RedisResource extends CacheResource {

    private Jedis jedis;

    private static final String OK = "OK";

    public RedisResource(RedisResourceManager manager, Jedis jedis) {
        super(manager);
        this.jedis = jedis;
    }

    @Override
    public int set(IDataGroup<?> dg, Object key, Object value) {
        Object jsonObject = JsonUtil.toJSON(value);
        int expireTime = getExpireTime(dg);
        if (expireTime > 0) { 
            return setex(dg, key.toString(), expireTime, jsonObject.toString());
        }
        else {
            return (int)jedis.hset(dg.getName(), key.toString(), jsonObject.toString()).longValue();
        }
    }

    @Override
    public void release() {
        jedis.close();
        super.release();
    }

    @Override
    public int setex(IDataGroup<?> dg, Object key, int seconds, Object value) {
        Object jsonObject = JsonUtil.toJSON(value);
        if (seconds > 0) {
            String result = jedis.setex(getKey(dg, key.toString()), seconds, jsonObject.toString());
            return isSuccess(result) ? 1 : -1;
        }
        throw new MyException("you cannot expire a value for a not expire ICacheKey");
    }

    @Override
    public int del(IDataGroup<?> dg, Object key) {
        if (isExpire(dg)) {
            return (int)jedis.del(getKey(dg, key.toString())).longValue();
        }
        else {
            return (int)jedis.hdel(dg.getName(), key.toString()).longValue();
        }
    }
    

    @Override
    public Object get(IDataGroup<?> dg, Object key) {
        if (getExpireTime(dg) > 0) {
            return jedis.get(getKey(dg, key.toString()));
        }
        else {
            Class<?> modelClassName = dg.getModelClassName();
            if (modelClassName != null) {
                return get(dg, key, modelClassName);
            }
            else {
                return jedis.hget(dg.getName(), key.toString());
            }
        }
    }

    @Override
    public <T> T get(IDataGroup<?> dg, Object key, Class<T> clazz) {
        Object result = get(dg, key);
        if (result == null) return null;
        return JsonUtil.getBean(JSONObject.parseObject(result.toString()), clazz);
    }

    @Override
    public boolean exists(IDataGroup<?> dg, Object key) {
        if (isExpire(dg)) {
            return jedis.exists(getKey(dg, key.toString()));
        }
        else {
            return jedis.hexists(dg.getName(), key.toString());
        }
    }

    @Override
    public int len(IDataGroup<?> dg) {
        if (isExpire(dg)) {
            throw new MyException("you cannot get length for a expire ICacheKey");
        }
        else {
            //重写父类会节约很多性能
            return (int)jedis.hlen(dg.getName()).longValue();
        }
    }

    @Override
    public List<Object> get(IDataGroup<?> dg) {
        if (isExpire(dg)) {
            throw new MyException("you cannot get length for a expire ICacheKey");
        }
        else {
            Map<String, String> result = jedis.hgetAll(dg.getName());
            List<Object> list = new ArrayList<>(result.size());
            for (Entry<String, String> entry : result.entrySet()) {
                Class<?> modelClassName = dg.getModelClassName();
                if (modelClassName == null) {
                    list.add(JSONObject.parseObject(entry.getValue()));
                }
                else {
                    Object object = JsonUtil.getBean(JSONObject.parseObject(entry.getValue()), modelClassName);
                    list.add(object);
                }
            }
            return list;
        }
    }

    @Override
    public boolean hasCacheDataGroup(IDataGroup<?> dg) {
        return jedis.exists(dg.getName());
    }

    private static boolean isSuccess(String result) {
        return OK.equals(result);
    }

    private static final String getKey(IDataGroup<?> ck, String key) {
        return ck.getName() + "_" + key;
    }

    @Override
    public void destroy() {
        this.release();
    }

    @Override
    public void recreateResource() throws Exception {
        this.jedis = ((RedisResourceManager)manager).jedisPool.getResource();
    }

}
