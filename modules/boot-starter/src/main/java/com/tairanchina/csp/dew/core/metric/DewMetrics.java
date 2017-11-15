package com.tairanchina.csp.dew.core.metric;

import com.tairanchina.csp.dew.core.filter.DewFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

/**
 * desription:
 * Created by ding on 2017/11/13.
 */
@Component
public class DewMetrics implements PublicMetrics {

    @Value("${dew.metric.interval-sec:600}")
    private long INTERVAL_SEC;

    @Override
    public Collection<Metric<?>> metrics() {
        long divid = Instant.now().minusSeconds(INTERVAL_SEC).toEpochMilli();
        List<Metric<?>> metricList = new ArrayList<>();
        List<Integer> totalList = new ArrayList<>();
        List<Integer> averageList = new ArrayList<>();
        DewFilter.RECORD_MAP.forEach((key, value) -> {
            long urlSum = 0;
            List<Integer> validList = new ArrayList<>();
            for (Map.Entry<Long, Integer> entry : value.entrySet()) {
                if (entry.getKey() > divid) {
                    urlSum += entry.getValue();
                    validList.add(entry.getValue());
                }
            }
            totalList.addAll(validList);
            Object[] urlTimeArr = validList.toArray();
            Arrays.sort(urlTimeArr);
            int nityPec = (int) urlTimeArr[(int) (validList.size() * 0.9)];
            int max = (int) urlTimeArr[(validList.size() - 1)];
            int average = (int) (urlSum / validList.size());
            metricList.add(new Metric<>("dew.response.average." + key, average));
            metricList.add(new Metric<>("dew.response.90percent." + key, nityPec));
            metricList.add(new Metric<>("dew.response.max." + key, max));
            metricList.add(new Metric<>("dew.response.tps." + key, validList.size() / INTERVAL_SEC));
            averageList.add(average);
        });
        if (averageList.size() != 0) {
            int totalSum = 0;
            for (int ave : averageList) {
                totalSum += ave;
            }
            metricList.add(new Metric<>("dew.response.average", totalSum / averageList.size()));
        }
        if (totalList.size() != 0) {
            Object[] totalArr = totalList.toArray();
            Arrays.sort(totalArr);
            metricList.add(new Metric<>("dew.response.90perent", (Integer) totalArr[(int) (totalList.size() * 0.9)]));
            metricList.add(new Metric<>("dew.response.max", (Integer) totalArr[totalList.size() - 1]));
            metricList.add(new Metric<>("dew.response.tps", totalList.size() / INTERVAL_SEC));
        }
        return metricList;
    }

}


