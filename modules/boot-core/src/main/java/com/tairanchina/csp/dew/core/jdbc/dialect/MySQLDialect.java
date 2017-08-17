package com.tairanchina.csp.dew.core.jdbc.dialect;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

class MySQLDialect implements Dialect {

    @Override
    public String paging(String sql, long pageNumber, int pageSize) {
        return sql + " LIMIT " + (pageNumber - 1) * pageSize + ", " + pageSize;
    }

    @Override
    public String count(String sql) {
        return "SELECT COUNT(1) FROM ( " + sql + " ) _" + Math.abs(sql.hashCode());
    }

    @Override
    public String getTableInfo(String tableName) {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public String getDriver() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    public DialectType getDialectType() {
        return DialectType.MYSQL;
    }

}
