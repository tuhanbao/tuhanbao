package com.tuhanbao.base.util.thread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.config.BaseConfigConstants;
import com.tuhanbao.base.util.config.Config;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.objutil.TimeUtil;

public final class ScheduledThreadManager
{
    private static int SCHEDULER_THREAD_NUM = 5;

    private static int MAX_SCHEDULER_THREAD_NUM = 20;
    
    private static int THREAD_NUM = 20;
    
    private static int MAX_THREAD_NUM = 30;
    
    private static final SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss");
    
    /**
     * 用于定时执行任务用
     */
    private static final ScheduledThreadPoolExecutor scheduler;
    
    /**
     * 用于
     */
    private static final ThreadPoolExecutor executor;
    
    private static List<Timer> timers = new ArrayList<Timer>();
    
    static
    {
    	Config baseConfig = ConfigManager.getBaseConfig();
		if (baseConfig != null) {
    		if (baseConfig.containsKey(BaseConfigConstants.SCHEDULER_THREAD_NUM))
    			SCHEDULER_THREAD_NUM = baseConfig.getInt(BaseConfigConstants.SCHEDULER_THREAD_NUM);
    		if (baseConfig.containsKey(BaseConfigConstants.MAX_SCHEDULER_THREAD_NUM))
    			MAX_SCHEDULER_THREAD_NUM = baseConfig.getInt(BaseConfigConstants.MAX_SCHEDULER_THREAD_NUM);
    		if (baseConfig.containsKey(BaseConfigConstants.THREAD_NUM))
    			THREAD_NUM = baseConfig.getInt(BaseConfigConstants.THREAD_NUM);
    		if (baseConfig.containsKey(BaseConfigConstants.MAX_THREAD_NUM))
    			MAX_THREAD_NUM = baseConfig.getInt(BaseConfigConstants.MAX_THREAD_NUM);
    	}
		
        scheduler = new ScheduledThreadPoolExecutor(SCHEDULER_THREAD_NUM);
        scheduler.setMaximumPoolSize(MAX_SCHEDULER_THREAD_NUM);
        
        executor = new ThreadPoolExecutor(THREAD_NUM, THREAD_NUM, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(MAX_THREAD_NUM - THREAD_NUM, true));
    }
    
    public static void execute(Runnable runnable)
    {
        executor.execute(runnable);
    }
    
    public static <T extends Object> Future<T> submit(Callable<T> command)
    {
        return executor.submit(command);
    }
    
    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long delay, long period, TimeUnit unit)
    {
        return scheduler.scheduleAtFixedRate(task, delay, period, unit);
    }

    
    /**
     * 批量执行任务，一次最多执行maxNumOnece条
     * @param tasks
     * @param maxNumOnece
     * @param timeout 每条任务的超时时间
     * @return
     */
    public static Map<Object, Object> executeBatch(List<BatchExcutorCallable> tasks, int maxNumOnece, long timeout)
    {
        return new BatchExcutor(maxNumOnece, timeout).excute(tasks);
    }
    
    /**
     * 批量执行任务，一次最多执行maxNumOnece条
     * @param tasks
     * @param maxNumOnece
     * @return
     */
    public static Map<Object, Object> executeBatch(List<BatchExcutorCallable> tasks, int maxNumOnece)
    {
        return new BatchExcutor(maxNumOnece).excute(tasks);
    }
    
    /**
     * 每天的第几分钟执行定时任务
     * 
     * Timer类不会讲task消耗的时间计算在内，每天都会准时执行
     * @param hour
     * @param min
    * @param sec
     * @param task
     */
    public static Timer executeOnTimer(int min, TimerTask task)
    {
        return executeOnTimer(min / 60, min % 60, 0, task);
    }
    
    /**
     * 多长时间后执行task，仅执行一次
     * 
     * 由于timer在执行一次后不会自动关闭，所以需要集成自己实现的task，在执行任务后准确关闭timmer
     * @param time
     * @param task
     * @return
     */
    public static Timer executeAfterTime(long time, ExcuteOnceTimerTask task)
    {
    	int hour = (int) (time / TimeUtil.HOUR2MILLS);
    	int min = (int) ((time % TimeUtil.HOUR2MILLS) / TimeUtil.MIN2MILL);
    	Timer timer = new Timer("TimerTask after " + hour + Constants.COLON + min);
    	task.setTimer(timer);
    	timer.schedule(task, time);
    	//执行一次的不需要加
//    	timers.add(timer);
    	return timer;
    }
    
    /**
     * 每天的几点几分执行定时任务
     * 
     * Timer类不会讲task消耗的时间计算在内，每天都会准时执行
     * @param hour
     * @param min
    * @param sec
     * @param task
     */
    public static Timer executeOnTimer(int hour, int min, int sec, TimerTask task)
    {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMin = calendar.get(Calendar.MINUTE);
        int currentSec = calendar.get(Calendar.SECOND);
        //今天是否过了时辰
        int now = currentHour * 3600 + currentMin * 60 + currentSec;
        int excuteTime = hour * 3600 + min * 60 + sec;
        boolean isPastTimeToday = (now >= excuteTime);
        
        //如果今天已经过了时辰，取明天的时刻作为首次运行
        if (isPastTimeToday)
        {
            hour += 24;
        }
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
        
        Date time = calendar.getTime();
        Timer timer = new Timer("TimerTask on " + SDF.format(time));
        timer.schedule(task, time, 24 * 60 * 60 * 1000L);
        
        timers.add(timer);
        return timer;
    }
    
    /**
     * 清理掉所有的定时器
     */
    public static void stopTimer()
    {
        for (Timer timer : timers)
        {
            timer.cancel();
        }
        timers.clear();
    }
    
}
