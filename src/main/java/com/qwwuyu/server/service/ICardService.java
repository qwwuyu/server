package com.qwwuyu.server.service;

import java.util.List;

import com.qwwuyu.server.bean.Card;

public interface ICardService {
	void insert(Card note);

	Card selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Card note);

	List<Card> selectByCard(Card note, int limit, int offset, String asc, String desc);

	int deleteByPrimaryKey(Integer id);
}
