package com.tairanchina.csp.dew.core.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ClusterDistLock {

    Logger logger = LoggerFactory.getLogger(ClusterDistLock.class);

    void lockWithFun(VoidProcessFun fun) throws Exception;

    void tryLockWithFun(VoidProcessFun fun) throws Exception;

    void tryLockWithFun(long waitMillSec, VoidProcessFun fun) throws Exception;

    void tryLockWithFun(long waitMillSec, long leaseMillSec, VoidProcessFun fun) throws Exception;

    void lock();

    boolean tryLock();

    boolean tryLock(long waitMillSec) throws InterruptedException;

    boolean tryLock(long waitMillSec, long leaseMillSec) throws InterruptedException;

    boolean unLock();

    void delete();

}
