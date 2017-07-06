package com.tuhanbao.web;


import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public abstract class ServerManager implements ServletContextListener
{
    private static AtomicBoolean SERVER_IS_BUSY = new AtomicBoolean(false);
    
    //子类重写
    public abstract void init();

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		init();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
	
	public static void setServerIsBusy(boolean value) {
	    SERVER_IS_BUSY.set(value);
	}
	
	public static boolean serverIsBusy() {
        return SERVER_IS_BUSY.get();
    }
}
