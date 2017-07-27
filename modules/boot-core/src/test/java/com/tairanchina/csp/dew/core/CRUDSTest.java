package com.tairanchina.csp.dew.core;

import com.ecfront.dew.common.$;
import com.ecfront.dew.common.Page;
import com.ecfront.dew.common.Resp;
import com.tairanchina.csp.dew.core.dao.TestInterfaceDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootApplication
@SpringBootTest(classes = DewBootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ComponentScan(basePackageClasses = {Dew.class, CRUDSTest.class})
public class CRUDSTest {

    private static final String url = "http://127.0.0.1:8080/crud";

    private static long pageNumber = 1;
    private static long pageSize = 10;

    @Autowired
    TestInterfaceDao dao;
    @Test
    public void testAll() throws Exception {
        initialize();
        testCRUD();
//        testCRUDS();
        testInterface();
    }

//    @Test
    public void testInterface() throws Exception {
        CRUDSTestEntity model = new CRUDSTestEntity();
        model.setFieldA("测试A");
        Page<CRUDSTestEntity> page=dao.getYouName(model,1L,10);
        Assert.assertTrue(page != null);

    }

    private void testCRUD() throws Exception {
        // findAll
        Resp<List<CRUDSTestEntity>> entitiesResp = Resp.genericList($.http.get(url + "/"), CRUDSTestEntity.class);
        long recordTotal = entitiesResp.getBody().size();
        // paging
        Resp<Page<CRUDSTestEntity>> entitiesPageResp = Resp.genericPage($.http.get(url + String.format("/%d/%d/", pageNumber, pageSize)), CRUDSTestEntity.class);
        Assert.assertEquals(pageNumber, entitiesPageResp.getBody().getPageNumber());
        Assert.assertEquals(pageSize, entitiesPageResp.getBody().getPageSize());
        Assert.assertEquals(recordTotal, entitiesPageResp.getBody().getRecordTotal());
        long pageTotal = entitiesPageResp.getBody().getPageTotal();
        // save
        CRUDSTestEntity entity = new CRUDSTestEntity();
        entity.setFieldA("测试A");
        entity.setFieldB("测试B");
        Resp<CRUDSTestEntity> entityResp = Resp.generic($.http.post(url + "/", entity), CRUDSTestEntity.class);
        Assert.assertEquals(entity.getFieldA(), entityResp.getBody().getFieldA());
        Assert.assertEquals(entity.getFieldB(), entityResp.getBody().getFieldB());
        Assert.assertEquals(entityResp.getBody().getCreateTime().getTime(), entityResp.getBody().getUpdateTime().getTime());
        Assert.assertTrue(entityResp.getBody().getEnabled());
//        long id = entityResp.getBody().getId();
//        String code = entityResp.getBody().getCode();
//        // getById
//        entityResp = Resp.generic($.http.get(url + "/" + id), CRUDSTestEntity.class);
//        Assert.assertEquals(entity.getFieldA(), entityResp.getBody().getFieldA());
//        Assert.assertEquals(entity.getFieldB(), entityResp.getBody().getFieldB());
//        Assert.assertEquals(entityResp.getBody().getCreateTime().getTime(), entityResp.getBody().getUpdateTime().getTime());
//        Assert.assertTrue(entityResp.getBody().getEnabled());
//        Assert.assertEquals(id, entityResp.getBody().getId());
//        Assert.assertEquals(code, entityResp.getBody().getCode());
//        // getByCode
//        entityResp = Resp.generic($.http.get(url + "/code/" + code), CRUDSTestEntity.class);
//        Assert.assertEquals(entity.getFieldA(), entityResp.getBody().getFieldA());
//        Assert.assertEquals(entity.getFieldB(), entityResp.getBody().getFieldB());
//        Assert.assertEquals(entityResp.getBody().getCreateTime().getTime(), entityResp.getBody().getUpdateTime().getTime());
//        Assert.assertTrue(entityResp.getBody().getEnabled());
//        Assert.assertEquals(id, entityResp.getBody().getId());
//        Assert.assertEquals(code, entityResp.getBody().getCode());
//        // updateById
//        entity.setFieldA("测试A->C");
//        entityResp = Resp.generic($.http.put(url + "/" + id, entity), CRUDSTestEntity.class);
//        Assert.assertEquals(entity.getFieldA(), entityResp.getBody().getFieldA());
//        Assert.assertEquals(entity.getFieldB(), entityResp.getBody().getFieldB());
//        Assert.assertNotEquals(entityResp.getBody().getCreateTime().getTime(), entityResp.getBody().getUpdateTime().getTime());
//        Assert.assertTrue(entityResp.getBody().getEnabled());
//        Assert.assertEquals(id, entityResp.getBody().getId());
//        Assert.assertEquals(code, entityResp.getBody().getCode());
//        // updateByCode
//        entity.setFieldB("测试B->D");
//        entityResp = Resp.generic($.http.put(url + "/code/" + code, entity), CRUDSTestEntity.class);
//        Assert.assertEquals(entity.getFieldA(), entityResp.getBody().getFieldA());
//        Assert.assertEquals(entity.getFieldB(), entityResp.getBody().getFieldB());
//        Assert.assertNotEquals(entityResp.getBody().getCreateTime().getTime(), entityResp.getBody().getUpdateTime().getTime());
//        Assert.assertTrue(entityResp.getBody().getEnabled());
//        Assert.assertEquals(id, entityResp.getBody().getId());
//        Assert.assertEquals(code, entityResp.getBody().getCode());
//        // getById
//        entityResp = Resp.generic($.http.get(url + "/" + id), CRUDSTestEntity.class);
//        Assert.assertEquals(entity.getFieldA(), entityResp.getBody().getFieldA());
//        Assert.assertEquals(entity.getFieldB(), entityResp.getBody().getFieldB());
//        Assert.assertNotEquals(entityResp.getBody().getCreateTime().getTime(), entityResp.getBody().getUpdateTime().getTime());
//        Assert.assertTrue(entityResp.getBody().getEnabled());
//        Assert.assertEquals(id, entityResp.getBody().getId());
//        Assert.assertEquals(code, entityResp.getBody().getCode());
//        // getByCode
//        entityResp = Resp.generic($.http.get(url + "/code/" + code), CRUDSTestEntity.class);
//        Assert.assertEquals(entity.getFieldA(), entityResp.getBody().getFieldA());
//        Assert.assertEquals(entity.getFieldB(), entityResp.getBody().getFieldB());
//        Assert.assertNotEquals(entityResp.getBody().getCreateTime().getTime(), entityResp.getBody().getUpdateTime().getTime());
//        Assert.assertTrue(entityResp.getBody().getEnabled());
//        Assert.assertEquals(id, entityResp.getBody().getId());
//        Assert.assertEquals(code, entityResp.getBody().getCode());
//        // findAll
//        entitiesResp = Resp.genericList($.http.get(url + "/"), CRUDSTestEntity.class);
//        Assert.assertEquals(recordTotal + 1, entitiesResp.getBody().size());
//        // paging
//        entitiesPageResp = Resp.genericPage($.http.get(url + String.format("/%d/%d/", pageNumber, pageSize)), CRUDSTestEntity.class);
//        Assert.assertEquals(pageNumber, entitiesPageResp.getBody().getPageNumber());
//        Assert.assertEquals(pageSize, entitiesPageResp.getBody().getPageSize());
//        Assert.assertEquals(pageTotal * pageSize < recordTotal ? pageTotal : pageTotal + 1, entitiesPageResp.getBody().getPageTotal());
//        Assert.assertEquals(recordTotal + 1, entitiesPageResp.getBody().getRecordTotal());
//        // deleteById
//        Resp deleteResp = Resp.generic($.http.delete(url + "/" + id), Void.class);
//        Assert.assertTrue(deleteResp.ok());
//        // findAll
//        entitiesResp = Resp.genericList($.http.get(url + "/"), CRUDSTestEntity.class);
//        Assert.assertEquals(recordTotal, entitiesResp.getBody().size());
//        // paging
//        entitiesPageResp = Resp.genericPage($.http.get(url + String.format("/%d/%d/", pageNumber, pageSize)), CRUDSTestEntity.class);
//        Assert.assertEquals(pageNumber, entitiesPageResp.getBody().getPageNumber());
//        Assert.assertEquals(pageSize, entitiesPageResp.getBody().getPageSize());
//        Assert.assertEquals(pageTotal, entitiesPageResp.getBody().getPageTotal());
//        Assert.assertEquals(recordTotal, entitiesPageResp.getBody().getRecordTotal());
//        // save
//        entity = new CRUDSTestEntity();
//        entity.setFieldA("测试A");
//        entity.setFieldB("测试B");
//        entityResp = Resp.generic($.http.post(url + "/", entity), CRUDSTestEntity.class);
//        id = entityResp.getBody().getId();
//        code = entityResp.getBody().getCode();
//        // deleteByCode
//        deleteResp = Resp.generic($.http.delete(url + "/code/" + code), Void.class);
//        Assert.assertTrue(deleteResp.ok());
//        // findAll
//        entitiesResp = Resp.genericList($.http.get(url + "/"), CRUDSTestEntity.class);
//        Assert.assertEquals(recordTotal, entitiesResp.getBody().size());
    }

    private void testCRUDS() throws Exception {
        // save
        CRUDSTestEntity entity = new CRUDSTestEntity();
        entity.setFieldA("测试A");
        entity.setFieldB("测试B");
        entity.setEnabled(true);
        Resp<CRUDSTestEntity> entityResp = Resp.generic($.http.post(url + "/", entity), CRUDSTestEntity.class);
        long oneId = entityResp.getBody().getId();
        String oneCode = entityResp.getBody().getCode();
        // save
        CRUDSTestEntity otherEntity = new CRUDSTestEntity();
        otherEntity.setFieldA("测试C");
        otherEntity.setFieldB("测试D");
        otherEntity.setEnabled(false);
        entityResp = Resp.generic($.http.post(url + "/", otherEntity), CRUDSTestEntity.class);
        long otherId = entityResp.getBody().getId();
        String otherCode = entityResp.getBody().getCode();
        // getById
        entityResp = Resp.generic($.http.get(url + "/" + oneId), CRUDSTestEntity.class);
        Assert.assertTrue(entityResp.getBody().getEnabled());
        entityResp = Resp.generic($.http.get(url + "/" + otherId), CRUDSTestEntity.class);
        Assert.assertFalse(entityResp.getBody().getEnabled());
        // disabledById
        Resp statusResp = Resp.generic($.http.delete(url + "/" + oneId + "/disable"), Void.class);
        Assert.assertTrue(statusResp.ok());
        // enabledById
        statusResp = Resp.generic($.http.put(url + "/" + otherId + "/enable", ""), Void.class);
        Assert.assertTrue(statusResp.ok());
        // getById
        entityResp = Resp.generic($.http.get(url + "/" + oneId), CRUDSTestEntity.class);
        Assert.assertFalse(entityResp.getBody().getEnabled());
        entityResp = Resp.generic($.http.get(url + "/" + otherId), CRUDSTestEntity.class);
        Assert.assertTrue(entityResp.getBody().getEnabled());
        // enabledByCode
        statusResp = Resp.generic($.http.put(url + "/code/" + oneCode + "/enable", ""), Void.class);
        Assert.assertTrue(statusResp.ok());
        // disableByCode
        statusResp = Resp.generic($.http.delete(url + "/code/" + otherCode + "/disable"), Void.class);
        Assert.assertTrue(statusResp.ok());
        // getById
        entityResp = Resp.generic($.http.get(url + "/" + oneId), CRUDSTestEntity.class);
        Assert.assertTrue(entityResp.getBody().getEnabled());
        entityResp = Resp.generic($.http.get(url + "/" + otherId), CRUDSTestEntity.class);
        Assert.assertFalse(entityResp.getBody().getEnabled());
        // findByStatus status=true
        Resp<List<CRUDSTestEntity>> entitiesResp = Resp.genericList($.http.get(url + "/?enabled=true"), CRUDSTestEntity.class);
        Assert.assertEquals(1, entitiesResp.getBody().size());
        Assert.assertTrue(entitiesResp.getBody().get(0).getEnabled());
        Assert.assertEquals(entity.getFieldA(), entitiesResp.getBody().get(0).getFieldA());
        // findByStatus status=false
        entitiesResp = Resp.genericList($.http.get(url + "/?enabled=false"), CRUDSTestEntity.class);
        Assert.assertEquals(1, entitiesResp.getBody().size());
        Assert.assertFalse(entitiesResp.getBody().get(0).getEnabled());
        Assert.assertEquals(otherEntity.getFieldA(), entitiesResp.getBody().get(0).getFieldA());
        // pagingByStatus status=true
        Resp<Page<CRUDSTestEntity>> pageEntitiesResp = Resp.genericPage($.http.get(url + "/1/10/?enabled=true"), CRUDSTestEntity.class);
        Assert.assertEquals(pageNumber, pageEntitiesResp.getBody().getPageNumber());
        Assert.assertEquals(pageSize, pageEntitiesResp.getBody().getPageSize());
        Assert.assertEquals(1, pageEntitiesResp.getBody().getPageTotal());
        Assert.assertEquals(1, pageEntitiesResp.getBody().getRecordTotal());
        Assert.assertEquals(entity.getFieldA(), pageEntitiesResp.getBody().getObjects().get(0).getFieldA());
        // pagingByStatus status=false
        pageEntitiesResp = Resp.genericPage($.http.get(url + "/1/10/?enabled=false"), CRUDSTestEntity.class);
        Assert.assertEquals(pageNumber, pageEntitiesResp.getBody().getPageNumber());
        Assert.assertEquals(pageSize, pageEntitiesResp.getBody().getPageSize());
        Assert.assertEquals(1, pageEntitiesResp.getBody().getPageTotal());
        Assert.assertEquals(1, pageEntitiesResp.getBody().getRecordTotal());
        Assert.assertEquals(otherEntity.getFieldA(), pageEntitiesResp.getBody().getObjects().get(0).getFieldA());
    }

    private void initialize() throws Exception {
        // ddl
        // Dew.ds.jdbc().execute("DROP TABLE t_test_crud_s_entity");
        Dew.ds().jdbc().execute("CREATE TABLE IF NOT EXISTS t_test_crud_s_entity\n" +
                "(\n" +
                "id int primary key auto_increment,\n" +
                "code varchar(32),\n" +
                "field_a varchar(255),\n" +
                "field_c varchar(255) not null,\n" +
                "create_user varchar(32) not null,\n" +
                "create_time datetime,\n" +
                "update_user varchar(32) not null,\n" +
                "update_time datetime,\n" +
                "enabled bool\n" +
                ")");
        Dew.ds().jdbc().execute("insert INTO t_test_crud_s_entity(code,field_a,field_c,create_user,create_time,update_user,update_time,enabled) " +
                "values('aa','测试A','测试B','jiaj','2017-07-08','j','2017-07-08',TRUE)");
    }
}
