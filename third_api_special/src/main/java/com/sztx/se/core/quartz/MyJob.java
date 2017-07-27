package com.sztx.se.core.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.TimeUtil;

public class MyJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(TimeUtil.now() + "i'm job!");
    }
    
}