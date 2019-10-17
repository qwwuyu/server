package com.qwwuyu.test;

import com.qwwuyu.server.service.INoteService;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

//表示继承了SpringJUnit4ClassRunner类
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class MyBatisTest {
    private static Logger logger = Logger.getLogger(MyBatisTest.class);
    @Resource
    private INoteService noteService;

    @Test
    public void test1() {
    }
}
