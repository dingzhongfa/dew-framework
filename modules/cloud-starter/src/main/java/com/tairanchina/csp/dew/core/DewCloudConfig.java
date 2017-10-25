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
        private int notifyIntervalSec = 1800;
        private Set<String> notifyEventTypes = new HashSet<>();
        private Set<String> notifyIncludeKeys = new HashSet<>();
        private Set<String> notifyExcludeKeys = new HashSet<>();

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

        public int getNotifyIntervalSec() {
            return notifyIntervalSec;
        }

        public void setNotifyIntervalSec(int notifyIntervalSec) {
            this.notifyIntervalSec = notifyIntervalSec;
        }

        public Set<String> getNotifyIncludeKeys() {
            return notifyIncludeKeys;
        }

        public void setNotifyIncludeKeys(Set<String> notifyIncludeKeys) {
            this.notifyIncludeKeys = notifyIncludeKeys;
        }

        public Set<String> getNotifyExcludeKeys() {
            return notifyExcludeKeys;
        }

        public void setNotifyExcludeKeys(Set<String> notifyExcludeKeys) {
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
