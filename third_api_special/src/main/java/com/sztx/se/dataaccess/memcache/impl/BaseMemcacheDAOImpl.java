package com.sztx.se.dataaccess.memcache.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.sztx.se.dataaccess.memcache.BaseMemcacheDAO;
import com.sztx.se.dataaccess.memcache.source.DynamicMemcacheSource;

public abstract class BaseMemcacheDAOImpl implements BaseMemcacheDAO {
	
	@Autowired(required = false)
	protected DynamicMemcacheSource memcacheTemplate;
	
}
