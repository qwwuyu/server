package com.qwwuyu.server.service.impl;

import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.dao.UserMapper;
import com.qwwuyu.server.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("user")
public class IUserServiceImpl implements IUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public void insert(User user) {
        userMapper.insert(user);
    }

    @Override
    public int updateByPrimaryKeySelective(User record) {
        return userMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public User selectByUser(User user) {
        return userMapper.selectByUser(user);
    }
}