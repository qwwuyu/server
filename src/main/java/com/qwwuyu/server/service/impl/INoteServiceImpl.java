package com.qwwuyu.server.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qwwuyu.server.bean.Note;
import com.qwwuyu.server.dao.NoteMapper;
import com.qwwuyu.server.service.INoteService;

@Service("note")
public class INoteServiceImpl implements INoteService {
	@Resource
	private NoteMapper mapper;

	@Override
	public void insert(Note note) {
		mapper.insert(note);
	}

	@Override
	public Note selectByPrimaryKey(Integer id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Note record) {
		return mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<Note> selectByNote(Note note, int limit, int offset, String asc, String desc) {
		return mapper.selectByNote(note, limit, offset, asc, desc);
	}

}