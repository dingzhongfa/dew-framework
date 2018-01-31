package com.ding.dew.select;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * desription:
 * Created by ding on 2018/1/29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
public class SelectTest {

    @Autowired
    private TestSelect testSelect;

    @Test
    public void testSelect() throws Exception {
        testSelect.testAll();
    }
}
