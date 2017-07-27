package com.sztx.se.dataaccess.hbase.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sztx.se.dataaccess.hbase.BaseHbaseDAO;
import com.sztx.se.dataaccess.hbase.serialize.HbaseSerializer;
import com.sztx.se.dataaccess.hbase.source.DynamicHbaseSource;

/**
 * 
 * @author zhihongp
 *
 */
public abstract class BaseHbaseDAOImpl implements BaseHbaseDAO {

	@Autowired(required = false)
	protected DynamicHbaseSource hbaseTemplate;

	@Override
	public byte[] serialize(Object obj) {
		return HbaseSerializer.serialize(obj);
	}

	@Override
	public <T> T deserialize(Class<T> type, byte[] bytes) {
		return HbaseSerializer.deserialize(type, bytes);
	}

	@Override
	public <T> List<T> deserializeArray(Class<T> type, byte[] bytes) {
		return HbaseSerializer.deserializeArray(type, bytes);
	}
}
