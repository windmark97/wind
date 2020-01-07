package com.wind.manager.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池
 * Created by hyj97 on 2018/12/14.
 */
public class ThreadPool {
    private static final Logger logger = LoggerFactory.getLogger(ThreadPool.class);
    private ThreadPoolExecutor executor = null;

    public ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, int dequeeMaxSize) {

        executor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                new LinkedBlockingDeque<Runnable>(dequeeMaxSize),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 提交任务到线程池执行
     * 当线程池的任务队列和线程都达到满值状态时。如果有新的任务提交到线程池，
     * 采用的处理程序策略为 ThreadPoolExecutor.CallerRunsPolicy
     * 即：让 调用submitTask(Runnable task)方法的线程，调用task.run()方法来运行此任务。
     *
     * @param task
     * @return Future [返回类型说明]
     * @throws throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Future submitTask(Runnable task) {

        try {
            //满值后不再抛出 RejectedExecutionException
            Future future = executor.submit(task);
            return future;
        } catch (Exception e) {
            //用于处理定位任务的run方法中抛出的异常
            logger.error("com.cmiot.hp.common.task submit task error：", e);
            throw new RuntimeException();
        }
    }

    /**
     * 执行任务
     *
     * @param cocmmand
     */
    public void execute(Runnable cocmmand) {
        if (executor != null) {
            executor.execute(cocmmand);
        }

    }

    /**
     * 关闭线程
     */
    public void shutdown() {
        if (executor != null) {
            executor.shutdown();
        }
    }
}
