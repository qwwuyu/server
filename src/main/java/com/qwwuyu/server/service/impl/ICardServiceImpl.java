package com.qwwuyu.server.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qwwuyu.server.bean.Card;
import com.qwwuyu.server.dao.CardMapper;
import com.qwwuyu.server.service.ICardService;

@Service("card")
public class ICardServiceImpl implements ICardService {
	@Resource
	private CardMapper mapper;

	@Override
	public void insert(Card card) {
		mapper.insert(card);
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

}