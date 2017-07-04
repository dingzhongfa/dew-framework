package com.tairanchina.csp.dew.model;

/**
 * Created by panshuai on 17/6/29.
 */
public class Account {

    /** 用户id **/
    private Integer id;
    /** 用户姓名 **/
    private String userName;
    /** 年龄 **/
    private Integer age;
    /** 金额分 **/
    private Integer amount;
    /** 版本号 **/
    private Long version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
