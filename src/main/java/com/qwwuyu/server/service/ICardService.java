package com.qwwuyu.server.service;

import java.util.List;

import com.qwwuyu.server.bean.Card;

public interface ICardService {
	int insert(Card card);

	Card selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Card card);

	int deleteByPrimaryKey(Integer id);

	List<Card> selectByCard(Card card, int limit, int offset, String asc, String desc);

	String getCard(int page);
}
