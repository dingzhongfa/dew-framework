package com.tairanchina.csp.dew.example.bone;


import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 在根路径创建用于初始化数据/行为的类
 * <p>
 * 减少滥用PostConstruct造成的不可控因素
 */
@Component
public class BoneExampleInitiator {

    @PostConstruct
    public void init() {
        // 在这里初始化
    }

}
