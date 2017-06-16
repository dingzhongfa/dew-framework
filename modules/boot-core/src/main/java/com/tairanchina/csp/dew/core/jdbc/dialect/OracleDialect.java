package com.tairanchina.csp.dew.core.jdbc.dialect;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class OracleDialect implements Dialect {

    @Override
    public String paging(String sql, long pageNumber, int pageSize) {
        return "SELECT * FROM (SELECT rownum rn , originaltable.* FROM (" + sql + ") originaltable  WHERE rownum<=" + (pageNumber * pageSize) + ") WHERE rn > " + (pageNumber - 1) * pageSize;
    }

    @Override
    public String count(String sql) {
        return "SELECT COUNT(1) FROM ( " + sql + " ) ";
    }

    @Override
    public String getTableInfo(String tableName) {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public String getDriver() {
        return "oracle.jdbc.driver.OracleDriver";
    }

    @Override
    public DialectType getDialectType() {
        return DialectType.ORACLE;
    }


}
