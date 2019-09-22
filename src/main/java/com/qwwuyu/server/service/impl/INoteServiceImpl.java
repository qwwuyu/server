package com.qwwuyu.server.service.impl;

import com.qwwuyu.server.bean.Note;
import com.qwwuyu.server.dao.CommMapper;
import com.qwwuyu.server.dao.NoteMapper;
import com.qwwuyu.server.service.INoteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<String, Object> getNote(int page) {
        Map<String, Object> map = new HashMap<>();
        int count = commMapper.selectCountByTable(table);
        List<Note> datas = mapper.selectByNote(new Note(), numOfPage, (page - 1) * numOfPage, null, "time");
        map.put("page", (count + numOfPage - 1) / numOfPage);
        map.put("count", count);
        map.put("select", page);
        map.put("datas", datas);
        map.put("sysTime", System.currentTimeMillis());
        return map;
    }
}