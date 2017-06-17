package com.tairanchina.csp.dew.example.cluster;


import com.ecfront.dew.common.$;
import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.cluster.ClusterDistLock;
import com.tairanchina.csp.dew.core.cluster.ClusterDistMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;

@Component
public class ClusterExampleInitiator {

    private static final Logger logger = LoggerFactory.getLogger(ClusterExampleInitiator.class);

    @PostConstruct
    public void init() throws InterruptedException {
        // cache
        Dew.cluster.cache.flushdb();
        Dew.cluster.cache.del("n_test");
        assert !Dew.cluster.cache.exists("n_test");
        Dew.cluster.cache.set("n_test", "{\"name\":\"jzy\"}", 1);
        assert Dew.cluster.cache.exists("n_test");
        assert "jzy".equals($.json.toJson(Dew.cluster.cache.get("n_test")).get("name").asText());
        Thread.sleep(1000);
        assert !Dew.cluster.cache.exists("n_test");
        assert null == Dew.cluster.cache.get("n_test");
        // ...

        // dist map
        ClusterDistMap<TestMapObj> mapObj = Dew.cluster.dist.map("test_obj_map", TestMapObj.class);
        mapObj.clear();
        TestMapObj obj = new TestMapObj();
        obj.a = "测试";
        mapObj.put("test", obj);
        assert "测试".equals(mapObj.get("test").a);
        // ...

        // dist lock
        ClusterDistLock lock = Dew.cluster.dist.lock("test_lock");
        lock.delete();
        lock.lock();
        assert !lock.tryLock();
        // ...

        // pub-sub
        Dew.cluster.mq.subscribe("test_pub_sub", message ->
                logger.info("pub_sub>>" + message));
        Thread.sleep(1000);
        Dew.cluster.mq.publish("test_pub_sub", "msgA");
        Dew.cluster.mq.publish("test_pub_sub", "msgB");
        // req-resp
        Dew.cluster.mq.response("test_rep_resp", message ->
                logger.info("req_resp>>" + message));
        Dew.cluster.mq.request("test_rep_resp", "msg1");
        Dew.cluster.mq.request("test_rep_resp", "msg2");
    }

    static class TestMapObj implements Serializable {
        private String a;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }

}
