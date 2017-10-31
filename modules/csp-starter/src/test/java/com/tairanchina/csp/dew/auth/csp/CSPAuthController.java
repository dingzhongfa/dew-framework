package com.tairanchina.csp.dew.auth.csp;

import com.ecfront.dew.common.Resp;
import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.auth.csp.CSPOptInfo;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * desription:
 * Created by ding on 2017/10/30.
 */
@RestController
@RequestMapping("csp-auth/")
public class CSPAuthController {

    /**
     * 模拟业务操作
     */
    @GetMapping(value = "business/someopt")
    public Resp<Void> someOpt() {
        // 获取登录用户信息
        System.out.println(Thread.currentThread().getId());
        System.out.println(Thread.currentThread().getName());
        Optional<CSPOptInfo> optInfoExtOpt = Dew.auth.getOptInfo();
        if (!optInfoExtOpt.isPresent()) {
            return Resp.unAuthorized("用户认证错误");
        }
        // 登录用户的信息
        optInfoExtOpt.get();
        return Resp.success(null);
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
