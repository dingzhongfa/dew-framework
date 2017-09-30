package com.tairanchina.csp.dew.core.auth;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.dto.OptInfo;
import com.tairanchina.csp.ucenter.tool.sdk.UCenterSDK;
import com.tairanchina.csp.ucenter.tool.sdk.exception.InvalidClientTokenException;
import com.tairanchina.csp.ucenter.tool.sdk.exception.SystemInternalException;
import com.tairanchina.csp.ucenter.tool.sdk.model.BasicJwtInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static com.tairanchina.csp.dew.core.Dew.Info.host;


@Component
@ConditionalOnExpression("#{'${dew.security.auth-adapter}'=='uc'}")
public class UCAuthAdapter implements AuthAdapter {

    private static final Logger logger = LoggerFactory.getLogger(UCAuthAdapter.class);

    private UCenterSDK sdk;

    @PostConstruct
    public void init() throws Exception {
        String appId = Dew.dewConfig.getSecurity().getUcAuthAdapter().getAppId();
        String secret = Dew.dewConfig.getSecurity().getUcAuthAdapter().getAppSecret();
        String host = Dew.dewConfig.getSecurity().getUcAuthAdapter().getHost();
        sdk = UCenterSDK.getInstance(host, appId, secret);
    }

    @Override
    public <E extends OptInfo> Optional<E> getOptInfo(String token) {
        BasicJwtInfo basicJwtInfo = null;
        try {
            basicJwtInfo = sdk.validateClientToken(token);
        } catch (InvalidClientTokenException e) {
            logger.error("ClientToken无效或不合法");
            return Optional.empty();
        } catch (SystemInternalException e) {
            logger.error("ucenter服务异常");
            return Optional.empty();
        }
        return Optional.of(convertOptInfo(token, basicJwtInfo.getPartyId()));
    }

    @Override
    public void removeOptInfo(String token) {
        logger.error("Not implemented", new Exception("Not implemented"));
    }

    @Override
    public <E extends OptInfo> void setOptInfo(E optInfo) {
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
