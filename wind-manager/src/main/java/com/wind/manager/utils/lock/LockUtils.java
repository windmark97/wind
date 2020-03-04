package com.wind.manager.utils.lock;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/2/14 14:51
 **/
public class LockUtils {

    private static volatile ThreadLocal<DistributedLock> threadlocal = new ThreadLocal();


    /**
     * 获取分布式锁对象
     * @param key
     * @param validTime
     * @return
     */
    public static DistributedLock getDistributedLock(String key, int validTime) {
        DistributedLock lock = getThreadLocalValue();
        if (lock == null) {
            lock = DistributedLockUtils.buildReentrantLock(key, validTime);
            setThreadlocalValue(lock);
        }
        return lock;
    }

    /**
     * 设置value
     *
     * @param lock
     */
    private static void setThreadlocalValue(DistributedLock lock) {
        threadlocal.set(lock);
    }

    /**
     * 获取value
     *
     * @return
     */
    public static DistributedLock getThreadLocalValue() {
        return threadlocal.get();
    }

    /**
     * 移除
     */
    public static void removeThreadLocalValue() {
        threadlocal.remove();
    }

}
