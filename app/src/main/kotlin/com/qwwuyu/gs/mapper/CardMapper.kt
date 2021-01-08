package com.qwwuyu.gs.mapper

import com.qwwuyu.gs.entity.Card
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface CardMapper {
    fun selectByPrimaryKey(id: Int): Card?

    fun insert(record: Card): Int

    fun updateByPrimaryKeySelective(record: Card): Int

    fun deleteByPrimaryKey(id: Int): Int

    fun selectByCard(@Param("card") card: Card, @Param("limit") limit: Int, @Param("offset") offset: Int,
                     @Param("asc") asc: String?, @Param("desc") desc: String?): List<Card>
}