package com.tairanchina.csp.dew.core.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * desription:
 * Created by ding on 2017/11/13.
 */

public class DewFilter implements Filter {


    private final Logger logger = LoggerFactory.getLogger(DewFilter.class);

    // url->(timestamp,resTime)
    public static final Map<String, LinkedHashMap<Long, Integer>> responseMap = new WeakHashMap<>();

    public static long start;

    private final String matchingPatternKey = "org.springframework.web.servlet.HandlerMapping.bestMatchingPattern";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("dewFilter Started");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        start = Instant.now().toEpochMilli();
        filterChain.doFilter(servletRequest, servletResponse);
        int resTime = (int) (Instant.now().toEpochMilli() - start);
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String method = httpServletRequest.getMethod();
        String matchingPattern = (String) httpServletRequest.getAttribute(matchingPatternKey);
        if (matchingPattern != null && !matchingPattern.endsWith("/favicon.ico")) {
            String key = "{[" + method + "]:" + matchingPattern + "]";
            if (responseMap.containsKey(key)) {
                responseMap.get(key).put(start, resTime);
            } else {
                responseMap.put(key, new LinkedHashMap<Long, Integer>() {{
                    put(start, resTime);
                }});
            }
        }
    }

    @Override
    public void destroy() {
        logger.info("dewFilter destroyed");
    }
}
