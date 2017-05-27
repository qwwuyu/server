package com.qwwuyu.server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qwwuyu.server.bean.Note;

public interface NoteMapper {
	Note selectByPrimaryKey(Integer id);

	int insert(Note record);

	int updateByPrimaryKeySelective(Note record);

	int deleteByPrimaryKey(Integer id);

	List<Note> selectByNote(@Param("note") Note note, @Param("limit") int limit, @Param("offset") int offset,
			@Param("asc") String asc, @Param("desc") String desc);
}