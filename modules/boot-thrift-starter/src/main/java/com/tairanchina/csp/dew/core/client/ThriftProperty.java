package com.tairanchina.csp.dew.core.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 迹_Jason on 2017/8/9.
 */
@Configuration
@ConfigurationProperties(prefix = "thrift")
public class ThriftProperty {

    private Service service = new Service();

    private Client client = new Client();

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public static class Client {
        private Integer maxThread = 10;

        public Integer getMaxThread() {
            return maxThread;
        }

        public void setMaxThread(Integer maxThread) {
            this.maxThread = maxThread;
        }
    }

    public static class Service {
        private String endpoint = null;
        private Integer connectTimeout = 1000;
        private Integer readTimeout = 30000;
        private Integer maxRetries = 1;
        private String path = "";

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public Integer getConnectTimeout() {
            return connectTimeout;
        }

        public void setConnectTimeout(Integer connectTimeout) {
            this.connectTimeout = connectTimeout;
        }

        public Integer getReadTimeout() {
            return readTimeout;
        }

        public void setReadTimeout(Integer readTimeout) {
            this.readTimeout = readTimeout;
        }

        public Integer getMaxRetries() {
            return maxRetries;
        }

        public void setMaxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

}
