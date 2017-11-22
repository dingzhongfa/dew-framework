package com.tairanchina.csp.dew.idempotent.interceptor;

import com.ecfront.dew.common.Resp;
import com.tairanchina.csp.dew.core.controller.ErrorController;
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
import javax.validation.ValidationException;

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
        Resp<DewIdempotentProcessor> processor = DewIdempotent.initProcessor(storageStrategy, optType, optId);
        if (!processor.ok()) {
            ErrorController.error(request, response, 400, processor.getMessage(), ValidationException.class.getName());
            return false;
        }
        switch (processor.getBody().getStatus(optType, optId)) {
            case NOT_EXIST:
                processor.getBody().storage(optType, optId, needConfirm ? StatusEnum.UN_CONFIRM : StatusEnum.CONFIRMED, expireMs);
                return super.preHandle(request, response, handler);
            case UN_CONFIRM:
                ErrorController.error(request, response, 409, "The last operation was still going on, please wait.", DewIdempotentException.class.getName());
                return false;
            case CONFIRMED:
                ErrorController.error(request, response, 423, "Resources have been processed, can't repeat the request.", DewIdempotentException.class.getName());
                return false;
            default:
                return false;
        }
    }

}
