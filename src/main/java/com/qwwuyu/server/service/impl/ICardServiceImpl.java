package com.qwwuyu.server.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qwwuyu.server.bean.Card;
import com.qwwuyu.server.dao.CardMapper;
import com.qwwuyu.server.dao.CommMapper;
import com.qwwuyu.server.service.ICardService;

@Service("card")
public class ICardServiceImpl implements ICardService {
	private String table = "card";
	private int numOfPage = 10;
	@Resource
	private CommMapper commMapper;
	@Resource
	private CardMapper mapper;

	@Override
	public int insert(Card card) {
		return mapper.insert(card);
	}

	@Override
	public Card selectByPrimaryKey(Integer id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Card record) {
		return mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<Card> selectByCard(Card card, int limit, int offset, String asc, String desc) {
		return mapper.selectByCard(card, limit, offset, asc, desc);
	}

	@Override
	public Map<String, Object> getCard(int page) {
		Map<String, Object> map = new HashMap<>();
		int count = commMapper.selectCountByTable(table);
		List<Card> datas = mapper.selectByCard(new Card(), numOfPage, (page - 1) * numOfPage, null, "time");
		map.put("page", (count + numOfPage - 1) / numOfPage);
		map.put("count", count);
		map.put("select", page);
		map.put("datas", datas);
		map.put("sysTime", System.currentTimeMillis());
		return map;
	}
}