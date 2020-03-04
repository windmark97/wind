package com.wind.manager.utils.lock;

import java.util.concurrent.TimeUnit;

/**
 * 自定义锁
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/3/4 15:42
 **/
public interface DistributedLock {
    /**
     * <p>仅在调用时锁为空闲状态才获取该锁。
     * <p>如果锁可用，则获取锁，并立即返回值 true。如果锁不可用，则此方法将立即返回值 false。
     * <p>使用如下:
     * <pre> {@code
     *  DistributedLock lock = ...;
     *   if (lock.tryLock()) {
     *      try {
     *          // ...
     *      } finally {
     *           lock.unlock();
     *      }
     *   } else {
     *         // ...
     *   }
     * }</pre>
     *
     * @return 如果获取了锁，则返回 true；否则返回 false。
     */
    boolean tryLock();

    /**
     * 如果在超时时间内成功获取锁返回true，否则返回false
     *
     * @return 如果获取了锁，则返回 true；否则返回 false。
     */
    boolean lock();

    /**
     * 如果在传入超时时间内成功获取锁返回true，否则返回false
     *
     * @param timeout  获取锁超时时间
     * @param timeUnit 时间单位
     * @return 如果在传入超时时间内成功获取锁返回true，否则返回false
     */
    boolean lock(long timeout, TimeUnit timeUnit);

    /**
     * 释放锁; DistributedLock的实现通常对哪个分布式节点可以释放锁施加了限制（不区分线程，只有持有锁对象就可以释放它）;如果违背了这个原则可能会抛出运行时异常。
     */
    void unlock();
}
