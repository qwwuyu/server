package com.qwwuyu.server.dao;

import com.qwwuyu.server.bean.Flag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FlagMapper {
    Flag selectByPrimaryKey(Integer id);

    int insert(Flag record);

    int updateByPrimaryKeySelective(Flag record);

    int deleteByPrimaryKey(Integer id);

    List<Flag> selectByFlag(@Param("flag") Flag flag, @Param("limit") int limit, @Param("offset") int offset, @Param("asc") String asc, @Param("desc") String desc);
}