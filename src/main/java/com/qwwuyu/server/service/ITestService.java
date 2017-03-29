package com.qwwuyu.server.service;

import com.qwwuyu.server.bean.Test;

public interface ITestService {
	public Test getTestById(int id);

	public void setTest(Test test);
}
