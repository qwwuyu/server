package com.qwwuyu.server.dao;

import org.apache.ibatis.annotations.Param;

public interface CommMapper {
	int selectCountByTable(@Param("table") String table);
}