package com.qwwuyu.server.dao;

import com.qwwuyu.server.bean.Note;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NoteMapper {
    Note selectByPrimaryKey(Integer id);

    int insert(Note record);

    int updateByPrimaryKeySelective(Note record);

    int deleteByPrimaryKey(Integer id);

    List<Note> selectByNote(@Param("note") Note note, @Param("limit") int limit, @Param("offset") int offset, @Param("asc") String asc, @Param("desc") String desc);
}