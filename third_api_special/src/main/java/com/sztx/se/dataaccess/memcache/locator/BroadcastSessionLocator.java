package com.sztx.se.dataaccess.memcache.locator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.rubyeye.xmemcached.MemcachedSessionLocator;

import com.google.code.yanf4j.core.Session;

public class BroadcastSessionLocator implements MemcachedSessionLocator {

	private transient volatile List<Session> sessions = Collections.emptyList();
	private final Random rand = new Random();

	@Override
	public Session getSessionByKey(String key) {
		List<Session> copiedOnWrite = sessions;
		if (copiedOnWrite == null || copiedOnWrite.isEmpty())
			return null;
		return copiedOnWrite.get(rand.nextInt(copiedOnWrite.size()));
	}

	@Override
	public void updateSessions(Collection<Session> list) {
		this.sessions = new ArrayList<Session>(list);

	}

	@Override
	public void setFailureMode(boolean failureMode) {
		// ignore
	}

}
