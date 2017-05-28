package com.qwwuyu.server.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.qwwuyu.server.bean.Note;
import com.qwwuyu.server.dao.CommMapper;
import com.qwwuyu.server.dao.NoteMapper;
import com.qwwuyu.server.service.INoteService;

@Service("note")
public class INoteServiceImpl implements INoteService {
	private String table = "note";
	private int numOfPage = 10;
	@Resource
	private CommMapper commMapper;
	@Resource
	private NoteMapper mapper;

	@Override
	public int insert(Note note) {
		return mapper.insert(note);
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

	@Override
	public String getNote(int page) {
		Map<String, Object> map = new HashMap<>();
		int count = commMapper.selectCountByTable(table);
		List<Note> notes = mapper.selectByNote(new Note(), numOfPage, (page - 1) * numOfPage, null, null);
		map.put("page", (count + numOfPage - 1) / numOfPage);
		map.put("notes", notes);
		return JSON.toJSONString(map);
	}
}