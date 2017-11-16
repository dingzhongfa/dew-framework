package com.tairanchina.csp.dew.core.metric;

import com.tairanchina.csp.dew.core.filter.DewFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
        // url级平均响应时间->url级个数
        Map<Double, Integer> averageMap = new HashMap<>();
        BigDecimal intervalSec = new BigDecimal(INTERVAL_SEC);
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
            double average = (double)urlSum / validList.size();
            metricList.add(new Metric<>("dew.response.average." + key, BigDecimal.valueOf(average).setScale(2,BigDecimal.ROUND_HALF_UP)));
            metricList.add(new Metric<>("dew.response.90percent." + key, nityPec));
            metricList.add(new Metric<>("dew.response.max." + key, max));
            BigDecimal times = new BigDecimal(validList.size());
            metricList.add(new Metric<>("dew.response.tps." + key, times.divide(intervalSec,2,BigDecimal.ROUND_HALF_UP)));
            averageMap.put(average, validList.size());
        });
        double totalAverage = 0;
        for (Map.Entry<Double,Integer> entry: averageMap.entrySet()){
            totalAverage+= entry.getKey()*((double)entry.getValue()/totalList.size());
        }
        Object[] totalArr = totalList.toArray();
        Arrays.sort(totalArr);
        metricList.add(new Metric<>("dew.response.average",BigDecimal.valueOf(totalAverage).setScale(2,BigDecimal.ROUND_HALF_UP)));
        metricList.add(new Metric<>("dew.response.90perent", (Integer) totalArr[(int) (totalList.size() * 0.9)]));
        metricList.add(new Metric<>("dew.response.max", (Integer) totalArr[totalList.size() - 1]));
        BigDecimal totalTimes = new BigDecimal(totalList.size());
        metricList.add(new Metric<>("dew.response.tps", totalTimes.divide(intervalSec,2,BigDecimal.ROUND_HALF_UP)));
        return metricList;
    }

}


