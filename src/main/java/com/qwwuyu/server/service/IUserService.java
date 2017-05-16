package com.qwwuyu.server.service;

import com.qwwuyu.server.bean.User;

public interface IUserService {
	User selectByPrimaryKey(int id);

	void insert(User user);

	int updateByPrimaryKeySelective(User record);

	User selectByName(String name);

	User selectByNick(String nick);
}
