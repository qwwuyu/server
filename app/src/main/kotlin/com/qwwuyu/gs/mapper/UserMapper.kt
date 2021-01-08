package com.qwwuyu.gs.mapper

import com.qwwuyu.gs.entity.User
import org.apache.ibatis.annotations.Mapper

@Mapper
interface UserMapper {
    fun insert(record: User): Int

    fun updateByPrimaryKeySelective(record: User): Int

    fun selectByUser(user: User): User?
}