package com.tairanchina.csp.dew.idempotent.interceptor;

import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.idempotent.DewIdempotent;
import com.tairanchina.csp.dew.idempotent.DewIdempotentConfig;
import com.tairanchina.csp.dew.idempotent.strategy.DewIdempotentProcessor;
import com.tairanchina.csp.dew.idempotent.strategy.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class DewIdempotentHandlerInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private DewIdempotentConfig dewIdempotentConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String optType = request.getHeader(dewIdempotentConfig.getOptTypeFlag());
        if (StringUtils.isEmpty(optType)) {
            optType = request.getParameter(dewIdempotentConfig.getOptTypeFlag());
        }
        String optId = request.getHeader(dewIdempotentConfig.getOptIdFlag());
        if (StringUtils.isEmpty(optId)) {
            optId = request.getParameter(dewIdempotentConfig.getOptIdFlag());
        }
        if (StringUtils.isEmpty(optType) || StringUtils.isEmpty(optId)) {
            // 不需要处理
            return super.preHandle(request, response, handler);
        }
        String forceIgnoreCheck = request.getHeader(dewIdempotentConfig.getForceIgnoreCheckFlag());
        if (StringUtils.isEmpty(forceIgnoreCheck)) {
            forceIgnoreCheck = request.getParameter(dewIdempotentConfig.getForceIgnoreCheckFlag());
        }
        if (!StringUtils.isEmpty(forceIgnoreCheck) && Boolean.valueOf(forceIgnoreCheck)) {
            // 不需要处理
            return super.preHandle(request, response, handler);
        }
        // 参数设置
        String expireMsTmp = request.getHeader(dewIdempotentConfig.getOptExpireMsFlag());
        if (StringUtils.isEmpty(expireMsTmp)) {
            expireMsTmp = request.getParameter(dewIdempotentConfig.getOptExpireMsFlag());
        }
        long expireMs = StringUtils.isEmpty(expireMsTmp) ? dewIdempotentConfig.getDefaultExpireMs() : Long.valueOf(expireMsTmp);
        String needConfirmTmp = request.getHeader(dewIdempotentConfig.getOptNeedConfirmFlag());
        if (StringUtils.isEmpty(needConfirmTmp)) {
            needConfirmTmp = request.getParameter(dewIdempotentConfig.getOptNeedConfirmFlag());
        }
        boolean needConfirm = StringUtils.isEmpty(needConfirmTmp) ? dewIdempotentConfig.isDefaultNeedConfirm() : Boolean.valueOf(needConfirmTmp);
        String storageStrategyTmp = request.getHeader(dewIdempotentConfig.getOptStorageStrategyFlag());
        if (StringUtils.isEmpty(storageStrategyTmp)) {
            storageStrategyTmp = request.getParameter(dewIdempotentConfig.getOptStorageStrategyFlag());
        }
        String storageStrategy = StringUtils.isEmpty(storageStrategyTmp) ? dewIdempotentConfig.getDefaultStorageStrategy() : storageStrategyTmp;
        // 处理
        DewIdempotentProcessor processor = DewIdempotent.initProcessor(storageStrategy, optType, optId);
        switch (processor.getStatus(optType, optId)) {
            case NOT_EXIST:
                processor.storage(optType, optId, needConfirm ? StatusEnum.UN_CONFIRM : StatusEnum.CONFIRMED, expireMs);
                return super.preHandle(request, response, handler);
            case UN_CONFIRM:
                throw Dew.E.e("409", new DewIdempotentException("The last operation was still going on, please wait."), 409);
            case CONFIRMED:
                throw Dew.E.e("423", new DewIdempotentException("Resources have been processed, can't repeat the request."), 423);
            default:
                return super.preHandle(request, response, handler);
        }
    }

}
