package com.tairanchina.csp.dew.core;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@ConfigurationProperties(prefix = "dew.cloud")
public class DewCloudConfig {

    private Error error = new Error();

    public static class Error {

        private Set<String> notifyEmails = new HashSet<>();
        private String notifyTitle = "服务异常";
        private long notifyIntervalMillSec = 5 * 60 * 1000;
        private String judgeType ;
        private String[] notifyIncludeKeys;
        private String[] notifyExcludeKeys;
        private Set<String> notifyEventTypes = new HashSet<>();

        public String getJudgeType() {
            return judgeType;
        }

        public void setJudgeType(String judgeType) {
            this.judgeType = judgeType;
        }

        public Set<String> getNotifyEmails() {
            return notifyEmails;
        }

        public void setNotifyEmails(Set<String> notifyEmails) {
            this.notifyEmails = notifyEmails;
        }

        public String getNotifyTitle() {
            return notifyTitle;
        }

        public void setNotifyTitle(String notifyTitle) {
            this.notifyTitle = notifyTitle;
        }

        public Set<String> getNotifyEventTypes() {
            return notifyEventTypes;
        }

        public void setNotifyEventTypes(Set<String> notifyEventTypes) {
            this.notifyEventTypes = notifyEventTypes;
        }

        public long getNotifyIntervalMillSec() {
            return notifyIntervalMillSec;
        }

        public void setNotifyIntervalMillSec(long notifyIntervalMillSec) {
            this.notifyIntervalMillSec = notifyIntervalMillSec;
        }

        public String[] getNotifyIncludeKeys() {
            return notifyIncludeKeys;
        }

        public void setNotifyIncludeKeys(String[] notifyIncludeKeys) {
            this.notifyIncludeKeys = notifyIncludeKeys;
        }

        public String[] getNotifyExcludeKeys() {
            return notifyExcludeKeys;
        }

        public void setNotifyExcludeKeys(String[] notifyExcludeKeys) {
            this.notifyExcludeKeys = notifyExcludeKeys;
        }
    }


    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
