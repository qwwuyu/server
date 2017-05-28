package com.qwwuyu.server.service;

import java.util.List;

import com.qwwuyu.server.bean.Note;

public interface INoteService {
	int insert(Note note);

	Note selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Note note);

	List<Note> selectByNote(Note note, int limit, int offset, String asc, String desc);

	int deleteByPrimaryKey(Integer id);

	String getNote(int page);
}
