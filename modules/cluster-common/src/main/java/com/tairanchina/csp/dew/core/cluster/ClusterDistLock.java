package com.tairanchina.csp.dew.core.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ClusterDistLock {

    Logger logger = LoggerFactory.getLogger(ClusterDistLock.class);

    void lockWithFun(VoidProcessFun fun) throws Exception;

    void tryLockWithFun(VoidProcessFun fun) throws Exception;

    void tryLockWithFun(int waitSec, VoidProcessFun fun) throws Exception;

    void lock();

    boolean tryLock();

    boolean tryLock(int waitSec) throws InterruptedException;

    boolean unLock();

    void delete();

}
