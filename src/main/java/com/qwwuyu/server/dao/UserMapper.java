package com.qwwuyu.server.dao;

import com.qwwuyu.server.bean.User;

public interface UserMapper {
    int insert(User record);

    int updateByPrimaryKeySelective(User record);

    User selectByUser(User user);
}