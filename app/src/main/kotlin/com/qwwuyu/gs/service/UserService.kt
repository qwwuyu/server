package com.qwwuyu.gs.service

import com.qwwuyu.gs.entity.User
import com.qwwuyu.gs.mapper.UserMapper
import org.springframework.stereotype.Service
import javax.annotation.Resource

@Service("user")
class UserService {
    @Resource
    private lateinit var userMapper: UserMapper

    fun insert(user: User) {
        userMapper.insert(user)
    }

    fun updateByPrimaryKeySelective(record: User): Int {
        return userMapper.updateByPrimaryKeySelective(record)
    }

    fun selectByUser(user: User): User? {
        return userMapper.selectByUser(user)
    }
}