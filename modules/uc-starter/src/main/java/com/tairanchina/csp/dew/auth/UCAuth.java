package com.tairanchina.csp.dew.auth;

import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.core.dto.OptInfo;
import com.tairanchina.csp.foundation.sdk.CSPKernelSDK;
import com.tairanchina.csp.foundation.sdk.dto.ClientTokenDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;


public class UCAuth{

    private static final Logger logger = LoggerFactory.getLogger(UCAuthAdapter.class);

    @Autowired
    CSPKernelSDK cspKernelSDK;

    public <E extends OptInfo> Optional<E> getUCOptInfo(String token) {
        ClientTokenDTO clientTokenDTO = null;
        try {
            clientTokenDTO = (ClientTokenDTO) cspKernelSDK.user.validateToken(token);
        } catch (Exception e) {
            logger.error("ucenter服务异常");
            return Optional.empty();
        }
        return Optional.of(convertOptInfo(token, clientTokenDTO.getPersonId()));
    }

    public void removeUCOptInfo(String token) {
        logger.error("Not implemented", new Exception("Not implemented"));
    }

    public <E extends OptInfo> void setUCOptInfo(E optInfo) {
        logger.error("Not implemented", new Exception("Not implemented"));
    }

    private <E extends OptInfo> E convertOptInfo(String jwtToken, String partyId) {
        E optInfo = null;
        try {
            optInfo = (E) Dew.context().getOptInfoClazz().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Convert OptInfo error", e);
        }
        optInfo.setToken(jwtToken);
        optInfo.setAccountCode(partyId);
        return optInfo;
    }

}
