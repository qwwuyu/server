package com.qwwuyu.server.service;

import java.util.List;
import java.util.Map;

import com.qwwuyu.server.bean.Flag;

public interface IFlagService {
	int insert(Flag flag);

	Flag selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Flag flag);

	int deleteByPrimaryKey(Integer id);

	List<Flag> selectByFlag(Flag flag, int limit, int offset, String asc, String desc);

	Map<String, Object> getFlag(int page);
}
