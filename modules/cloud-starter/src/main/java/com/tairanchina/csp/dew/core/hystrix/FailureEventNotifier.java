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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.*;

@Component
public class FailureEventNotifier extends HystrixEventNotifier {

    Logger logger = LoggerFactory.getLogger(FailureEventNotifier.class);

    @PostConstruct
    public void init() {
        this.notifiedTime = Instant.now().toEpochMilli() - dewCloudConfig.getError().getNotifyIntervalMillSec();
        this.judgeType = dewCloudConfig.getError().getJudgeType();
        if (this.judgeType != null) {
            if (this.judgeType.equals("include") && dewCloudConfig.getError().getNotifyIncludeKeys() != null) {
                this.notifyIncludeKeys.addAll(Arrays.asList(dewCloudConfig.getError().getNotifyIncludeKeys()));
            }
            if (this.judgeType.equals("exclude") && dewCloudConfig.getError().getNotifyExcludeKeys() != null) {
                this.notifyExcludeKeys.addAll(Arrays.asList(dewCloudConfig.getError().getNotifyExcludeKeys()));
            }
        }
    }

    private String judgeType;

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

    @Override
    public void markEvent(HystrixEventType eventType, HystrixCommandKey key) {
        if (judgeType != null) {
            if (judgeType.equals("include") && (notifyIncludeKeys.isEmpty() || !notifyIncludeKeys.contains(key.name()))) {
                return;
            }
            if (judgeType.equals("exclude") && (!notifyExcludeKeys.isEmpty() && notifyExcludeKeys.contains(key.name()))) {
                return;
            }
        }
        if (eventType == HystrixEventType.SUCCESS) {
            if (failureInfo.containsKey(key.name())) {
                failureInfo.remove(key.name());
            }
        }
        if (!dewCloudConfig.getError().getNotifyEventTypes().contains(eventType.name())) {
            return;
        } else {
            failureInfo.putIfAbsent(key.name(), new HashSet<>());
            failureInfo.get(key.name()).add(eventType.name());
        }
        if (Instant.now().toEpochMilli() - notifiedTime < dewCloudConfig.getError().getNotifyIntervalMillSec()) {
            return;
        }
        notifiedTime = Instant.now().toEpochMilli();
        if (!failureInfo.isEmpty()) {
            new Thread(() -> sendEmail()).start();
        }
    }

    public void sendEmail() {
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
                value.forEach(type -> stringBuilder.append(type + ",\t"));
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
