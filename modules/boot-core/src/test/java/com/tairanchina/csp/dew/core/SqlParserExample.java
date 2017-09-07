package com.tairanchina.csp.dew.core;


import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObjectImpl;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.tairanchina.csp.dew.core.entity.EntityContainer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SqlParserExample {

    public static void main(String[] args) {


        String sql = "SELECT *\n" +
                "FROM app_version a\n" +
                "WHERE a.name = #{name} AND a.inner_number = #{innerNumber} " +
                "AND a.force_number = #{forceNumber} AND a.device_type = #{deviceType} AND a.app_flag = #{appFlag}";
        SQLStatementParser parser = new SQLStatementParser(sql);
        SQLSelectStatement statement = (SQLSelectStatement) parser.parseStatementList().get(0);
        SQLExpr sqlExpr = ((SQLSelectQueryBlock) statement.getSelect().getQuery()).getWhere();
        SQLTableSource sqlTableSource = ((SQLSelectQueryBlock) statement.getSelect().getQuery()).getFrom();
        List<SQLSelectItem> selectList1 = ((SQLSelectQueryBlock) statement.getSelect().getQuery()).getSelectList();

        String sql2 = "SELECT n.*,r2.*,r.*\n" +
                "FROM node n \n" +
                "INNER JOIN node_relation r ON n.id = r.from_id \n" +
                "LEFT JOIN node_relation2 r2 ON n.id = r.from_id \n" +
                "WHERE n.type = #{type} AND n.level = #{level} AND r.from_id = #{fromId} AND r.to_id = #{toId}";

        String sql3 = "SELECT *\n" +
                "FROM app_version\n" +
                "WHERE device_type = #{deviceType} AND app_flag = #{appFlag}\n" +
                "ORDER BY inner_number DESC\n" +
                "LIMIT 1";

        String sql4 = "SELECT * FROM product ORDER BY created_time DESC LIMIT 1";

        SQLStatementParser parser2 = new SQLStatementParser(sql4);
        SQLSelectStatement statement2 = (SQLSelectStatement) parser2.parseStatementList().get(0);
        SQLTableSource sqlTableSource2 = ((SQLSelectQueryBlock) statement2.getSelect().getQuery()).getFrom();
        List<SQLSelectItem> addList = new ArrayList<>();
        List<SQLSelectItem> selectList = ((SQLSelectQueryBlock) statement2.getSelect().getQuery()).getSelectList();
        SqlParserExample.formatFrom(sqlTableSource2, selectList, addList);
        selectList.addAll(addList);
        String result = statement2.toString();
        System.out.println(result);


    }

    private static void formatFrom(SQLTableSource sqlTableSource, List<SQLSelectItem> selectList, List<SQLSelectItem> addList) {
        if (sqlTableSource == null) {
            return;
        }
        if (sqlTableSource instanceof SQLExprTableSource) {
            doFormat((SQLExprTableSource) sqlTableSource, selectList, addList);
        }
        if (sqlTableSource instanceof SQLJoinTableSource) {
            formatFrom(((SQLJoinTableSource) sqlTableSource).getRight(), selectList, addList);
            formatFrom(((SQLJoinTableSource) sqlTableSource).getLeft(), selectList, addList);
        }
    }

    private static void doFormat(SQLExprTableSource sqlTableSource, List<SQLSelectItem> selectList, List<SQLSelectItem> addList) {
        System.out.println("表名：    " + sqlTableSource.getExpr() + "         别名：     " + sqlTableSource.getAlias());
        EntityContainer.EntityClassInfo entityClassInfo = EntityContainer.getEntityClassByClazz(underlineToCamel(((SQLIdentifierExpr) sqlTableSource.getExpr()).getName()));
        Iterator<SQLSelectItem> iterator = selectList.iterator();
        while (iterator.hasNext()) {
            SQLSelectItem sqlSelectItem = iterator.next();
            if (sqlSelectItem.getExpr() instanceof SQLPropertyExpr) {
                SQLPropertyExpr expr = (SQLPropertyExpr) sqlSelectItem.getExpr();
                SQLIdentifierExpr expr_owner = (SQLIdentifierExpr) expr.getOwner();
                if ((expr_owner.getName() + "." + expr.getName()).equals(sqlTableSource.getAlias() + ".*")) {
                    iterator.remove();
                    addList.add(new SQLSelectItem(new SQLPropertyExpr(expr_owner.getName(), "hehe1")));
                    addList.add(new SQLSelectItem(new SQLPropertyExpr(expr_owner.getName(), "hehe2")));
                    addList.add(new SQLSelectItem(new SQLPropertyExpr(expr_owner.getName(), "hehe3")));
                    addList.add(new SQLSelectItem(new SQLPropertyExpr(expr_owner.getName(), "hehe4")));
                }

            } else if (sqlSelectItem.getExpr() instanceof SQLObjectImpl) {

                System.out.println();
            }
        }

    }

    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == '_') {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


}
