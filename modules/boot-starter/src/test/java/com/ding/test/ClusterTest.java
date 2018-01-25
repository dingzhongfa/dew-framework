package com.ding.test;

import com.ecfront.dew.common.$;
import com.tairanchina.csp.dew.core.cluster.spi.redis.RedisClusterCache;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * desription:
 * Created by ding on 2018/1/24.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BootTestApplicationWithAnnotation.class)
public class ClusterTest {

    private String valueTest = "value_test";

    private String hashTest = "hash_test";

    private String listTest = "list_test";

    private String intTest = "int_test";

    private String setTest = "set_test";
    
    @Autowired
    @Qualifier("redisClusterCache")
    private RedisClusterCache redisClusterCache;

    @Test
    public void testCache() throws InterruptedException {
        Assert.assertTrue(true);
        redisClusterCache.flushdb();
        redisClusterCache.setex(valueTest, "{\"name\":\"jzy\"}", 1L);
        Assert.assertTrue(redisClusterCache.exists(valueTest));
        redisClusterCache.del(valueTest);
        Assert.assertTrue(!redisClusterCache.exists(valueTest));
        redisClusterCache.setex(valueTest, "{\"name\":\"jzy\"}", 2L);
        Assert.assertTrue(redisClusterCache.exists(valueTest));
        Assert.assertEquals("jzy", $.json.toJson(redisClusterCache.get(valueTest)).get("name").asText());
        Thread.sleep(1000);
        long timeLeft = redisClusterCache.ttl(valueTest);
        Assert.assertTrue(timeLeft == 1); // 返回剩余时间
        Thread.sleep(1000);
        Assert.assertTrue(!redisClusterCache.exists(valueTest));
        redisClusterCache.setex(valueTest, "{\"name\":\"jzy\"}", 1L);
        Assert.assertTrue(redisClusterCache.exists(valueTest));
        Assert.assertTrue(!redisClusterCache.setnx(valueTest, "存入失败", 1L));
        String val = redisClusterCache.getSet(valueTest, "{\"name\":\"dzf\"}");
        Assert.assertEquals("jzy", $.json.toJson(val).get("name").asText());
        Thread.sleep(1000);
        Assert.assertTrue(redisClusterCache.exists(valueTest));
        redisClusterCache.expire(valueTest, 1L);
        Thread.sleep(1000);
        Assert.assertTrue(!redisClusterCache.exists(valueTest));
        redisClusterCache.del(hashTest);
        redisClusterCache.hmset(hashTest, new HashMap<String, String>() {{
            put("f1", "v1");
            put("f2", "v2");
        }});
        Set<String> set = redisClusterCache.hkeys(hashTest);
        Assert.assertEquals(2, set.size());
        Assert.assertTrue(set.containsAll(new ArrayList<String>() {{
            add("f1");
            add("f2");
        }}));
        Assert.assertTrue(redisClusterCache.hvalues(hashTest).contains("v1"));
        redisClusterCache.hset(hashTest, "f3", "v3");
        Assert.assertEquals("v3", redisClusterCache.hget(hashTest, "f3"));
        Assert.assertEquals("v2", redisClusterCache.hget(hashTest, "f2"));
        Assert.assertEquals(null, redisClusterCache.hget(hashTest, "notexist"));
        Assert.assertTrue(redisClusterCache.hexists(hashTest, "f3"));
        Assert.assertEquals(3, redisClusterCache.hlen(hashTest));
        Map<String, String> hashVals = redisClusterCache.hgetAll(hashTest);
        Assert.assertTrue(hashVals.size() == 3
                && hashVals.get("f1").equals("v1")
                && hashVals.get("f2").equals("v2")
                && hashVals.get("f3").equals("v3"));
        redisClusterCache.hdel(hashTest, "f3");
        Assert.assertTrue(!redisClusterCache.hexists(hashTest, "f3"));
        redisClusterCache.del(hashTest);
        Assert.assertTrue(!redisClusterCache.exists(hashTest));

        redisClusterCache.del(listTest);
        redisClusterCache.lmset(listTest, new ArrayList<String>() {{
            add("v1");
            add("v2");
        }});
        redisClusterCache.lpush(listTest, "v0");
        Assert.assertEquals(3, redisClusterCache.llen(listTest));
        Assert.assertEquals("v0", redisClusterCache.lpop(listTest));
        Assert.assertEquals(2, redisClusterCache.llen(listTest));
        List<String> listVals = redisClusterCache.lget(listTest);
        Assert.assertTrue(listVals.size() == 2 && listVals.stream().findAny().get().equals("v2"));

        redisClusterCache.del(intTest);
        Assert.assertEquals(0, redisClusterCache.incrBy(intTest, 0));
        redisClusterCache.set(intTest, "10");
        Assert.assertEquals("10", redisClusterCache.get(intTest));
        redisClusterCache.incrBy(intTest, 10);
        Assert.assertEquals("20", redisClusterCache.get(intTest));
        redisClusterCache.incrBy(intTest, 0);
        Assert.assertEquals("20", redisClusterCache.get(intTest));
        redisClusterCache.incrBy(intTest, 10);
        Assert.assertEquals("30", redisClusterCache.get(intTest));
        redisClusterCache.decrBy(intTest, 4);
        redisClusterCache.decrBy(intTest, 2);
        Assert.assertEquals("24", redisClusterCache.get(intTest));
        redisClusterCache.expire(intTest, 1);
        Assert.assertEquals("24", redisClusterCache.get(intTest));
        Thread.sleep(1000);
        Assert.assertEquals(null, redisClusterCache.get(intTest));
        redisClusterCache.del(setTest);
        redisClusterCache.smset(setTest, new ArrayList<String>() {{
            add("v1");
            add("v2");
        }});
        redisClusterCache.sset(setTest, "v3");
        Assert.assertEquals(3, redisClusterCache.slen(setTest));
        String popValue = redisClusterCache.spop(setTest);
        Set<String> values = redisClusterCache.sget(setTest);
        Assert.assertEquals(2, values.size());
        Assert.assertTrue(!values.contains(popValue));
        List<String> list = new ArrayList<String>() {{
            addAll(values);
        }};
        Assert.assertEquals(2, redisClusterCache.sdel(setTest, list.get(0), list.get(1)));
        Assert.assertTrue(!redisClusterCache.exists(setTest));

        //test bit
        boolean b = redisClusterCache.getBit("ss", 100);
        Assert.assertTrue(!b);
        b = redisClusterCache.setBit("ss", 100, true);
        Assert.assertTrue(!b);
        b = redisClusterCache.setBit("ss", 100, false);
        Assert.assertTrue(b);
    }
}
