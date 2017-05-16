package com.qwwuyu.server.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.dao.UserMapper;
import com.qwwuyu.server.service.IUserService;

@Service("user")
public class IUserServiceImpl implements IUserService {
	@Resource
	private UserMapper userMapper;

	@Override
	public User selectByPrimaryKey(int id) {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public void insert(User user) {
		userMapper.insert(user);
	}

	@Override
	public int updateByPrimaryKeySelective(User record) {
		return userMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public User selectByName(String name) {
		return userMapper.selectByName(name);
	}

	@Override
	public User selectByNick(String nick) {
		return userMapper.selectByNick(nick);
	}
}