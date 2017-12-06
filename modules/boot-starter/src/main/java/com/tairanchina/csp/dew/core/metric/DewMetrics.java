package com.tairanchina.csp.dew.core.metric;

import com.tairanchina.csp.dew.core.DewConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(DewMetrics.class);

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
        //monitor
        try {
            MonitorInfo monitorInfo = MonitorUtil.getMonitorInfoBean();
            Field[] fields = MonitorInfo.class.getDeclaredFields();
            Field.setAccessible(fields, true);
            for (Field field : fields) {
                if (field.get(monitorInfo) == null) {
                    continue;
                }
                if (field.getGenericType().toString().equals("double")) {
                    metricList.add(new Metric<>("dew.monitor." + field.getName(), field.getDouble(monitorInfo)));
                }
                if (field.getGenericType().toString().equals("long")) {
                    metricList.add(new Metric<>("dew.monitor." + field.getName(), field.getLong(monitorInfo)));
                }
                if (field.getGenericType().toString().equals("int")) {
                    metricList.add(new Metric<>("dew.monitor." + field.getName(), field.getInt(monitorInfo)));
                }
                if (field.getGenericType().toString().equals("java.util.Map<java.lang.String, java.lang.Double>")) {
                    Map<String, Double> threadTimes = (Map<String, Double>) field.get(monitorInfo);
                    threadTimes.forEach((key, value) -> metricList.add(new Metric<>("dew.monitor.threadInfo." + key, value)));
                }
            }
        } catch (Exception e) {
            logger.error("Error:->get motitorInfo bean failed", e);
        }
        return metricList;
    }

}


