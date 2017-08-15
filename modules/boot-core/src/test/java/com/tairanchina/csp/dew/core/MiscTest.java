package com.tairanchina.csp.dew.core;

import org.junit.Test;

public class MiscTest {

    @Test
    public void testReplaceAll(){
        String str="select * from t_test_crud_s_entity where 1 =1 and  field_a= #{ fieldA } and field_c = #{fc} order by code desc";
        str=str.replaceAll("((and)|(or)|(AND)|(OR))(\\s*\\S*)*\\#(\\s*\\S*)*\\}", "");
        System.out.println(str);
    }
}
