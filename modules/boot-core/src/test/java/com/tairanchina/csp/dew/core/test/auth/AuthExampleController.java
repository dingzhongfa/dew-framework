package com.tairanchina.csp.dew.core.test.auth;

import com.ecfront.dew.common.$;
import com.ecfront.dew.common.Resp;
import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.DewContext;
import com.tairanchina.csp.dew.core.logger.DewLogger;
import com.tairanchina.csp.dew.core.test.auth.dto.OptInfoExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class AuthExampleController {

    private static Map<String, User> MOCK_USER_CONTAINER = new HashMap<>();

    private Logger dewLogger = DewLogger.getLogger(AuthExampleController.class);
    private Logger oriLogger = LoggerFactory.getLogger(AuthExampleController.class);

    @PostConstruct
    public void init(){
        DewContext.setOptInfoClazz(OptInfoExt.class);
    }

    /**
     * 模拟用户注册
     */
    @PostMapping(value = "user/register")
    public Resp<Void> register(@RequestBody User user) {
        // 实际注册处理
        user.setId($.field.createUUID());
        MOCK_USER_CONTAINER.put(user.getId(), user);
        dewLogger.info("dewLogger:        TEST");
        oriLogger.info("oriLogger:        TEST");
        return Resp.success(null);
    }

    /**
     * 模拟用户登录
     */
    @PostMapping(value = "auth/login")
    public Resp<String> login(@RequestBody LoginDTO loginDTO) throws Exception {
        // 实际登录处理
        User user = MOCK_USER_CONTAINER.values().stream().filter(u -> u.getIdCard().equals(loginDTO.getIdCard())).findFirst().get();
        if (!loginDTO.getPassword().equals(user.getPassword())) {
            throw Dew.E.e("ASXXX0", new Exception("密码错误"));
        }
        String token = $.field.createUUID();
        Dew.Auth.setOptInfo(new OptInfoExt()
                .setIdCard(user.getIdCard())
                .setAccountCode($.field.createShortUUID())
                .setToken(token)
                .setName(user.getName())
                .setMobile(user.getPhone()));
        dewLogger.info("dewLogger:        TEST");
        oriLogger.info("oriLogger:        TEST");
        return Resp.success(token);
    }

    /**
     * 模拟业务操作
     */
    @GetMapping(value = "business/someopt")
    public Resp<? extends Object> someOpt() {

        // 获取登录用户信息
        Optional<OptInfoExt> optInfoExtOpt = Dew.Auth.getOptInfo();
        dewLogger.info("dewLogger:        TEST");
        oriLogger.info("oriLogger:        TEST");
        return optInfoExtOpt.<Resp<? extends Object>>map(Resp::success).orElseGet(() -> Resp.unAuthorized("用户认证错误"));
    }

    /**
     * 模拟用户注销
     */
    @DeleteMapping(value = "auth/logout")
    public Resp<Void> logout() {
        dewLogger.info("dewLogger:        TEST");
        oriLogger.info("oriLogger:        TEST");
        // 实际注册处理
        Dew.Auth.removeOptInfo();
        return Resp.success(null);
    }

    public static class LoginDTO {

        private String idCard;

        private String password;

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class User {

        private String id;

        private String name;

        private String phone;

        private String idCard;

        private String password;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }
    }

}
