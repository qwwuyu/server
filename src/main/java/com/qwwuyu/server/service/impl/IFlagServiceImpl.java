package com.qwwuyu.server.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qwwuyu.server.bean.Flag;
import com.qwwuyu.server.dao.CommMapper;
import com.qwwuyu.server.dao.FlagMapper;
import com.qwwuyu.server.service.IFlagService;

@Service("flag")
public class IFlagServiceImpl implements IFlagService {
	private String table = "flag";
	private int numOfPage = 10;
	@Resource
	private CommMapper commMapper;
	@Resource
	private FlagMapper mapper;

	@Override
	public int insert(Flag flag) {
		return mapper.insert(flag);
	}

	@Override
	public Flag selectByPrimaryKey(Integer id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Flag record) {
		return mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<Flag> selectByFlag(Flag flag, int limit, int offset, String asc, String desc) {
		return mapper.selectByFlag(flag, limit, offset, asc, desc);
	}

	@Override
	public Map<String, Object> getFlag(int page) {
		Map<String, Object> map = new HashMap<>();
		int count = commMapper.selectCountByTable(table);
		List<Flag> datas = mapper.selectByFlag(new Flag(), numOfPage, (page - 1) * numOfPage, null, "time");
		map.put("page", (count + numOfPage - 1) / numOfPage);
		map.put("count", count);
		map.put("select", page);
		map.put("datas", datas);
		map.put("sysTime", System.currentTimeMillis());
		return map;
	}
}