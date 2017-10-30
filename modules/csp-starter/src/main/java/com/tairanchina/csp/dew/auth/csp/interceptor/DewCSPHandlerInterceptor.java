package com.tairanchina.csp.dew.auth.csp.interceptor;

import com.ecfront.dew.common.$;
import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.auth.csp.CSPAuthAdapter;
import com.tairanchina.csp.dew.auth.csp.CSPOptInfo;
import com.tairanchina.csp.dew.auth.csp.DewCSPConfig;
import com.tairanchina.csp.dew.core.DewContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Arrays;

/**
 * Dew Servlet拦截器
 */
public class DewCSPHandlerInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(DewCSPHandlerInterceptor.class);

    @Autowired
    private CSPAuthAdapter cspAuthAdapter;
    @Autowired
    private DewCSPConfig dewCSPConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token;
        if (Dew.dewConfig.getSecurity().isTokenInHeader()) {
            token = request.getHeader(Dew.dewConfig.getSecurity().getTokenFlag());
        } else {
            token = request.getParameter(Dew.dewConfig.getSecurity().getTokenFlag());
        }
        if (token != null) {
            token = URLDecoder.decode(token, "UTF-8");
            if (Dew.dewConfig.getSecurity().isTokenHash()) {
                token = $.security.digest.digest(token, "MD5");
            }
        }
        DewContext context = new DewContext();
        context.setId($.field.createUUID());
        context.setSourceIP(Dew.Util.getRealIP(request));
        context.setRequestUri(request.getRequestURI());
        context.setToken(token);
        DewContext.setContext(context);
        CSPOptInfo cspOptInfo = new CSPOptInfo();
        cspOptInfo.setAccountCode(request.getHeader(dewCSPConfig.getPartyId()));
        cspOptInfo.setRoles(Arrays.asList(request.getHeader(dewCSPConfig.getRoles()).split(",")));
        cspOptInfo.setAppId(request.getHeader(dewCSPConfig.getAppId()));
        cspAuthAdapter.setOptInfo(cspOptInfo);
        logger.trace("[{}] {}{} from {}", request.getMethod(), request.getRequestURI(), request.getQueryString() == null ? "" : "?" + request.getQueryString(), context.getSourceIP());
        return super.preHandle(request, response, handler);
    }

}