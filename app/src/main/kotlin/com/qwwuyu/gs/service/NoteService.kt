package com.qwwuyu.gs.service

import com.qwwuyu.gs.entity.Note
import com.qwwuyu.gs.mapper.CommMapper
import com.qwwuyu.gs.mapper.NoteMapper
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.Resource

@Service("note")
class NoteService {
    private val table = "note"
    private val numOfPage = 10

    @Resource
    private lateinit var commMapper: CommMapper

    @Resource
    private lateinit var mapper: NoteMapper

    fun insert(note: Note): Int {
        return mapper.insert(note)
    }

    fun selectByPrimaryKey(id: Int): Note? {
        return mapper.selectByPrimaryKey(id)
    }

    fun updateByPrimaryKeySelective(record: Note): Int {
        return mapper.updateByPrimaryKeySelective(record)
    }

    fun deleteByPrimaryKey(id: Int): Int {
        return mapper.deleteByPrimaryKey(id)
    }

    fun selectByNote(note: Note, limit: Int, offset: Int, asc: String?, desc: String?): List<Note> {
        return mapper.selectByNote(note, limit, offset, asc, desc)
    }

    fun getNote(page: Int): Map<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        val count = commMapper.selectCountByTable(table)
        val datas = mapper.selectByNote(Note(), numOfPage, (page - 1) * numOfPage, null, "time")
        map["page"] = (count + numOfPage - 1) / numOfPage
        map["count"] = count
        map["select"] = page
        map["datas"] = datas
        map["sysTime"] = System.currentTimeMillis()
        return map
    }
}