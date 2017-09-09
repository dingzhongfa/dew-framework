package com.tairanchina.csp.dew.example.mybatisplus.service;

import com.tairanchina.csp.dew.model.Account;
import com.tairanchina.csp.dew.storage.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by panshuai on 17/6/29.
 */
@Service
public class AccountService {

    @Autowired
    private AccountMapper mapper;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Boolean transfer(Integer turnOutUserId, Integer turnOntUserId, Integer amount){

        //操作输出账户
        Account account = mapper.getAccount(turnOutUserId);
        if(account == null){
            return Boolean.FALSE;
        }
        Long oldVersion = account.getVersion();
        Integer userAmount = account.getAmount() - amount;
        if(userAmount < 0){
            return Boolean.FALSE;
        }
        Long newVersion = oldVersion + 1;
        int res = mapper.updateAmount(account.getId(), userAmount, newVersion, oldVersion);

        if(res == 0) {
            throw new RuntimeException("交易失败");
        }

        //操作输入账户
        account = mapper.getAccount(turnOntUserId);
        if(account == null){
            return Boolean.FALSE;
        }
        oldVersion = account.getVersion();
        userAmount = account.getAmount() + amount;
        newVersion = oldVersion + 1;
        res = mapper.updateAmount(account.getId(), userAmount, newVersion, oldVersion);

        if(res == 0) {
            throw new RuntimeException("交易失败");
        }

        return Boolean.TRUE;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Boolean addAmount(Integer userId, Integer amount){

        Account account = mapper.getAccount(userId);
        if(account == null){
            return Boolean.FALSE;
        }
        Long oldVersion = account.getVersion();
        Integer userAmount = account.getAmount() + amount;
        Long newVersion = oldVersion + 1;
        int res = mapper.updateAmount(account.getId(), userAmount, newVersion, oldVersion);
        if(res == 0 ){
            throw new RuntimeException("交易失败");
        }
        return Boolean.TRUE;
    }

    public Integer getAmount(Integer userId) {
        Account account = mapper.getAccount(userId);
        if(account == null){
            return 0;
        }
        return account.getAmount();
    }
}
