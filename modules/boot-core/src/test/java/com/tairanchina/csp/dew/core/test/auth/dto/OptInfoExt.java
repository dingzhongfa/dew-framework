package com.tairanchina.csp.dew.core.test.auth.dto;

import com.tairanchina.csp.dew.core.dto.OptInfo;

public class OptInfoExt extends OptInfo {

    private String idCard;

    public String getIdCard() {
        return idCard;
    }

    public OptInfoExt setIdCard(String idCard) {
        this.idCard = idCard;
        return this;
    }
}
