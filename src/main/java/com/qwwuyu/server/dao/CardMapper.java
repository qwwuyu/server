package com.qwwuyu.server.dao;

import com.qwwuyu.server.bean.Card;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CardMapper {
    Card selectByPrimaryKey(Integer id);

    int insert(Card record);

    int updateByPrimaryKeySelective(Card record);

    int deleteByPrimaryKey(Integer id);

    List<Card> selectByCard(@Param("card") Card card, @Param("limit") int limit, @Param("offset") int offset, @Param("asc") String asc, @Param("desc") String desc);
}