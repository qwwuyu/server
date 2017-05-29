package com.qwwuyu.server.service;

import java.util.List;
import java.util.Map;

import com.qwwuyu.server.bean.Note;

public interface INoteService {
	int insert(Note note);

	Note selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Note note);

	List<Note> selectByNote(Note note, int limit, int offset, String asc, String desc);

	int deleteByPrimaryKey(Integer id);

	Map<String, Object> getNote(int page);
}
