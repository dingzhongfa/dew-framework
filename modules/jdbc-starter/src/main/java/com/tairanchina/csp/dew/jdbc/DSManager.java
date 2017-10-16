package com.tairanchina.csp.dew.jdbc;

import com.tairanchina.csp.dew.core.Dew;

public interface DSManager {

    static DewDS select(String dsName) {
        if (dsName == null) {
            dsName = "";
        }
        if (dsName.isEmpty()) {
            return (DewDS) Dew.applicationContext.getBean("ds");
        } else {
            return (DewDS) Dew.applicationContext.getBean(dsName + "DS");
        }
    }

}
