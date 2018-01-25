package com.tairanchina.csp.dew.core.metric;

import com.tairanchina.csp.dew.core.DewConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ConcurrentReferenceHashMap;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;


public class DewFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(DewFilter.class);

    private DewConfig dewConfig;

    public DewFilter(){

    }

    DewFilter(DewConfig dewConfig) {
        this.dewConfig = dewConfig;
    }

    // url->(timestamp,resTime)
    public static final Map<String, RecordMap<Long, Integer>> RECORD_MAP = new ConcurrentReferenceHashMap<>(50, ConcurrentReferenceHashMap.ReferenceType.SOFT);

    private static final String MATCHING_PATTERN_KEY = "org.springframework.web.servlet.HandlerMapping.bestMatchingPattern";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("dewFilter Started");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        long start = Instant.now().toEpochMilli();
        servletRequest.setAttribute("dew.metric.start", start);
        filterChain.doFilter(servletRequest, servletResponse);
        int resTime = (int) (Instant.now().toEpochMilli() - start);
        String method = httpServletRequest.getMethod();
        String matchingPattern = (String) httpServletRequest.getAttribute(MATCHING_PATTERN_KEY);
        if (matchingPattern != null && !matchingPattern.endsWith("/favicon.ico")) {
            String key = "{[" + method + "]:" + matchingPattern + "}";
            if (RECORD_MAP.containsKey(key)) {
                RECORD_MAP.get(key).put(start, resTime);
            } else {
                RECORD_MAP.put(key, new RecordMap<Long, Integer>(RequestType.NORMAL) {{
                    put(start, resTime);
                }});
            }
        }
    }

    @Override
    public void destroy() {
        logger.info("dewFilter destroyed");
    }

    public class RecordMap<K, V> extends LinkedHashMap<K, V> {

        private RequestType requestType;

        public RecordMap(RequestType requestType) {
            this.requestType = requestType;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            if (requestType.equals(RequestType.NORMAL)) {
                return size() > dewConfig.getMetric().getUrlSize();
            }
            return size() > 2000;
        }
    }
}
