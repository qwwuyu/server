package com.qwwuyu.test;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.service.IUserService;

//表示继承了SpringJUnit4ClassRunner类
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class MyBatisTest {
	private static Logger logger = Logger.getLogger(MyBatisTest.class);
	@Resource
	private IUserService service = null;

	@Test
	public void test1() {
		try {
			service.insert(new User(null, "qwwuyu2", "123456", "qwwuyu2", "", 2));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		User user = service.selectByName("qwwuyu2");
		logger.info(JSON.toJSONString(user));
	}
}
