package com.icecreamqaq.yuq.onebot.connect

import com.IceCreamQAQ.Yu.toJSONString
import com.alibaba.fastjson.JSONObject
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.io.Closeable
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class OnebotWebSocketClient(
    private val url: String,
    private val token: String? = null
) : Closeable {

    companion object {
        suspend fun OnebotWebSocketClient.action(action: String, vararg params: Pair<String, Any>) =
            sendAction(action, mapOf(*params))
    }

    private var isClosed = false

    val client = OkHttpClient.Builder()
        .build()
    lateinit var wsConnect: WebSocket

    val msgSeq = AtomicInteger()
    val messageMap = ConcurrentHashMap<Int, CompletableDeferred<JSONObject>>()

    data class SendMessage(
        val echo: String,
        val action: String,
        val params: Map<String, Any>
    )

    suspend fun sendAction(action: String, params: Map<String, Any>): JSONObject {
        val echo = msgSeq.incrementAndGet()
        val msg = SendMessage(echo = echo.toString(), action = action, params = params).toJSONString().apply { println(this) }
        val msgWait = CompletableDeferred<JSONObject>()
        messageMap[echo] = msgWait
        return try {
            withTimeout(5000) {
                wsConnect.send(msg)
                return@withTimeout msgWait.await()
            }
        } finally {
            messageMap.remove(echo)
        }
    }

    suspend fun connect() {
        isClosed = false
        reConnect()
    }

    private val eventHandler = ConcurrentHashMap<String, suspend (JSONObject) -> Unit>()

    fun registerEventHandler(event: String, handler: suspend (JSONObject) -> Unit) {
        eventHandler[event] = handler
    }

    private suspend fun reConnect() {
        if (isClosed) return

        val connectWait = CompletableDeferred<Unit>()

        val handler = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                connectWait.complete(Unit)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val json = JSONObject.parseObject(text)

                val echo = json.getInteger("echo")
                if (echo != null)
                    messageMap[echo]?.let {
                        if (json.getIntValue("retcode") != 0)
                            it.completeExceptionally(RuntimeException("OneBot Error! status: ${json.getString("status")}"))
                        else it.complete(json)
                    }
                else {
                    val postType = json.getString("post_type") ?: error("post_type is null! $text")
                    val eventType = json.getString("${postType}_type") ?: error("${postType}_type is null! $text")

                    GlobalScope.launch {
                        try {
                            eventHandler["$postType.$eventType"]?.let { it(json) }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                runBlocking { reConnect() }
            }
        }
        wsConnect = client.newWebSocket(Request.Builder().url(url).build(), handler)
        connectWait.await()
    }

    override fun close() {
        isClosed = true
        if (::wsConnect.isInitialized)
            wsConnect.close(1000, "close")
    }

}