package com.tairanchina.csp.dew.auth;

import com.tairanchina.csp.dew.core.auth.AuthAdapter;
import com.tairanchina.csp.dew.core.dto.OptInfo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * desription: 用户中心适配
 * Created by ding on 2017/10/27.
 */
@Component("ucAuthAdapter")
@ConditionalOnExpression("#{'${dew.security.auth-adapter}'=='uc'}")
public class UCAuthAdapter extends UCAuth implements AuthAdapter {



    @Override
    public <E extends OptInfo> Optional<E> getOptInfo(String token) {
        return getUCOptInfo(token);
    }

    @Override
    public void removeOptInfo(String token) {
        removeUCOptInfo(token);
    }

    @Override
    public <E extends OptInfo> void setOptInfo(E optInfo) {
        setUCOptInfo(optInfo);
    }
}
