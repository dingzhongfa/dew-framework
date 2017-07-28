package com.tairanchina.csp.dew.core;

import com.ecfront.dew.common.$;
import com.ecfront.dew.common.HttpHelper;
import com.ecfront.dew.common.StandardCode;
import com.tairanchina.csp.dew.core.cluster.Cluster;
import com.tairanchina.csp.dew.core.cluster.ClusterCache;
import com.tairanchina.csp.dew.core.cluster.ClusterDist;
import com.tairanchina.csp.dew.core.cluster.ClusterMQ;
import com.tairanchina.csp.dew.core.dto.OptInfo;
import com.tairanchina.csp.dew.core.entity.EntityContainer;
import com.tairanchina.csp.dew.core.fun.VoidExecutor;
import com.tairanchina.csp.dew.core.jdbc.ClassPathScanner;
import com.tairanchina.csp.dew.core.jdbc.DS;
import com.tairanchina.csp.dew.core.jdbc.DSManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class Dew {

    private static final Logger logger = LoggerFactory.getLogger(Dew.class);

    @Autowired
    @Qualifier("dewConfig")
    private DewConfig innerDewConfig;
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
        Dew.dewConfig = innerDewConfig;
        if (Dew.applicationContext.containsBean(DSManager.class.getSimpleName())) {
            Dew.applicationContext.getBean(DSManager.class);
        }
        Dew.applicationContext.containsBean(EntityContainer.class.getSimpleName());
        // JDBC Scan
        ClassPathScanner scanner = new ClassPathScanner((BeanDefinitionRegistry) ( (GenericApplicationContext) Dew.applicationContext).getBeanFactory());
        scanner.setResourceLoader(Dew.applicationContext);
        scanner.registerFilters();
        scanner.scan(StringUtils.tokenizeToStringArray(Dew.dewConfig.getDao().getBasePackage(), ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));

    }

    public static class Constant {
        // token存储key
        public static final String TOKEN_INFO_FLAG = "dew:auth:token:info:";
        // Token Id 关联 key : dew:auth:token:id:rel:<code> value : <token Id>
        public static final String TOKEN_ID_REL_FLAG = "dew:auth:token:id:rel:";

        public static final String MQ_AUTH_TENANT_ADD = "dew.auth.tenant.add";
        public static final String MQ_AUTH_TENANT_REMOVE = "dew.auth.tenant.remove";
        public static final String MQ_AUTH_RESOURCE_ADD = "dew.auth.resource.add";
        public static final String MQ_AUTH_RESOURCE_REMOVE = "dew.auth.resource.remove";
        public static final String MQ_AUTH_ROLE_ADD = "dew.auth.role.add";
        public static final String MQ_AUTH_ROLE_REMOVE = "dew.auth.role.remove";
        public static final String MQ_AUTH_ACCOUNT_ADD = "dew.auth.account.add";
        public static final String MQ_AUTH_ACCOUNT_REMOVE = "dew.auth.account.remove";

        public static final String MQ_AUTH_REFRESH = "dew.auth.refresh";

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
                name = Dew.applicationContext.getId();
                ip = InetAddress.getLocalHost().getHostAddress();
                host = InetAddress.getLocalHost().getHostName();
                instance = $.field.createUUID();
            } catch (UnknownHostException e) {
                logger.error("Dew info fetch error.", e);
            }
        }

    }

    public static Cluster cluster = new Cluster();

    public static ApplicationContext applicationContext;

    public static DS ds() {
        return DSManager.select("");
    }

    public static DS ds(String dsName) {
        return DSManager.select(dsName);
    }

    public static DewConfig dewConfig;

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

        public static void setServiceClient(RestTemplate _serviceClient) {
            serviceClient = _serviceClient;
        }

        public static HttpHelper.WrapHead get(String url) {
            return get(url, null);
        }

        public static HttpHelper.WrapHead get(String url, Map<String, String> header) {
            return exchange(HttpMethod.GET, url, null, header);
        }

        public static HttpHelper.WrapHead delete(String url) {
            return delete(url, null);
        }

        public static HttpHelper.WrapHead delete(String url, Map<String, String> header) {
            return exchange(HttpMethod.DELETE, url, null, header);
        }

        public static HttpHelper.WrapHead head(String url) {
            return head(url, null);
        }

        public static HttpHelper.WrapHead head(String url, Map<String, String> header) {
            return exchange(HttpMethod.HEAD, url, null, header);
        }

        public static HttpHelper.WrapHead options(String url) {
            return options(url, null);
        }

        public static HttpHelper.WrapHead options(String url, Map<String, String> header) {
            return exchange(HttpMethod.OPTIONS, url, null, header);
        }

        public static HttpHelper.WrapHead post(String url, Object body) {
            return post(url, body, null);
        }

        public static HttpHelper.WrapHead post(String url, Object body, Map<String, String> header) {
            return exchange(HttpMethod.POST, url, body, header);
        }

        public static HttpHelper.WrapHead put(String url, Object body) {
            return put(url, body, null);
        }

        public static HttpHelper.WrapHead put(String url, Object body, Map<String, String> header) {
            return exchange(HttpMethod.PUT, url, body, header);
        }

        private static HttpHelper.WrapHead exchange(HttpMethod httpMethod, String url, Object body, Map<String, String> header) {
            try {
                if (!$.field.isIPv4Address(new URL(url).getHost())) {
                    HttpHeaders headers = new HttpHeaders();
                    if (header != null) {
                        header.forEach(headers::add);
                    }
                    tryAttachTokenToHeader(headers);
                    HttpEntity entity;
                    if (body != null) {
                        entity = new HttpEntity(body, headers);
                    } else {
                        entity = new HttpEntity(headers);
                    }
                    ResponseEntity resp = serviceClient.exchange(tryAttachTokenToUrl(url), httpMethod, entity, Object.class);
                    HttpHelper.WrapHead wrapHead = new HttpHelper.WrapHead();
                    wrapHead.head = new HashMap<>();
                    for (Map.Entry<String, List<String>> entry : resp.getHeaders().entrySet()) {
                        wrapHead.head.put(entry.getKey(), entry.getValue().size() > 0 ? entry.getValue().get(0) : "");
                    }
                    wrapHead.result = $.json.toJsonString(resp.getBody());
                    return wrapHead;
                } else {
                    return $.http.request(httpMethod.name(), tryAttachTokenToUrl(url), body, header, null, null, 0);
                }
            } catch (IOException e) {
                logger.error("EB Process error.", e);
                return null;
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

        private static ExecutorService executorService = Executors.newCachedThreadPool();

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

    public static class Auth {

        public static Optional<OptInfo> getOptInfo() {
            return Dew.context().optInfo();
        }

        public static Optional<OptInfo> getOptInfo(String token) {
            String optInfoStr = Dew.cluster.cache.get(Dew.Constant.TOKEN_INFO_FLAG + token);
            if (optInfoStr != null && !optInfoStr.isEmpty()) {
                return Optional.of($.json.toObject(optInfoStr, OptInfo.class));
            } else {
                return Optional.empty();
            }
        }

        public static void removeOptInfo() {
            Optional<OptInfo> tokenInfoOpt = getOptInfo();
            if (tokenInfoOpt.isPresent()) {
                Dew.cluster.cache.del(Dew.Constant.TOKEN_ID_REL_FLAG + tokenInfoOpt.get().getAccountCode());
                Dew.cluster.cache.del(Dew.Constant.TOKEN_INFO_FLAG + tokenInfoOpt.get().getToken());
            }
        }

        public static void removeOptInfo(String token) {
            Optional<OptInfo> tokenInfoOpt = getOptInfo(token);
            if (tokenInfoOpt.isPresent()) {
                Dew.cluster.cache.del(Dew.Constant.TOKEN_ID_REL_FLAG + tokenInfoOpt.get().getAccountCode());
                Dew.cluster.cache.del(Dew.Constant.TOKEN_INFO_FLAG + token);
            }
        }

        public static Optional<OptInfo> getOptInfoByAccCode(String accountCode) {
            String token = Dew.cluster.cache.get(Dew.Constant.TOKEN_ID_REL_FLAG + accountCode);
            if (token != null && !token.isEmpty()) {
                return Optional.of($.json.toObject(Dew.cluster.cache.get(Dew.Constant.TOKEN_INFO_FLAG + token), OptInfo.class));
            } else {
                return Optional.empty();
            }
        }

        public static void setOptInfo(OptInfo optInfo) {
            Dew.cluster.cache.del(Dew.Constant.TOKEN_INFO_FLAG + Dew.cluster.cache.get(Dew.Constant.TOKEN_ID_REL_FLAG + optInfo.getAccountCode()));
            Dew.cluster.cache.set(Dew.Constant.TOKEN_ID_REL_FLAG + optInfo.getAccountCode(), optInfo.getToken());
            Dew.cluster.cache.set(Dew.Constant.TOKEN_INFO_FLAG + optInfo.getToken(), $.json.toJsonString(optInfo));
        }

    }

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
            logger.error("Throw Exception Convert error", ex);
        }
        return ex;
    }

}
