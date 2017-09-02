package com.tairanchina.csp.dew.core.cluster.spi.redis;

import com.tairanchina.csp.dew.core.cluster.Cluster;
import com.tairanchina.csp.dew.core.cluster.ClusterDistLock;
import com.tairanchina.csp.dew.core.cluster.VoidProcessFun;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisCommands;

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
        if (tryLock(waitMillSec, leaseMillSec)) {
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
        long now = new Date().getTime();
        while (new Date().getTime() - now < waitMillSec) {
            if (isLock()) {
                Thread.sleep(100);
            } else {
                if (tryLock()) {
                    return Boolean.TRUE;
                }
            }

        }
        return tryLock();
    }

    @Override
    public boolean tryLock(long waitMillSec, long leaseMillSec) throws InterruptedException {
        if (waitMillSec == 0 && leaseMillSec == 0) {
            return tryLock();
        } else if (leaseMillSec == 0) {
            return tryLock(waitMillSec);
        } else if (waitMillSec == 0) {
            return putLockKey(leaseMillSec);
        } else {
            long now = new Date().getTime();
            while (new Date().getTime() - now < waitMillSec) {
                if (isLock()) {
                    Thread.sleep(100);
                } else if (putLockKey(leaseMillSec)) {
                    return Boolean.TRUE;
                }
            }
            return putLockKey(leaseMillSec);
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

    private Boolean putLockKey(long leaseMillSec) {
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
        String res = ((JedisCommands) redisConnection.getNativeConnection()).set(key, currThreadId, "NX", "PX", leaseMillSec);
        redisConnection.close();
        return res != null && "OK".equalsIgnoreCase(res);
    }
}
