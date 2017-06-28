package com.tairanchina.csp.dew.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tairanchina.csp.dew.entity.User;
import com.tairanchina.csp.dew.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by panshuai on 17/6/26.
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;

    public List<String> ageGroup(){
        return baseMapper.ageGroup();
//        return userMapper.ageGroup();
    }

}
