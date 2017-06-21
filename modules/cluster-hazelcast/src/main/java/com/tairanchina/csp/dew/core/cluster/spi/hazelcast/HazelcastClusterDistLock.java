package com.tairanchina.csp.dew.core.cluster.spi.hazelcast;

import com.tairanchina.csp.dew.core.cluster.ClusterDistLock;
import com.tairanchina.csp.dew.core.cluster.VoidProcessFun;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;

import java.util.concurrent.TimeUnit;

public class HazelcastClusterDistLock implements ClusterDistLock {

    private ILock lock;

    HazelcastClusterDistLock(String key, HazelcastInstance hazelcastInstance) {
        lock = hazelcastInstance.getLock("dew:dist:lock:" + key);
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
    public void tryLockWithFun(int waitSec, VoidProcessFun fun) throws Exception {
        if (tryLock(waitSec)) {
            try {
                fun.exec();
            } finally {
                unLock();
            }
        }
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
    public boolean tryLock(int waitSec) throws InterruptedException {
        if (waitSec == 0) {
            return lock.tryLock();
        } else {
            return lock.tryLock(waitSec, TimeUnit.SECONDS);
        }
    }

    @Override
    public boolean unLock() {
        lock.unlock();
        return true;
    }

    @Override
    public void delete() {
        lock.forceUnlock();
    }
}
