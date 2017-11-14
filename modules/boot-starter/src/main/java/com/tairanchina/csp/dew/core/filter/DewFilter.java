package com.tairanchina.csp.dew.core.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * desription:
 * Created by ding on 2017/11/13.
 */

public class DewFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(DewFilter.class);

    public static List<Integer> timeList = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("dewFilter Started");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long start = Instant.now().toEpochMilli();
        filterChain.doFilter(servletRequest, servletResponse);
        int resTime = (int) (Instant.now().toEpochMilli() - start);
        timeList.add(resTime);
    }

    @Override
    public void destroy() {
        logger.info("dewFilter destroyed");
    }
}
