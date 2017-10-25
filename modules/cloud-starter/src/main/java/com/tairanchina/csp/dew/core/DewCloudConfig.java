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
        private int eventHoldSec = 3600;
        private Set<String> notifyEventTypes = new HashSet<>();
        private Set<String> notifyIncludeKeys = new HashSet<>();
        private Set<String> notifyExcludeKeys = new HashSet<>();

        public Set<String> getNotifyEmails() {
            return notifyEmails;
        }

        public void setNotifyEmails(Set<String> notifyEmails) {
            this.notifyEmails = notifyEmails;
        }

        public int getEventHoldSec() {
            return eventHoldSec;
        }

        public void setEventHoldSec(int eventHoldSec) {
            this.eventHoldSec = eventHoldSec;
        }

        public Set<String> getNotifyEventTypes() {
            return notifyEventTypes;
        }

        public void setNotifyEventTypes(Set<String> notifyEventTypes) {
            this.notifyEventTypes = notifyEventTypes;
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
