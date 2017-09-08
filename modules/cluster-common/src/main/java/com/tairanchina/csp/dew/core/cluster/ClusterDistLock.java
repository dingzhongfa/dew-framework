package com.tairanchina.csp.dew.core.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ClusterDistLock {

    Logger logger = LoggerFactory.getLogger(ClusterDistLock.class);

    void lockWithFun(VoidProcessFun fun) throws Exception;

    void tryLockWithFun(VoidProcessFun fun) throws Exception;

    void tryLockWithFun(long waitMillSec, VoidProcessFun fun) throws Exception;

    void tryLockWithFun(long waitMillSec, long leaseMillSec, VoidProcessFun fun) throws Exception;

    /**
     * 加锁，推荐使用 {@link #tryLock(long waitMillSec, long leaseMillSec)}
     */
    void lock();

    /**
     * 尝试加锁，推荐使用 {@link #tryLock(long waitMillSec, long leaseMillSec)}
     */
    boolean tryLock();

    /**
     * 尝试加锁，推荐使用 {@link #tryLock(long waitMillSec, long leaseMillSec)}
     *
     * @param waitMillSec 等待毫秒数
     */
    boolean tryLock(long waitMillSec) throws InterruptedException;

    /**
     * 尝试加锁
     *
     * @param waitMillSec  等待毫秒数
     * @param leaseMillSec 锁释放毫秒数
     */
    boolean tryLock(long waitMillSec, long leaseMillSec) throws InterruptedException;

    /**
     * 解锁操作，只有加锁的实例及线程才能解锁
     */
    boolean unLock();

    /**
     * 强制解锁，不用匹配加锁的实例与线程
     */
    void delete();

}
