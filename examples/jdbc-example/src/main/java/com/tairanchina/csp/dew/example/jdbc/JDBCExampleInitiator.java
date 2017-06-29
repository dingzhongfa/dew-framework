package com.tairanchina.csp.dew.example.jdbc;


import com.tairanchina.csp.dew.core.Dew;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
public class JDBCExampleInitiator {

    private static final Logger logger = LoggerFactory.getLogger(JDBCExampleInitiator.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TXService txService;

    @PostConstruct
    public void init() {
//        // ddl
//        Dew.ds().jdbc().execute("CREATE TABLE example_entity\n" +
//                "(\n" +
//                "id int primary key auto_increment,\n" +
//                "field_a varchar(255)\n" +
//                ")");
//        txService.insert();
//        logger.info(">>>> " + txService.count());
//        txService.insertWithTX();
//        logger.info(">>>> " + txService.count());
//        try {
//            txService.insertWithTXHasError();
//        }catch(Exception e){
//
//        }
//        logger.info(">>>> " + txService.count());


        // ddl
        Dew.ds("test2").jdbc().execute("CREATE TABLE example_entity\n" +
                "(\n" +
                "id int primary key auto_increment,\n" +
                "field_a varchar(255)\n" +
                ")");
        txService.insertMulti();
        logger.info(">>>> " + txService.countMulti());
        txService.insertWithTXMulti();
        logger.info(">>>> " + txService.countMulti());
        try {
            txService.insertWithTXHasErrorMulti();
        }catch(Exception e){

        }
        logger.info(">>>> " + txService.countMulti());
    }

//    @PostConstruct
//    @Transactional
//    public void init() {
//
//        jdbcTemplate.update("INSERT into example_entity (field_a) VALUE ('TransactionA3')");
//        String aaa = null;
//        if(aaa.equals("")){
//
//        }
//    }
}
