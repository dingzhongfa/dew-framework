package com.tairanchina.csp.dew.core.cluster.spi.redis;

import com.tairanchina.csp.dew.core.cluster.Cluster;
import com.tairanchina.csp.dew.core.cluster.ClusterDistLock;
import com.tairanchina.csp.dew.core.cluster.VoidProcessFun;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RedisClusterDistLock implements ClusterDistLock {

    private String key;
    private String currThreadId;
    private RedisTemplate<String, String> redisTemplate;

    RedisClusterDistLock(String key, RedisTemplate<String, String> redisTemplate) {
        this.key = "dew:dist:lock:" + key;
        currThreadId = Cluster.CLASS_LOAD_UNIQUE_FLAG + "-" + Thread.currentThread().getId();
        this.redisTemplate = redisTemplate;
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
        if (tryLock(waitMillSec,leaseMillSec)) {
            try {
                fun.exec();
            } finally {
                unLock();
            }
        }
    }

    @Override
    public void lock() {
        redisTemplate.opsForValue().setIfAbsent(key, currThreadId);
    }

    @Override
    public boolean tryLock() {
        return redisTemplate.opsForValue().setIfAbsent(key, currThreadId);
    }

    @Override
    public boolean tryLock(long waitMillSec) throws InterruptedException {
        synchronized (this) {
            long now = new Date().getTime();
            while (isLock() && new Date().getTime() - now < waitMillSec) {
                Thread.sleep(100);
            }
            return tryLock();
        }
    }

    @Override
    public boolean tryLock(long waitMillSec, long leaseMillSec) throws InterruptedException {
        if (waitMillSec == 0 && leaseMillSec == 0) {
            return tryLock();
        } else if (leaseMillSec == 0) {
            return tryLock(waitMillSec);
        } else if (waitMillSec == 0) {
            return redisTemplate.getConnectionFactory().getConnection()..set(key.getBytes(),currThreadId.getBytes(),"NX","EX",leaseMillSec);
        } else {
            synchronized (this) {
                long now = new Date().getTime();
                while (isLock() && new Date().getTime() - now < waitMillSec) {
                    Thread.sleep(100);
                }
                return redisTemplate.getConnectionFactory().getConnection().set(key,currThreadId,"NX","EX",leaseMillSec);
            }
        }
    }

    @Override
    public boolean unLock() {
        if (currThreadId.equals(redisTemplate.opsForValue().get(key))) {
            redisTemplate.delete(key);
            return true;
        } else {
            return false;
        }
    }

    private boolean isLock() {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void delete() {
        redisTemplate.delete(key);
    }
}
