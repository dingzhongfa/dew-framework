package com.tairanchina.csp.dew.core;

import com.ecfront.dew.common.$;
import com.ecfront.dew.common.Page;
import com.ecfront.dew.common.Resp;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    @Test
    public void testAll() throws Exception {
        initialize();
        testCRU();
        testCRUD();
        testCRUDS();
    }

    private void testCRU() throws Exception {
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
        long id = entityResp.getBody().getId();
        String code = entityResp.getBody().getCode();
        // getById
        entityResp = Resp.generic($.http.get(url + "/" + id), CRUDSTestEntity.class);
        Assert.assertEquals(entity.getFieldA(), entityResp.getBody().getFieldA());
        Assert.assertEquals(entity.getFieldB(), entityResp.getBody().getFieldB());
        // getByCode
        entityResp = Resp.generic($.http.get(url + "/code/" + code), CRUDSTestEntity.class);
        Assert.assertEquals(entity.getFieldA(), entityResp.getBody().getFieldA());
        Assert.assertEquals(entity.getFieldB(), entityResp.getBody().getFieldB());
        // updateById
        entity.setFieldA("测试A->C");
        entityResp = Resp.generic($.http.put(url + "/" + id, entity), CRUDSTestEntity.class);
        Assert.assertEquals(entity.getFieldA(), entityResp.getBody().getFieldA());
        Assert.assertEquals(entity.getFieldB(), entityResp.getBody().getFieldB());
        Assert.assertEquals(id, entityResp.getBody().getId());
        Assert.assertEquals(code, entityResp.getBody().getCode());
        // updateByCode
        entity.setFieldB("测试B->D");
        System.out.println(code);
        entityResp = Resp.generic($.http.put(url + "/code/" + code, entity), CRUDSTestEntity.class);
        System.out.println($.json.toJsonString(entityResp));
        Assert.assertEquals(entity.getFieldA(), entityResp.getBody().getFieldA());
        Assert.assertEquals(entity.getFieldB(), entityResp.getBody().getFieldB());
        Assert.assertEquals(id, entityResp.getBody().getId());
        Assert.assertEquals(code, entityResp.getBody().getCode());
        // getById
        entityResp = Resp.generic($.http.get(url + "/" + id), CRUDSTestEntity.class);
        Assert.assertEquals(entity.getFieldA(), entityResp.getBody().getFieldA());
        Assert.assertEquals(entity.getFieldB(), entityResp.getBody().getFieldB());
        // getByCode
        entityResp = Resp.generic($.http.get(url + "/code/" + code), CRUDSTestEntity.class);
        Assert.assertEquals(entity.getFieldA(), entityResp.getBody().getFieldA());
        Assert.assertEquals(entity.getFieldB(), entityResp.getBody().getFieldB());
        // findAll
        entitiesResp = Resp.genericList($.http.get(url + "/"), CRUDSTestEntity.class);
        Assert.assertEquals(recordTotal + 1, entitiesResp.getBody().size());
        // paging
        entitiesPageResp = Resp.genericPage($.http.get(url + String.format("/%d/%d/", pageNumber, pageSize)), CRUDSTestEntity.class);
        Assert.assertEquals(pageNumber, entitiesPageResp.getBody().getPageNumber());
        Assert.assertEquals(pageSize, entitiesPageResp.getBody().getPageSize());
        Assert.assertEquals(pageTotal * pageSize < recordTotal ? pageTotal : pageTotal + 1, entitiesPageResp.getBody().getPageTotal());
        Assert.assertEquals(recordTotal + 1, entitiesPageResp.getBody().getRecordTotal());
    }

    private void testCRUD() throws Exception {
//        // =========== Full Test
//        FullEntity fullEntity = new FullEntity();
//        fullEntity.setFieldA("测试A");
//        // insert
//        try {
//            Dew.ds.insert(fullEntity);
//            Assert.assertTrue(false);
//        } catch (Throwable e) {
//            Assert.assertTrue(true);
//        }
//        fullEntity.setFieldB("测试B");
//        id = Dew.ds.insert(fullEntity);
//        // getById
//        fullEntity = Dew.ds.getById(id, FullEntity.class);
//        Assert.assertTrue(!fullEntity.getCode().isEmpty());
//        Assert.assertEquals("测试A", fullEntity.getFieldA());
//        Assert.assertEquals("测试B", fullEntity.getFieldB());
//        // getByCode
//        fullEntity = Dew.ds.getByCode(fullEntity.getCode(), FullEntity.class);
//        Assert.assertEquals("", fullEntity.getCreateUser());
//        Assert.assertEquals("", fullEntity.getUpdateUser());
//        Assert.assertTrue(fullEntity.getCreateTime() != null);
//        Assert.assertEquals(fullEntity.getCreateTime(), fullEntity.getUpdateTime());
//        // updateById
//        fullEntity.setFieldA("测试C");
//        Dew.ds.updateById(id, fullEntity);
//        Assert.assertEquals("测试C", Dew.ds.getById(id, FullEntity.class).getFieldA());
//        // updateByCode
//        fullEntity.setFieldA(null);
//        fullEntity.setFieldB("测试D");
//        // null不更新
//        Thread.sleep(1000);
//        Dew.ds.updateByCode(fullEntity.getCode(), fullEntity);
//        fullEntity = Dew.ds.getById(id, FullEntity.class);
//        Assert.assertEquals("测试C", fullEntity.getFieldA());
//        Assert.assertEquals("测试D", fullEntity.getFieldB());
//        Assert.assertNotEquals(fullEntity.getCreateTime(), fullEntity.getUpdateTime());
//        Assert.assertEquals(true, fullEntity.getEnabled());
//        // disableById
//        Dew.ds.disableById(fullEntity.getId(), FullEntity.class);
//        Assert.assertEquals(false, Dew.ds.getById(fullEntity.getId(), FullEntity.class).getEnabled());
//        // enableById
//        Dew.ds.enableById(fullEntity.getId(), FullEntity.class);
//        Assert.assertEquals(true, Dew.ds.getById(fullEntity.getId(), FullEntity.class).getEnabled());
//        // disableByCode
//        Dew.ds.disableByCode(fullEntity.getCode(), FullEntity.class);
//        Assert.assertEquals(false, Dew.ds.getById(fullEntity.getId(), FullEntity.class).getEnabled());
//        // enableByCode
//        Dew.ds.enableByCode(fullEntity.getCode(), FullEntity.class);
//        Assert.assertEquals(true, Dew.ds.getById(fullEntity.getId(), FullEntity.class).getEnabled());
//        // existById
//        Assert.assertEquals(true, Dew.ds.existById(fullEntity.getId(), FullEntity.class));
//        Assert.assertEquals(false, Dew.ds.existById(11111, FullEntity.class));
//        // existByCode
//        Assert.assertEquals(true, Dew.ds.existByCode(fullEntity.getCode(), FullEntity.class));
//        Assert.assertEquals(false, Dew.ds.existByCode("11111", FullEntity.class));
//        // findAll
//        Assert.assertEquals(1, Dew.ds.findAll(FullEntity.class).size());
//        Assert.assertEquals("测试C", Dew.ds.findAll(FullEntity.class).get(0).getFieldA());
//        // findEnabled
//        Assert.assertEquals(1, Dew.ds.findEnabled(FullEntity.class).size());
//        // findDisabled
//        Assert.assertEquals(0, Dew.ds.findDisabled(FullEntity.class).size());
//        // countAll
//        Assert.assertEquals(1, Dew.ds.countAll(FullEntity.class));
//        // countEnabled
//        Assert.assertEquals(1, Dew.ds.countEnabled(FullEntity.class));
//        // countDisabled
//        Assert.assertEquals(0, Dew.ds.countDisabled(FullEntity.class));
//        // insert
//        FullEntity fullEntity2 = new FullEntity();
//        fullEntity2.setFieldA("测试A2");
//        fullEntity2.setFieldB("测试B2");
//        FullEntity fullEntity3 = new FullEntity();
//        fullEntity3.setFieldA("测试A3");
//        fullEntity3.setFieldB("测试B3");
//        Dew.ds.insert(new ArrayList<FullEntity>() {{
//            add(fullEntity2);
//            add(fullEntity3);
//        }});
//        Assert.assertEquals(3, Dew.ds.countAll(FullEntity.class));
//        // paging
//        Page<FullEntity> fullEntities = Dew.ds.paging(1, 2, FullEntity.class);
//        Assert.assertEquals(3, fullEntities.getRecordTotal());
//        Assert.assertEquals(2, fullEntities.getPageSize());
//        Assert.assertEquals(1, fullEntities.getPageNumber());
//        Assert.assertEquals(2, fullEntities.getPageTotal());
//        Assert.assertEquals(2, fullEntities.getObjects().size());
//        // pagingEnabled
//        Dew.ds.disableById(fullEntity.getId(), FullEntity.class);
//        fullEntities = Dew.ds.pagingEnabled(1, 2, FullEntity.class);
//        Assert.assertEquals(2, fullEntities.getRecordTotal());
//        // pagingDisabled
//        fullEntities = Dew.ds.pagingDisabled(1, 2, FullEntity.class);
//        Assert.assertEquals(1, fullEntities.getRecordTotal());
//        // deleteById
//        Dew.ds.deleteById(fullEntity.getId(), FullEntity.class);
//        // deleteByCode
//        Dew.ds.deleteByCode(Dew.ds.findAll(FullEntity.class).get(0).getCode(), FullEntity.class);
//        Assert.assertEquals(1, Dew.ds.findAll(FullEntity.class).size());
    }

    private void testCRUDS() throws Exception {

    }

    private void initialize() throws Exception {
        // ddl
        // Dew.ds.jdbc().execute("DROP TABLE t_test_crud_s_entity");
        Dew.ds.jdbc().execute("CREATE TABLE IF NOT EXISTS t_test_crud_s_entity\n" +
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
    }
}
