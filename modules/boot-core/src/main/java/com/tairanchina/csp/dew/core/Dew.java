package com.tairanchina.csp.dew.core;

import com.ecfront.dew.common.$;
import com.ecfront.dew.common.HttpHelper;
import com.ecfront.dew.common.StandardCode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tairanchina.csp.dew.core.auth.AuthAdapter;
import com.tairanchina.csp.dew.core.auth.BasicAuthAdapter;
import com.tairanchina.csp.dew.core.auth.UCAuthAdapter;
import com.tairanchina.csp.dew.core.cluster.*;
import com.tairanchina.csp.dew.core.entity.EntityContainer;
import com.tairanchina.csp.dew.core.fun.VoidExecutor;
import com.tairanchina.csp.dew.core.fun.VoidPredicate;
import com.tairanchina.csp.dew.core.jdbc.ClassPathScanner;
import com.tairanchina.csp.dew.core.jdbc.DS;
import com.tairanchina.csp.dew.core.jdbc.DSManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class Dew {

    private static final Logger logger = LoggerFactory.getLogger(Dew.class);

    public static Cluster cluster = new Cluster();
    public static ApplicationContext applicationContext;
    public static DewConfig dewConfig;
    public static AuthAdapter auth;

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    @Qualifier("dewConfig")
    private DewConfig innerDewConfig;

    @Autowired(required = false)
    private JacksonProperties jacksonProperties;

    @Autowired
    private ApplicationContext innerApplicationContext;

    @PostConstruct
    private void init() {
        Dew.applicationContext = innerApplicationContext;
        if (Dew.applicationContext.containsBean(innerDewConfig.getCluster().getCache() + "ClusterCache")) {
            Dew.cluster.cache = (ClusterCache) Dew.applicationContext.getBean(innerDewConfig.getCluster().getCache() + "ClusterCache");
        }
        if (Dew.applicationContext.containsBean(innerDewConfig.getCluster().getDist() + "ClusterDist")) {
            Dew.cluster.dist = (ClusterDist) Dew.applicationContext.getBean(innerDewConfig.getCluster().getDist() + "ClusterDist");
        }
        if (Dew.applicationContext.containsBean(innerDewConfig.getCluster().getMq() + "ClusterMQ")) {
            Dew.cluster.mq = (ClusterMQ) Dew.applicationContext.getBean(innerDewConfig.getCluster().getMq() + "ClusterMQ");
        }
        if (Dew.applicationContext.containsBean(innerDewConfig.getCluster().getElection() + "ClusterElection")) {
            Dew.cluster.election = (ClusterElection) Dew.applicationContext.getBean(innerDewConfig.getCluster().getElection() + "ClusterElection");
        }
        Dew.dewConfig = innerDewConfig;
        if (Dew.applicationContext.containsBean(DSManager.class.getSimpleName())) {
            Dew.applicationContext.getBean(DSManager.class);
        }
        Dew.applicationContext.containsBean(EntityContainer.class.getSimpleName());
        Info.name = applicationName;
        // JDBC Scan
        if (!Dew.dewConfig.getJdbc().getBasePackages().isEmpty()) {
            ClassPathScanner scanner = new ClassPathScanner((BeanDefinitionRegistry) ((GenericApplicationContext) Dew.applicationContext).getBeanFactory());
            scanner.setResourceLoader(Dew.applicationContext);
            scanner.registerFilters();
            scanner.scan(Dew.dewConfig.getJdbc().getBasePackages().toArray(new String[]{}));
        }
        // Select Auth Adapter
        if(Dew.dewConfig.getSecurity().getAuthAdapter().equalsIgnoreCase("basic")){
            auth=Dew.applicationContext.getBean(BasicAuthAdapter.class);
        }else if(Dew.dewConfig.getSecurity().getAuthAdapter().equalsIgnoreCase("uc")){
            auth=Dew.applicationContext.getBean(UCAuthAdapter.class);
        }
        // Support java8 Time
        if (jacksonProperties != null) {
            jacksonProperties.getSerialization().put(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        }
    }

    public static class Constant {
        public static final String HTTP_REQUEST_FROM_FLAG = "Request-From";
    }

    /**
     * 组件基础信息
     */
    public static class Info {
        // 应用名称
        public static String name;
        // 应用主机IP
        public static String ip;
        // 应用主机名
        public static String host;
        // 应用实例，各组件唯一
        public static String instance;

        static {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
                host = InetAddress.getLocalHost().getHostName();
                instance = $.field.createUUID();
            } catch (UnknownHostException e) {
                logger.error("Dew info fetch error.", e);
            }
        }

    }

    public static DS ds() {
        return DSManager.select("");
    }

    public static DS ds(String dsName) {
        return DSManager.select(dsName);
    }

    /**
     * 获取请求上下文信息
     *
     * @return 请求上下文信息
     */
    public static DewContext context() {
        return DewContext.getContext();
    }

    /**
     * 请求消息（基于RestTemplate）辅助工具
     */
    public static class EB {

        private static RestTemplate serviceClient;

        public static void setServiceClient(RestTemplate inputServiceClient) {
            serviceClient = inputServiceClient;
        }

        public static HttpHelper.ResponseWrap get(String url) throws Exception {
            return get(url, null);
        }

        public static HttpHelper.ResponseWrap get(String url, Map<String, String> header) throws Exception {
            return exchange(HttpMethod.GET, url, null, header);
        }

        public static HttpHelper.ResponseWrap delete(String url) throws Exception {
            return delete(url, null);
        }

        public static HttpHelper.ResponseWrap delete(String url, Map<String, String> header) throws Exception {
            return exchange(HttpMethod.DELETE, url, null, header);
        }

        public static HttpHelper.ResponseWrap head(String url) throws Exception {
            return head(url, null);
        }

        public static HttpHelper.ResponseWrap head(String url, Map<String, String> header) throws Exception {
            return exchange(HttpMethod.HEAD, url, null, header);
        }

        public static HttpHelper.ResponseWrap options(String url) throws Exception {
            return options(url, null);
        }

        public static HttpHelper.ResponseWrap options(String url, Map<String, String> header) throws Exception {
            return exchange(HttpMethod.OPTIONS, url, null, header);
        }

        public static HttpHelper.ResponseWrap post(String url, Object body) throws Exception {
            return post(url, body, null);
        }

        public static HttpHelper.ResponseWrap post(String url, Object body, Map<String, String> header) throws Exception {
            return exchange(HttpMethod.POST, url, body, header);
        }

        public static HttpHelper.ResponseWrap put(String url, Object body) throws Exception {
            return put(url, body, null);
        }

        public static HttpHelper.ResponseWrap put(String url, Object body, Map<String, String> header) throws Exception {
            return exchange(HttpMethod.PUT, url, body, header);
        }

        public static HttpHelper.ResponseWrap exchange(HttpMethod httpMethod, String url, Object body, Map<String, String> header) throws Exception {
            try {
                if (header == null) {
                    header = new HashMap<>();
                    header.put(Constant.HTTP_REQUEST_FROM_FLAG, Info.name.toUpperCase());
                }
                if (!$.field.isIPv4Address(new URL(url).getHost())) {
                    HttpHeaders headers = new HttpHeaders();
                    header.forEach(headers::add);
                    tryAttachTokenToHeader(headers);
                    HttpEntity entity;
                    if (body != null) {
                        entity = new HttpEntity(body, headers);
                    } else {
                        entity = new HttpEntity(headers);
                    }
                    ResponseEntity resp = serviceClient.exchange(tryAttachTokenToUrl(url), httpMethod, entity, Object.class);
                    HttpHelper.ResponseWrap responseWrap = new HttpHelper.ResponseWrap();
                    responseWrap.head = new HashMap<>();
                    for (Map.Entry<String, List<String>> entry : resp.getHeaders().entrySet()) {
                        responseWrap.head.put(entry.getKey(), !entry.getValue().isEmpty() ? entry.getValue().get(0) : "");
                    }
                    responseWrap.result = $.json.toJsonString(resp.getBody());
                    return responseWrap;
                } else {
                    return $.http.request(httpMethod.name(), tryAttachTokenToUrl(url), body, header, null, null, 0);
                }
            } catch (Exception e) {
                logger.error("EB Process error.", e);
                throw e;
            }
        }

        private static String tryAttachTokenToUrl(String url) {
            if (!Dew.dewConfig.getSecurity().isTokenInHeader()) {
                String token = Dew.context().getToken();
                if (url.contains("&")) {
                    return url + "&" + Dew.dewConfig.getSecurity().getTokenFlag() + "=" + token;
                } else {
                    return url + "?" + Dew.dewConfig.getSecurity().getTokenFlag() + "=" + token;
                }
            }
            return url;
        }

        private static void tryAttachTokenToHeader(HttpHeaders headers) {
            if (Dew.dewConfig.getSecurity().isTokenInHeader()) {
                String token = Dew.context().getToken();
                headers.add(Dew.dewConfig.getSecurity().getTokenFlag(), token);
            }
        }

    }

    /**
     * 定时器支持（带请求上下文绑定）
     */
    public static class Timer {

        private static final Logger logger = LoggerFactory.getLogger(Timer.class);

        public static void periodic(long initialDelaySec, long periodSec, VoidExecutor fun) {
            DewContext context = Dew.context();
            $.timer.periodic(initialDelaySec, periodSec, true, () -> {
                DewContext.setContext(context);
                try {
                    fun.exec();
                } catch (Exception e) {
                    logger.error("[Timer] Execute error", e);
                }
            });
        }

        public static void periodic(long periodSec, VoidExecutor fun) {
            periodic(0, periodSec, fun);
        }

        public static void timer(long delaySec, VoidExecutor fun) {
            DewContext context = Dew.context();
            $.timer.timer(delaySec, () -> {
                DewContext.setContext(context);
                try {
                    fun.exec();
                } catch (Exception e) {
                    logger.error("[Timer] Execute error", e);
                }
            });
        }
    }

    /**
     * 常用工具
     */
    public static class Util {

        private static ExecutorService executorService = Executors.newCachedThreadPool();

        public static String getRealIP(HttpServletRequest request) {
            Map<String, String> requestHeader = new HashMap<>();
            Enumeration<String> header = request.getHeaderNames();
            while (header.hasMoreElements()) {
                String key = header.nextElement();
                requestHeader.put(key.toLowerCase(), request.getHeader(key));
            }
            return getRealIP(requestHeader, request.getRemoteAddr());
        }

        public static String getRealIP(Map<String, String> requestHeader, String remoteAddr) {
            if (requestHeader.containsKey("x-forwarded-for") && requestHeader.get("x-forwarded-for") != null && !requestHeader.get("x-forwarded-for").isEmpty()) {
                return requestHeader.get("x-forwarded-for");
            }
            if (requestHeader.containsKey("wl-proxy-client-ip") && requestHeader.get("wl-proxy-client-ip") != null && !requestHeader.get("wl-proxy-client-ip").isEmpty()) {
                return requestHeader.get("wl-proxy-client-ip");
            }
            if (requestHeader.containsKey("x-forwarded-host") && requestHeader.get("x-forwarded-host") != null && !requestHeader.get("x-forwarded-host").isEmpty()) {
                return requestHeader.get("x-forwarded-host");
            }
            return remoteAddr;
        }

        public static void newThread(Runnable fun) {
            executorService.execute(fun);
        }

        public static class RunnableWithContext implements Runnable {

            private VoidExecutor fun;
            private DewContext context;

            public RunnableWithContext(VoidExecutor fun) {
                this.fun = fun;
                this.context = DewContext.getContext();
            }

            @Override
            public void run() {
                DewContext.setContext(context);
                fun.exec();
            }
        }

    }

    public static class E {

        /**
         * 异常处理-重用Http状态
         *
         * @param code 异常编码
         * @param ex   异常类型
         */
        public static <E extends Throwable> E e(String code, E ex) {
            return e(code, ex, -1);
        }

        /**
         * 异常处理-重用Http状态
         *
         * @param code           异常编码
         * @param ex             异常类型
         * @param customHttpCode 自定义Http状态码
         */
        public static <E extends Throwable> E e(String code, E ex, StandardCode customHttpCode) {
            return e(code, ex, Integer.valueOf(customHttpCode.toString()));
        }

        /**
         * 异常处理-重用Http状态
         *
         * @param code           异常编码
         * @param ex             异常类型
         * @param customHttpCode 自定义Http状态码
         */
        public static <E extends Throwable> E e(String code, E ex, int customHttpCode) {
            try {
                $.bean.setValue(ex, "detailMessage", $.json.createObjectNode()
                        .put("code", code)
                        .put("message", ex.getLocalizedMessage())
                        .put("customHttpCode", customHttpCode)
                        .toString());
            } catch (NoSuchFieldException e1) {
                logger.error("Throw Exception Convert error", e1);
            }
            return ex;
        }

        public static <E extends RuntimeException> void checkNotNull(Object obj, E ex) {
            check(() -> obj == null, ex);
        }

        public static <E extends RuntimeException> void checkNotEmpty(Iterable<?> objects, E ex) {
            check(() -> !objects.iterator().hasNext(), ex);
        }

        public static <E extends RuntimeException> void checkNotEmpty(Map<?, ?> objects, E ex) {
            check(() -> objects.size() == 0, ex);
        }

        /**
         * 抛出不符合预期异常
         *
         * @param notExpected 不符合预期的情况
         * @param ex          异常
         */
        public static <E extends RuntimeException> void check(boolean notExpected, E ex) {
            check(() -> notExpected, ex);
        }

        /**
         * 抛出不符合预期异常
         *
         * @param notExpected 不符合预期的情况
         * @param ex          异常
         */
        public static <E extends RuntimeException> void check(VoidPredicate notExpected, E ex) {
            if (notExpected.test()) {
                throw ex;
            }
        }

    }

}
