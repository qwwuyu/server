package com.qwwuyu.gs.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface CommMapper {
    fun selectCountByTable(@Param("table") table: String): Int
}