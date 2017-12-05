package com.tairanchina.csp.dew.core.metric;

import com.tairanchina.csp.dew.core.DewConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Component
@ConditionalOnBean(DewFilter.class)
public class DewMetrics implements PublicMetrics {

    @Autowired
    private DewConfig dewConfig;

    @Override
    public Collection<Metric<?>> metrics() {
        long standardTime = Instant.now().minusSeconds(dewConfig.getMetric().getPeriodSec()).toEpochMilli();
        List<Metric<?>> metricList = new ArrayList<>();
        List<Integer> totalList = new ArrayList<>();
        // url级平均响应时间->url级个数
        Map<Double, Integer> averageMap = new HashMap<>();
        DewFilter.RECORD_MAP.forEach((key, value) -> {
            long urlSum = 0;
            List<Integer> validList = new ArrayList<>();
            for (Map.Entry<Long, Integer> entry : value.entrySet()) {
                if (entry.getKey() > standardTime) {
                    urlSum += entry.getValue();
                    validList.add(entry.getValue());
                }
            }
            Object[] urlTimeArr = validList.toArray();
            Arrays.sort(urlTimeArr);
            double average = (double) urlSum / validList.size();
            metricList.add(new Metric<>("dew.response.average." + key, BigDecimal.valueOf(average).setScale(2, BigDecimal.ROUND_HALF_UP)));
            metricList.add(new Metric<>("dew.response.90percent." + key, (int) urlTimeArr[(int) (validList.size() * 0.9)]));
            metricList.add(new Metric<>("dew.response.max." + key, (int) urlTimeArr[(validList.size() - 1)]));
            metricList.add(new Metric<>("dew.response.tps." + key, BigDecimal.valueOf(validList.size() * 1.0 / dewConfig.getMetric().getPeriodSec()).setScale(2, BigDecimal.ROUND_HALF_UP)));
            totalList.addAll(validList);
            averageMap.put(average, validList.size());
        });
        double totalAverage = 0;
        for (Map.Entry<Double, Integer> entry : averageMap.entrySet()) {
            totalAverage += entry.getKey() * ((double) entry.getValue() / totalList.size());
        }
        Object[] totalArr = totalList.toArray();
        Arrays.sort(totalArr);
        metricList.add(new Metric<>("dew.response.average", BigDecimal.valueOf(totalAverage).setScale(2, BigDecimal.ROUND_HALF_UP)));
        metricList.add(new Metric<>("dew.response.90percent", (int) totalArr[(int) (totalList.size() * 0.9)]));
        metricList.add(new Metric<>("dew.response.max", (int) totalArr[totalList.size() - 1]));
        metricList.add(new Metric<>("dew.response.tps", BigDecimal.valueOf(totalList.size() * 1.0 / dewConfig.getMetric().getPeriodSec()).setScale(2, BigDecimal.ROUND_HALF_UP)));
        try {
            MonitorInfo monitorInfo = MonitorService.getMonitorInfoBean();
            Field[] fileds = MonitorInfo.class.getDeclaredFields();
            Field.setAccessible(fileds, true);
            for (Field field : fileds) {
                if (field.getGenericType().toString().equals("long")) {
                    metricList.add(new Metric<>("dew.monitor." + field.getName(), field.getLong(monitorInfo)));
                }
                if (field.getGenericType().toString().equals("int")) {
                    metricList.add(new Metric<>("dew.monitor." + field.getName(), field.getInt(monitorInfo)));
                }
            }
        } catch (Exception e) {
            System.err.println("获取bean失败");
        }
        return metricList;
    }

}


