package com.qwwuyu.test;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.qwwuyu.server.bean.Note;
import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.service.INoteService;
import com.qwwuyu.server.service.IUserService;
import com.qwwuyu.server.utils.J2EEUtil;

//表示继承了SpringJUnit4ClassRunner类
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class MyBatisTest {
	private static Logger logger = Logger.getLogger(MyBatisTest.class);
	@Resource
	private IUserService userService = null;
	@Resource
	private INoteService noteService = null;

	@Test
	public void test1() {
		long time = System.currentTimeMillis();
		User user = userService.selectByUser(new User().setName("qwwuyu"));
		logger.info(J2EEUtil.parseToken(user.getToken()));
		System.out.println(System.currentTimeMillis() - time);
	}

	@Test
	public void test2() {
		List<Note> note = noteService.selectByNote(new Note().setUserId(1), 1, 1, null, "time");
		logger.info(JSON.toJSON(note));
	}
}
