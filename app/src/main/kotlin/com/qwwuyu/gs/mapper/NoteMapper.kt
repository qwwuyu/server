package com.qwwuyu.gs.mapper

import com.qwwuyu.gs.entity.Note
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface NoteMapper {
    fun selectByPrimaryKey(id: Int): Note?

    fun insert(record: Note): Int

    fun updateByPrimaryKeySelective(record: Note): Int

    fun deleteByPrimaryKey(id: Int): Int

    fun selectByNote(@Param("note") note: Note, @Param("limit") limit: Int, @Param("offset") offset: Int,
                     @Param("asc") asc: String?, @Param("desc") desc: String?): List<Note>
}