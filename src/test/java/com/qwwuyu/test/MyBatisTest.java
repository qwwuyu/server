package com.qwwuyu.test;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.qwwuyu.server.service.ITestService;

//表示继承了SpringJUnit4ClassRunner类
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class MyBatisTest {
	private static Logger logger = Logger.getLogger(MyBatisTest.class);
	@Resource
	private ITestService service = null;

	@Test
	public void test1() {
		com.qwwuyu.server.bean.Test test = service.getTestById(1);
		logger.info(JSON.toJSONString(test));
	}
}
