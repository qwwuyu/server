package com.qwwuyu.gs.service

import com.qwwuyu.gs.entity.Card
import com.qwwuyu.gs.mapper.CardMapper
import com.qwwuyu.gs.mapper.CommMapper
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.Resource

@Service("card")
class CardService {
    private val table = "card"
    private val numOfPage = 10

    @Resource
    private lateinit var commMapper: CommMapper

    @Resource
    private lateinit var mapper: CardMapper

    fun insert(card: Card): Int {
        return mapper.insert(card)
    }

    fun selectByPrimaryKey(id: Int): Card? {
        return mapper.selectByPrimaryKey(id)
    }

    fun updateByPrimaryKeySelective(record: Card): Int {
        return mapper.updateByPrimaryKeySelective(record)
    }

    fun deleteByPrimaryKey(id: Int): Int {
        return mapper.deleteByPrimaryKey(id)
    }

    fun selectByCard(card: Card, limit: Int, offset: Int, asc: String?, desc: String?): List<Card> {
        return mapper.selectByCard(card, limit, offset, asc, desc)
    }

    fun getCard(page: Int): Map<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        val count = commMapper.selectCountByTable(table)
        val datas = mapper.selectByCard(Card(), numOfPage, (page - 1) * numOfPage, null, "time")
        map["page"] = (count + numOfPage - 1) / numOfPage
        map["count"] = count
        map["select"] = page
        map["datas"] = datas
        map["sysTime"] = System.currentTimeMillis()
        return map
    }
}