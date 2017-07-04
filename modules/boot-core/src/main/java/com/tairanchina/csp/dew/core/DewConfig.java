package com.tairanchina.csp.dew.core;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "dew")
public class DewConfig {

    private Basic basic = new Basic();
    private Cluster cluster = new Cluster();
    private Security security = new Security();

    public static class Basic {

        private String name = "";
        private String version = "1.0";
        private String desc = "";
        private String webSite = "";

        private Doc doc = new Doc();
        private Entity entity = new Entity();
        private Format format = new Format();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getWebSite() {
            return webSite;
        }

        public void setWebSite(String webSite) {
            this.webSite = webSite;
        }

        public static class Doc {

            private String basePackage = "";

            public String getBasePackage() {
                return basePackage;
            }

            public void setBasePackage(String basePackage) {
                this.basePackage = basePackage;
            }
        }

        public static class Entity {

            private List<String> basePackages = new ArrayList<>();

            public List<String> getBasePackages() {
                return basePackages;
            }

            public void setBasePackages(List<String> basePackages) {
                this.basePackages = basePackages;
            }
        }

        public static class Format {

            private boolean useUnityError = false;
            private boolean reuseHttpState = false;
            // 兼容原系统设置
            private String messageFieldName = "message";

            public boolean isUseUnityError() {
                return useUnityError;
            }

            public void setUseUnityError(boolean useUnityError) {
                this.useUnityError = useUnityError;
            }

            public boolean isReuseHttpState() {
                return reuseHttpState;
            }

            public void setReuseHttpState(boolean reuseHttpState) {
                this.reuseHttpState = reuseHttpState;
            }

            public String getMessageFieldName() {
                return messageFieldName;
            }

            public void setMessageFieldName(String messageFieldName) {
                this.messageFieldName = messageFieldName;
            }
        }

        public Doc getDoc() {
            return doc;
        }

        public void setDoc(Doc doc) {
            this.doc = doc;
        }

        public Entity getEntity() {
            return entity;
        }

        public void setEntity(Entity entity) {
            this.entity = entity;
        }

        public Format getFormat() {
            return format;
        }

        public void setFormat(Format format) {
            this.format = format;
        }


    }

    public static class Cluster {

        private String mq = "redis";
        private String cache = "redis";
        private String dist = "redis";

        public String getMq() {
            return mq;
        }

        public void setMq(String mq) {
            this.mq = mq;
        }

        public String getCache() {
            return cache;
        }

        public void setCache(String cache) {
            this.cache = cache;
        }

        public String getDist() {
            return dist;
        }

        public void setDist(String dist) {
            this.dist = dist;
        }
    }

    public static class Security {

        private SecurityCORS cors = new SecurityCORS();

        private String tokenFlag = "__dew_token__";

        private boolean tokenInHeader = false;

        private boolean tokenHash = false;

        public SecurityCORS getCors() {
            return cors;
        }

        public void setCors(SecurityCORS cors) {
            this.cors = cors;
        }

        public String getTokenFlag() {
            return tokenFlag;
        }

        public void setTokenFlag(String tokenFlag) {
            this.tokenFlag = tokenFlag;
        }

        public boolean isTokenInHeader() {
            return tokenInHeader;
        }

        public void setTokenInHeader(boolean tokenInHeader) {
            this.tokenInHeader = tokenInHeader;
        }

        public boolean isTokenHash() {
            return tokenHash;
        }

        public void setTokenHash(boolean tokenHash) {
            this.tokenHash = tokenHash;
        }
    }

    public static class SecurityCORS {

        private String allowOrigin = "*";
        private String allowMethods = "POST,GET,OPTIONS,PUT,DELETE,HEAD";
        private String allowHeaders = "x-requested-with,content-type";

        public String getAllowOrigin() {
            return allowOrigin;
        }

        public void setAllowOrigin(String allowOrigin) {
            this.allowOrigin = allowOrigin;
        }

        public String getAllowMethods() {
            return allowMethods;
        }

        public void setAllowMethods(String allowMethods) {
            this.allowMethods = allowMethods;
        }

        public String getAllowHeaders() {
            return allowHeaders;
        }

        public void setAllowHeaders(String allowHeaders) {
            this.allowHeaders = allowHeaders;
        }
    }

    public Basic getBasic() {
        return basic;
    }


    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }
}
