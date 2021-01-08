package com.qwwuyu.gs.mapper

import com.qwwuyu.gs.entity.Flag
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface FlagMapper {
    fun selectByPrimaryKey(id: Int): Flag?

    fun insert(record: Flag): Int

    fun updateByPrimaryKeySelective(record: Flag): Int

    fun deleteByPrimaryKey(id: Int): Int

    fun selectByFlag(@Param("flag") flag: Flag, @Param("limit") limit: Int, @Param("offset") offset: Int,
                     @Param("asc") asc: String?, @Param("desc") desc: String?): List<Flag>
}