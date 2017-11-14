package com.tairanchina.csp.dew.core.initiator;

import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.core.filter.DewFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Iterator;
import java.util.Map;

/**
 * desription:
 * Created by ding on 2017/11/14.
 */
@Component
public class DewInitiator {

    @Value("${metric.timeout:600}")
    private long DIVIDING_LINE;

    @PostConstruct
    public void init() {
        long divid = Instant.now().minusSeconds(DIVIDING_LINE).toEpochMilli();
        Dew.Timer.periodic(60, () -> {
            for (Map<Long, Integer> map : DewFilter.responseMap.values()) {
                Iterator<Map.Entry<Long, Integer>> iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Long, Integer> entry = iterator.next();
                    if (entry.getKey() < divid) {
                        iterator.remove();
                    } else {
                        break;
                    }
                }
            }
        });
    }
}
