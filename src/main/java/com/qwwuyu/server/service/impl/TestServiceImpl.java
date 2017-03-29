package com.qwwuyu.server.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qwwuyu.server.bean.Test;
import com.qwwuyu.server.dao.TestMapper;
import com.qwwuyu.server.service.ITestService;

@Service("test")
public class TestServiceImpl implements ITestService {
	@Resource
	private TestMapper testMapper;

	@Override
	public Test getTestById(int id) {
		return this.testMapper.selectByPrimaryKey(id);
	}

	@Override
	public void setTest(Test test) {
		this.testMapper.updateByPrimaryKey(test);
	}
}