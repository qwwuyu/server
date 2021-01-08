package com.qwwuyu.gs.service

import com.qwwuyu.gs.entity.Flag
import com.qwwuyu.gs.mapper.CommMapper
import com.qwwuyu.gs.mapper.FlagMapper
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.Resource

@Service("flag")
class FlagService {
    private val table = "flag"
    private val numOfPage = 10

    @Resource
    private lateinit var commMapper: CommMapper

    @Resource
    private lateinit var mapper: FlagMapper

    fun insert(flag: Flag): Int {
        return mapper.insert(flag)
    }

    fun selectByPrimaryKey(id: Int): Flag? {
        return mapper.selectByPrimaryKey(id)
    }

    fun updateByPrimaryKeySelective(record: Flag): Int {
        return mapper.updateByPrimaryKeySelective(record)
    }

    fun deleteByPrimaryKey(id: Int): Int {
        return mapper.deleteByPrimaryKey(id)
    }

    fun selectByFlag(flag: Flag, limit: Int, offset: Int, asc: String?, desc: String?): List<Flag> {
        return mapper.selectByFlag(flag, limit, offset, asc, desc)
    }

    fun getFlag(page: Int): Map<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        val count = commMapper.selectCountByTable(table)
        val datas = mapper.selectByFlag(Flag(), numOfPage, (page - 1) * numOfPage, null, "time")
        map["page"] = (count + numOfPage - 1) / numOfPage
        map["count"] = count
        map["select"] = page
        map["datas"] = datas
        map["sysTime"] = System.currentTimeMillis()
        return map
    }
}