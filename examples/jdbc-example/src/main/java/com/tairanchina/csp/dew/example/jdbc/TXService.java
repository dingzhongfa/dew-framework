package com.tairanchina.csp.dew.example.jdbc;

import com.tairanchina.csp.dew.core.Dew;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class TXService {

    public long insert() {
        ExampleEntity entity = new ExampleEntity();
        entity.setFieldA("测试A");
        return Dew.ds().insert(entity);
    }

    @Transactional
    public long insertWithTX() {
        ExampleEntity entity = new ExampleEntity();
        entity.setFieldA("测试B");
        return Dew.ds().insert(entity);
    }

    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public long insertWithTXHasError() throws Exception {
        ExampleEntity entity = new ExampleEntity();
        entity.setFieldA("测试C");
        long id= Dew.ds().insert(entity);
        throw new Exception("");
    }

    public long count() {
        return Dew.ds().countAll(ExampleEntity.class);
    }

    public long insertMulti() {
        ExampleEntity entity = new ExampleEntity();
        entity.setFieldA("测试A");
        return Dew.ds("test2").insert(entity);
    }

    @Transactional("test2TransactionManager")
    public long insertWithTXMulti() {
        ExampleEntity entity = new ExampleEntity();
        entity.setFieldA("测试B");
        return Dew.ds("test2").insert(entity);
    }

    @Transactional(value = "test2TransactionManager", rollbackFor = {Exception.class, RuntimeException.class})
    public long insertWithTXHasErrorMulti() throws Exception {
        ExampleEntity entity = new ExampleEntity();
        entity.setFieldA("测试C");
        long id= Dew.ds("test2").insert(entity);
        throw new Exception("");
    }

    public long countMulti() {
        return Dew.ds("test2").countAll(ExampleEntity.class);
    }

}
