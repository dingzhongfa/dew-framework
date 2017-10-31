package com.tairanchina.csp.dew.auth.csp;

import com.ecfront.dew.common.Resp;
import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.auth.csp.CSPOptInfo;
import com.tairanchina.csp.dew.core.DewContext;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * desription:
 * Created by ding on 2017/10/30.
 */
@RestController
@RequestMapping("csp-auth/")
public class CSPAuthController {

    @PostConstruct
    public void init() {
        DewContext.setOptInfoClazz(CSPOptInfo.class);
    }


    /**
     * 模拟业务操作
     */
    @GetMapping(value = "business/someopt")
    public Resp<?> someOpt() {
        // 获取登录用户信息
        Optional<CSPOptInfo> optInfoExtOpt = Dew.auth.getOptInfo();
        return optInfoExtOpt.<Resp<?>>map(Resp::success).orElseGet(() -> Resp.unAuthorized("用户认证错误"));
    }

    /**
     * 模拟用户注销
     */
    @DeleteMapping(value = "auth/logout")
    public Resp<Void> logout() {
        // 实际注册处理
        Dew.auth.removeOptInfo();
        return Resp.success(null);
    }

}
