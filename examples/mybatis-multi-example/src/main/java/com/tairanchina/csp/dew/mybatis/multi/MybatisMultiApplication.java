package com.tairanchina.csp.dew.mybatis.multi;


import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.core.autoconfigure.DewBootApplication;
import com.tairanchina.csp.dew.jdbc.mybatis.annotion.DewMapperScan;
import org.springframework.boot.builder.SpringApplicationBuilder;


/**
 * desription:
 * Created by ding on 2017/12/28.
 */
@DewBootApplication(scanBasePackageClasses = {Dew.class, MybatisMultiApplication.class})
@DewMapperScan(basePackages = "com.tairanchina.csp.dew.mybatis.multi.mapper")
public class MybatisMultiApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MybatisMultiApplication.class).web(false).run(args);
    }


}
