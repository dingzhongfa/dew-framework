package com.tairanchina.csp.dew.core.cluster.spi.ignite;

import com.tairanchina.csp.dew.core.cluster.ClusterDistLock;
import com.tairanchina.csp.dew.core.cluster.VoidProcessFun;
import org.apache.ignite.Ignite;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class IgniteClusterDistLock implements ClusterDistLock {

    private Lock lock;
    private Ignite ignite;
    private String key;

    IgniteClusterDistLock(String key, Ignite ignite) {
        this.ignite = ignite;
        this.key = "dew:dist:lock:" + key;
        lock = ignite.getOrCreateCache("dew:dist:lock:" + key).lock(key);
    }

    @Override
    public void lockWithFun(VoidProcessFun fun) throws Exception {
        try {
            lock();
            fun.exec();
        } finally {
            unLock();
        }
    }

    @Override
    public void tryLockWithFun(VoidProcessFun fun) throws Exception {
        tryLockWithFun(0, fun);
    }


    @Override
    public void tryLockWithFun(long waitMillSec, VoidProcessFun fun) throws Exception {
        if (tryLock(waitMillSec)) {
            try {
                fun.exec();
            } finally {
                unLock();
            }
        }
    }

    @Override
    public void tryLockWithFun(long waitMillSec, long leaseMillSec, VoidProcessFun fun) throws Exception {

    }

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public boolean tryLock() {
        return lock.tryLock();
    }

    @Override
    public boolean tryLock(long waitMillSec) throws InterruptedException {
        if (waitMillSec == 0) {
            return lock.tryLock();
        } else {
            return lock.tryLock(waitMillSec, TimeUnit.SECONDS);
        }
    }

    @Override
    public boolean tryLock(long waitMillSec, long leaseMillSec) throws InterruptedException {
        return false;
    }

    @Override
    public boolean unLock() {
        try{
            lock.unlock();
            return true;
        }catch (IllegalStateException e){
            logger.error("Ignite Unlock error.",e);
            return false;
        }
    }

    @Override
    public void delete() {
        ignite.destroyCache(key);
    }
}
