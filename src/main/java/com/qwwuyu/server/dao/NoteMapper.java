package com.qwwuyu.server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qwwuyu.server.bean.Note;

public interface NoteMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(Note record);

	int insertSelective(Note record);

	Note selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Note record);

	int updateByPrimaryKey(Note record);

	List<Note> selectByNote(@Param("note") Note note, @Param("limit") int limit, @Param("offset") int offset,
			@Param("asc") String asc, @Param("desc") String desc);
}