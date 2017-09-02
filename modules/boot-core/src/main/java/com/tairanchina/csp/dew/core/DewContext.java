package com.tairanchina.csp.dew.core;

import com.ecfront.dew.common.$;
import com.tairanchina.csp.dew.core.dto.OptInfo;

import java.util.Optional;

public class DewContext {

    private static ThreadLocal<DewContext> context = new ThreadLocal<>();

    private String id;
    private String sourceIP;
    private String requestUri;
    private String token;
    private Optional<OptInfo> innerOptInfo = Optional.empty();

    public Optional<OptInfo> optInfo() {
        if (innerOptInfo.isPresent()) {
            return innerOptInfo;
        }
        if (token != null && !token.isEmpty()) {
            innerOptInfo = Dew.Auth.getOptInfo(token);
        } else {
            innerOptInfo = Optional.empty();
        }
        return innerOptInfo;
    }

    public static DewContext getContext() {
        DewContext cxt = context.get();
        if (cxt == null) {
            cxt = new DewContext();
            cxt.id = $.field.createUUID();
            cxt.sourceIP = Dew.Info.ip;
            cxt.requestUri = "";
            cxt.token = "";
            setContext(cxt);
        }
        return cxt;
    }

    public static void setContext(DewContext _context) {
        if (_context.token == null) {
            _context.token = "";
        }
        context.set(_context);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public static void setContext(ThreadLocal<DewContext> context) {
        DewContext.context = context;
    }
}
