package com.qwwuyu.server.service;

import com.qwwuyu.server.bean.User;

public interface IUserService {
    void insert(User user);

    int updateByPrimaryKeySelective(User record);

    User selectByUser(User user);
}
