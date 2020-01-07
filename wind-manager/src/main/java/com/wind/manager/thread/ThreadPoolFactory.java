package com.wind.manager.thread;



import com.wind.manager.constant.ThreadPoolTypeEnum;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工厂类
 * Created by hyj97 on 2018/12/14.
 */
public class ThreadPoolFactory {
    public static ThreadPoolFactory threadPoolFactory = null;

    private static int corePoolSize;

    //private static int corePoolSize = Runtime.getRuntime().availableProcessors() + 1;
    private static int maximumPoolSize;

    private static long keepAliveTime;

    //队列大小
    private static int dequeMaxSize;

    private static ConcurrentHashMap<Integer, ThreadPool> threadPoolMap = new ConcurrentHashMap<>();

    private ThreadPoolFactory(){

    }
    public static void setParam(int corePoolSize,int maximumPoolSize,long keepAliveTime,int dequeMaxSize){
        ThreadPoolFactory.corePoolSize = corePoolSize;
        ThreadPoolFactory.maximumPoolSize = maximumPoolSize;
        ThreadPoolFactory.keepAliveTime = keepAliveTime;
        ThreadPoolFactory.dequeMaxSize = dequeMaxSize;
    }
    /**
     * 获取ThreadPoolFactory
     *
     * @return
     */
    public static ThreadPoolFactory getInstance() {
        if (threadPoolFactory == null) {
            threadPoolFactory = new ThreadPoolFactory();
        }
        return threadPoolFactory;
    }



    /**
     * 获取线程池
     *
     * @param key
     * @return
     */
    public ThreadPool getThreadPool(ThreadPoolTypeEnum key) {
        if (threadPoolMap.get(key.getType()) == null) {
            synchronized (ThreadPoolFactory.class) {
                if(threadPoolMap.get(key.getType()) == null){
                    threadPoolMap.put(key.getType(), new ThreadPool(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, dequeMaxSize));
                }
            }

        }
        return threadPoolMap.get(key.getType());
    }

}
