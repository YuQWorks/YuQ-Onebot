package com.icecreamqaq.yuq.onebot.connect

import com.IceCreamQAQ.Yu.toJSONString
import com.alibaba.fastjson.JSONObject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
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
        val msg = SendMessage(echo = echo.toString(), action = action, params = params).toJSONString()
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
                        else it.complete(json.getJSONObject("data"))
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