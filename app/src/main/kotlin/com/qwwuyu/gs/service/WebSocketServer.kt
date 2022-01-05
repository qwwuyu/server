package com.qwwuyu.gs.service

import com.qwwuyu.gs.entity.User
import com.qwwuyu.lib.utils.WLog
import org.springframework.stereotype.Component
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.Resource
import javax.websocket.*
import javax.websocket.server.ServerEndpoint

@ServerEndpoint("/ws")
@Component
class WebSocketServer {
    companion object {
        private val sessions = ConcurrentHashMap<String, Session>()
        private val users = ConcurrentHashMap<String, User>()
        private val topics = ConcurrentHashMap<String, MutableList<Session>>()

        fun sendMsg(id: String, message: String): String? {
            val session = sessions[id] ?: return "id notfound"
            return try {
                session.basicRemote.sendText(message)
                null
            } catch (e: Exception) {
                WLog.printStackTrace(e)
                e.message ?: "err"
            }
        }

        fun sendTopic(topic: String, message: String): String? {
            val topicList = topics[topic] ?: return "topic notfound"
            var error: String? = null
            topicList.forEach {
                try {
                    it.basicRemote.sendText(message)
                } catch (e: Exception) {
                    WLog.printStackTrace(e)
                    error = e.message ?: "err"
                }
            }
            return error
        }
    }

    @Resource
    private lateinit var userService: UserService

    @OnOpen
    fun onOpen(session: Session) {
        session.maxIdleTimeout = 60_000L
        sessions[session.id] = session
        WLog.i("onOpen:size=${sessions.size} id=${session.id}")
    }

    @OnMessage
    fun onMessage(session: Session, text: String) {
        WLog.i("onMessage:id=${session.id} text=$text")
        try {
            //TODO auth
        } catch (e: Exception) {
            WLog.printStackTrace(e)
        }
    }

    @OnMessage
    fun onMessage(session: Session, data: ByteBuffer) {
        WLog.i("onMessage:id=${session.id} data=$data")
    }

    @OnClose
    fun onClose(session: Session, cr: CloseReason) {
        sessions.remove(session.id)
        users.remove(session.id)
        WLog.i("onClose:size=${sessions.size} id=${session.id} $cr")
    }

    @OnError
    fun onError(session: Session, throwable: Throwable) {
        sessions.remove(session.id)
        users.remove(session.id)
        WLog.i("onError:size=${sessions.size} id=${session.id} throwable=${throwable.message ?: throwable::class.java.name}")
    }
}

