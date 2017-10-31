package com.tairanchina.csp.dew.core.hystrix;

import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixEventType;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.core.DewCloudConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
@ConditionalOnBean(JavaMailSender.class)
public class FailureEventNotifier extends HystrixEventNotifier {

    private static final Logger logger = LoggerFactory.getLogger(FailureEventNotifier.class);

    @Autowired
    private DewCloudConfig dewCloudConfig;

    private Set<String> notifyIncludeKeys = new HashSet<>();
    private Set<String> notifyExcludeKeys = new HashSet<>();
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String emailFrom;

    private long notifiedTime;
    // key.name -> eventType.names
    private Map<String, Set<String>> failureInfo = new WeakHashMap<>();

    private Executor executor = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void init() {
        this.notifiedTime = Instant.now().toEpochMilli() - dewCloudConfig.getError().getNotifyIntervalSec() * 1000;
        this.notifyIncludeKeys.addAll(Arrays.asList(dewCloudConfig.getError().getNotifyIncludeKeys()));
        this.notifyExcludeKeys.addAll(Arrays.asList(dewCloudConfig.getError().getNotifyExcludeKeys()));
    }

    @Override
    public void markEvent(HystrixEventType eventType, HystrixCommandKey key) {
        if (eventType == HystrixEventType.SUCCESS) {
            if (failureInfo.containsKey(key.name())) {
                failureInfo.remove(key.name());
            }
            return;
        }
        if (!notifyIncludeKeys.isEmpty() && !notifyIncludeKeys.contains(key.name())) {
            return;
        }
        if (!notifyExcludeKeys.isEmpty() && notifyExcludeKeys.contains(key.name())) {
            return;
        }
        if (!dewCloudConfig.getError().getNotifyEventTypes().contains(eventType.name())) {
            return;
        }
        failureInfo.putIfAbsent(key.name(), new HashSet<>());
        failureInfo.get(key.name()).add(eventType.name());

        if (Instant.now().toEpochMilli() - notifiedTime < dewCloudConfig.getError().getNotifyIntervalSec() * 1000) {
            return;
        }
        notifiedTime = Instant.now().toEpochMilli();
        if (!failureInfo.isEmpty()) {
            executor.execute(this::sendEmail);
        }
    }

    private void sendEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(dewCloudConfig.getError().getNotifyEmails().toArray(new String[]{}));
            message.setSubject(Dew.dewConfig.getBasic().getName() + dewCloudConfig.getError().getNotifyTitle());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("以下服务发生异常:");
            failureInfo.forEach((key, value) -> {
                stringBuilder.append("\r\n" +
                        "异常方法:").append(key).append("\t类型:\t");
                value.forEach(type -> stringBuilder.append(type).append(",\t"));
            });
            stringBuilder.append("\r\n");
            message.setText(stringBuilder.toString());
            mailSender.send(message);
            logger.info("邮件通知成功\t\t\tdetail:\t" + stringBuilder.toString());
        } catch (Exception e) {
            logger.error("邮件通知失败\t\t\tdetail：" + e.getMessage());
        }
    }
}
