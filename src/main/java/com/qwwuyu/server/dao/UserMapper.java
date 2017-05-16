package com.qwwuyu.server.dao;

import com.qwwuyu.server.bean.User;

public interface UserMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);

	User selectByName(String name);

	User selectByNick(String nick);
}