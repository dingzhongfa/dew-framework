package com.tairanchina.csp.dew.example.jdbc;


import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.entity.Column;
import com.tairanchina.csp.dew.core.entity.Entity;
import com.tairanchina.csp.dew.core.entity.PkEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class JDBCExampleInitiator {

    private static final Logger logger= LoggerFactory.getLogger(JDBCExampleInitiator.class);

    @PostConstruct
    public void init() {
        // ddl
        Dew.ds.jdbc().execute("CREATE TABLE example_entity\n" +
                "(\n" +
                "id int primary key auto_increment,\n" +
                "field_a varchar(255)\n" +
                ")");
        // insert
        ExampleEntity entity = new ExampleEntity();
        entity.setFieldA("测试A");
        long id = Dew.ds.insert(entity);
        // get
        logger.info(">>>> "+Dew.ds.getById(id, ExampleEntity.class).getFieldA());

    }

    @Entity
    public static class ExampleEntity extends PkEntity {

        @Column
        private String fieldA;

        public String getFieldA() {
            return fieldA;
        }

        public void setFieldA(String fieldA) {
            this.fieldA = fieldA;
        }

    }

}
