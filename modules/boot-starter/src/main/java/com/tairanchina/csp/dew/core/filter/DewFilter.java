package com.tairanchina.csp.dew.core.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

/**
 * desription:
 * Created by ding on 2017/11/13.
 */

public class DewFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(DewFilter.class);

    // url->(timestamp,resTime)
    public static final Map<String,LinkedHashMap<Long,Integer>> responseMap = new WeakHashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("dewFilter Started");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        long start = Instant.now().toEpochMilli();
        filterChain.doFilter(servletRequest, servletResponse);
        int resTime = (int) (Instant.now().toEpochMilli() - start);
        String url = httpServletRequest.getRequestURI();
        if (responseMap.containsKey(url)){
            responseMap.get(url).put(start,resTime);
        }{
            responseMap.put(url,new LinkedHashMap<Long,Integer>(){{
                    put(start,resTime);
                }});
        }
    }

    @Override
    public void destroy() {
        logger.info("dewFilter destroyed");
    }
}
