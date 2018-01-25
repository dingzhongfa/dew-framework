package com.tairanchina.csp.dew.auth.csp.interceptor;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.auth.csp.CSPOptInfo;
import com.tairanchina.csp.dew.auth.csp.DewCSPAuthAutoConfiguration;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * Dew Servlet拦截器
 */
public class DewCSPHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        CSPOptInfo cspOptInfo = new CSPOptInfo();
        cspOptInfo.setToken(Dew.context().getToken());
        cspOptInfo.setAccountCode(request.getHeader(DewCSPAuthAutoConfiguration.dewCSPConfig.getPartyId()));
        String roles = request.getHeader(DewCSPAuthAutoConfiguration.dewCSPConfig.getRoles());
        if (roles != null) {
            cspOptInfo.setRoles(Arrays.asList(roles.split(",")));
        }
        cspOptInfo.setAppId(request.getHeader(DewCSPAuthAutoConfiguration.dewCSPConfig.getAppId()));
        Dew.auth.setOptInfo(cspOptInfo);
        return super.preHandle(request, response, handler);
    }

}
