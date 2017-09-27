package com.tairanchina.csp.dew.core.auth;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.dto.OptInfo;
import com.tairanchina.csp.ucenter.tool.sdk.jwt.JwtHelper;
import com.tairanchina.csp.ucenter.tool.sdk.model.BasicJwtInfo;
import com.tairanchina.csp.ucenter.tool.sdk.user.UserInfoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
@ConditionalOnExpression("#{'${dew.security.auth-adapter}'=='uc'}")
public class UCAuthAdapter implements AuthAdapter {

    private static final Logger logger = LoggerFactory.getLogger(UCAuthAdapter.class);

    private JwtHelper jwtHelper;
    private UserInfoHelper userInfoHelper;
    private String appId;
    private String secret;
    private String serverJwt;
    private String publicKey;

    @PostConstruct
    public void init() throws Exception {
        // SDK init
        jwtHelper = new JwtHelper();
        userInfoHelper = new UserInfoHelper();
        appId = Dew.dewConfig.getSecurity().getUcAuthAdapter().getAppId();
        secret = Dew.dewConfig.getSecurity().getUcAuthAdapter().getAppSecret();
        serverJwt = jwtHelper.grantToken(appId, secret, Long.MAX_VALUE);
        // 根据AppId获取公钥（调用时需要带上serverJwt进行鉴权）
        publicKey = jwtHelper.getPublicKey(appId, serverJwt);
    }

    @Override
    public <E extends OptInfo> Optional<E> getOptInfo(String token) {
        // 拿到公钥后 对用户的jwt进行校验 并获取其中的信息
        BasicJwtInfo basicJwtInfo = jwtHelper.validateClientToken(publicKey, token);
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
