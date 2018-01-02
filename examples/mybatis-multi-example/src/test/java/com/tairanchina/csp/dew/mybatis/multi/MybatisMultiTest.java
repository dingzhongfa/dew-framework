package com.tairanchina.csp.dew.mybatis.multi;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.jdbc.DewDS;
import com.tairanchina.csp.dew.jdbc.DewSB;
import com.tairanchina.csp.dew.mybatis.multi.entity.TOrder;
import com.tairanchina.csp.dew.mybatis.multi.entity.User;
import com.tairanchina.csp.dew.mybatis.multi.service.TOrderService;
import com.tairanchina.csp.dew.mybatis.multi.service.UserService;
import com.tairanchina.csp.dew.mybatis.multi.service.UserService2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * desription:
 * Created by ding on 2017/12/28.
 */
@SpringBootTest(classes = MybatisMultiApplication.class)
@RunWith(SpringRunner.class)
public class MybatisMultiTest {

    private static final Logger logger = LoggerFactory.getLogger(MybatisMultiTest.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserService2 userService2;

    @Autowired
    private TOrderService tOrderService;

    @Before
    public void init() {
        ((DewDS) Dew.ds()).jdbc().execute("CREATE TABLE user\n" +
                "(\n" +
                "test_id int primary key,\n" +
                "name varchar(50),\n" +
                "age INT ,\n" +
                "test_type INT ,\n" +
                "test_date datetime,\n" +
                "role long,\n" +
                "phone varchar(50)\n" +
                ")");
    }

    @Test
    @Transactional
    public void testUser() {
        testUserProcess(userService);
        testUserProcess(userService2);
    }


    public void testUserProcess(ServiceImpl service) {
        User user = new User();
        user.setId(1L);
        user.setName("Tom");
        service.insert(user);
        logger.info("======================");

        User exampleUser = (User) service.selectById(user.getId());
        exampleUser.setAge(18);
        service.updateById(exampleUser);

        List<User> userList = service.selectList(
                new EntityWrapper<User>().eq("name", "Tom")
        );
        logger.info("========userList=========size====={}", userList.size());

        Page<User> userListTemp = service.selectPage(
                new Page<User>(1, 2),
                new EntityWrapper<User>().eq("name", "Tom")
        );
        logger.info("========userList=========size====={}", userListTemp.getRecords().size());

        userList = service.selectList(
                new EntityWrapper<User>().eq("age", "19")
        );
        logger.info("========userList===age===19===size====={}", userList.size());

        service.delete(new EntityWrapper<User>().eq("age", "19"));

        userList = service.selectList(
                new EntityWrapper<User>().eq("age", "19")
        );
        logger.info("========userList=========size====={}", userList.size());

        List<String> ages =null;
        if (service instanceof UserService) {
            ages = ((UserService) service).ageGroup();
        }
        if (service instanceof UserService2){
            ages =((UserService2) service).ageGroup();
        }
        logger.info("========userList=========ages====={}", ages);
    }

    @Test
    public void testSharding() {
        long cout = tOrderService.selectCount(new EntityWrapper<TOrder>());
        long countStart = Dew.ds("sharding").countAll(TOrder.class);
        TOrder tOrder = new TOrder();
        tOrder.setUserId(13).setStatus("test");
        for (int i = 1110; i < 1120; i++) {
            tOrder.setOrderId(i);
            Dew.ds("sharding").insert(tOrder);
        }
        tOrder.setUserId(12).setStatus("test");
        for (int i = 1010; i < 1020; i++) {
            tOrder.setOrderId(i);
            Dew.ds("sharding").insert(tOrder);
        }
        Assert.assertTrue((Dew.ds("sharding").countAll(TOrder.class) - countStart) == 20);
        List<TOrder> tOrderList = Dew.ds("sharding").find(DewSB.inst().eq("status", "test"), TOrder.class);
        Assert.assertEquals(20, tOrderList.size());
        Dew.ds("sharding").delete(DewSB.inst().eq("userId", 12), TOrder.class);
        Dew.ds("sharding").delete(DewSB.inst().eq("userId", 13), TOrder.class);
        Assert.assertEquals(20, Dew.ds("sharding").countAll(TOrder.class));
    }
}
