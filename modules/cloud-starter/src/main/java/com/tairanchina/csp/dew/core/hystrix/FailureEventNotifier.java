package com.tairanchina.csp.dew.core.hystrix;

import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixEventType;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.core.DewCloudConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FailureEventNotifier extends HystrixEventNotifier {

    @Autowired
    private DewCloudConfig dewCloudConfig;
    @Autowired
    private JavaMailSender mailSender;
    @Value("spring.mail.from")
    private String emailFrom;

    private long notifiedTime = Instant.now().toEpochMilli() - dewCloudConfig.getError().getNotifyIntervalSec();
    // key.name -> eventType.names
    private Map<String, Set<String>> failureInfo = new WeakHashMap<>();

    @Override
    public void markEvent(HystrixEventType eventType, HystrixCommandKey key) {
        if (dewCloudConfig.getError().getNotifyExcludeKeys().contains(key.name())) {
            return;
        }
        if (!dewCloudConfig.getError().getNotifyIncludeKeys().isEmpty()
                && !dewCloudConfig.getError().getNotifyIncludeKeys().contains(key.name())) {
            return;
        }
        if (!dewCloudConfig.getError().getNotifyEventTypes().contains(eventType.name())) {
            return;
        }
        if (eventType == HystrixEventType.SUCCESS) {
            if (failureInfo.containsKey(key.name())) {
                failureInfo.remove(key.name());
            }
        } else {
            failureInfo.putIfAbsent(key.name(), new HashSet<>());
            failureInfo.get(key.name()).add(eventType.name());
        }
        if (Instant.now().toEpochMilli() - notifiedTime < dewCloudConfig.getError().getNotifyIntervalSec()) {
            return;
        }
        notifiedTime = Instant.now().toEpochMilli();
        if (!failureInfo.isEmpty()) {
            sendEmail();
        }
        super.markEvent(eventType, key);
    }

    @Async
    public void sendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(dewCloudConfig.getError().getNotifyEmails().toArray(new String[]{}));
        message.setSubject(Dew.dewConfig.getBasic().getName() + dewCloudConfig.getError().getNotifyTitle());
        message.setText("以下服务发生异常:\r\n"
                + failureInfo.entrySet().stream()
                .map(entry -> "异常方法:" + entry.getKey() + "\t类型:" + entry.getValue().stream().collect(Collectors.joining(","))) + "\r\n");
        mailSender.send(message);
    }
}
