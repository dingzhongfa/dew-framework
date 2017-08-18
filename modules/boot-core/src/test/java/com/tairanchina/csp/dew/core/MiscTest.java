package com.tairanchina.csp.dew.core;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.tairanchina.csp.dew.core.jdbc.DS;
import org.junit.Test;

public class MiscTest {

    @Test
    public void testReplaceAll() {
        String str = "select * from t_test_crud_s_entity where 1 =1 and  field_a= #{ fieldA } and field_c = #{fc} order by code desc";
        str = str.replaceAll("((and)|(or)|(AND)|(OR))(\\s*\\S*)*\\#(\\s*\\S*)*\\}", "");
        System.out.println(str);
    }

    @Test
    public void testSqlParser() {
        String sql = "select * from table";
        SQLStatementParser parser = new SQLStatementParser(sql);
        SQLSelectStatement statement = (SQLSelectStatement) parser.parseStatementList().get(0);
        SQLExpr sqlExpr = ((SQLSelectQueryBlock) statement.getSelect().getQuery()).getWhere();
        DS.formatWhere(sqlExpr);
        System.out.println(statement.toString());
        sql = "select * from table where  id = #{sss}";
        parser = new SQLStatementParser(sql);
        statement = (SQLSelectStatement) parser.parseStatementList().get(0);
        sqlExpr = ((SQLSelectQueryBlock) statement.getSelect().getQuery()).getWhere();
        DS.formatWhere(sqlExpr);
        System.out.println(statement.toString());
        sql = "select * from table where f1 between #{x} and #{y}";
        parser = new SQLStatementParser(sql);
        statement = (SQLSelectStatement) parser.parseStatementList().get(0);
        sqlExpr = ((SQLSelectQueryBlock) statement.getSelect().getQuery()).getWhere();
        DS.formatWhere(sqlExpr);
        System.out.println(statement.toString());
        sql = "select * from table where" +
                " id = #{sss} and ( name1= ? or name2 = #{aaa} or name3 = ? ) " +
                "and age in (?,?) and no1 like ? and no2 like #{aaa} " +
                "or f1 between #{x} and #{y} or f2 = ?";
        parser = new SQLStatementParser(sql);
        statement = (SQLSelectStatement) parser.parseStatementList().get(0);
        sqlExpr = ((SQLSelectQueryBlock) statement.getSelect().getQuery()).getWhere();
        DS.formatWhere(sqlExpr);
        System.out.println(statement.toString());
        sql = "select * from t1 left join t2 tt on t1.id=t2.rel_id where" +
                " t1.id = #{sss} and ( t1.name1= ? or t1.name2 = #{aaa} or t1.name3 = ? ) " +
                "and tt.age in (?,?) and tt.no1 like ? and tt.no2 like #{aaa} " +
                "or tt.f1 between #{x} and #{y} or tt.f2 = ?";
        parser = new SQLStatementParser(sql);
        statement = (SQLSelectStatement) parser.parseStatementList().get(0);
        sqlExpr = ((SQLSelectQueryBlock) statement.getSelect().getQuery()).getWhere();
        DS.formatWhere(sqlExpr);
        System.out.println(statement.toString());
    }

}
