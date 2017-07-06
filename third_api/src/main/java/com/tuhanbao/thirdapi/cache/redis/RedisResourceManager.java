package com.tuhanbao.thirdapi.cache.redis;

import java.sql.SQLException;

import com.tuhanbao.thirdapi.cache.ICacheResource;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.rm.IResource;
import com.tuhanbao.base.util.rm.IResourceManager;
import com.tuhanbao.thirdapi.cache.CacheResourceFactory;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisResourceManager extends IResourceManager implements CacheResourceFactory {
	protected JedisPool jedisPool;
	
	private static final RedisResourceManager INSTANCE = new RedisResourceManager();

	private RedisResourceManager() {
	    super(0, 1, -1, false);

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        if (RedisConfig.MAX_ACTIVE != -1) jedisPoolConfig.setMaxTotal(RedisConfig.MAX_ACTIVE);
        if (RedisConfig.MAX_IDLE != -1) jedisPoolConfig.setMaxIdle(RedisConfig.MAX_IDLE);
        if (RedisConfig.MAX_WAIT != -1) jedisPoolConfig.setMaxWaitMillis(RedisConfig.MAX_WAIT);
        jedisPoolConfig.setTestOnBorrow(RedisConfig.TEST_ON_BORROW);

        jedisPool = new JedisPool(jedisPoolConfig, RedisConfig.IP, RedisConfig.PORT, RedisConfig.TIME_OUT, RedisConfig.PASSWORD, RedisConfig.DATABASE,
                RedisConfig.CLIENT_NAME);
	}
	
	public static final RedisResourceManager getInstance() {
	    return INSTANCE;
	}
	
    /**
     * 外部不允许使用
     * 
     * 除非你很清楚什么时候该释放连接
     * @return
     * @throws SQLException
     */
    @Override
    public synchronized IResource getResource() throws Exception {
        return new RedisResource(this, jedisPool.getResource());
    }

    @Override
    protected IResource createResource() throws Exception {
        throw new MyException("can not invoke this method!");
    }

    @Override
    public ICacheResource getCacheResource() {
        try {
            return (RedisResource)getResource();
        }
        catch (Exception e) {
            throw MyException.getMyException(e);
        }
    }
    
}
