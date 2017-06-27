package com.tairanchina.csp.dew.core.cluster.spi.redis;

import com.tairanchina.csp.dew.core.cluster.Cluster;
import com.tairanchina.csp.dew.core.cluster.ClusterDistLock;
import com.tairanchina.csp.dew.core.cluster.VoidProcessFun;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

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
        redisTemplate.opsForValue().setIfAbsent(key, currThreadId);
    }

    @Override
    public boolean tryLock() {
        return redisTemplate.opsForValue().setIfAbsent(key, currThreadId);
    }

    @Override
    public boolean tryLock(int waitSec) throws InterruptedException {
        synchronized (this) {
            if (waitSec == 0) {
                return tryLock();
            } else {
                long now = new Date().getTime();
                while (isLock() && new Date().getTime() - now < waitSec) {
                    Thread.sleep(500);
                }
                return tryLock();
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
