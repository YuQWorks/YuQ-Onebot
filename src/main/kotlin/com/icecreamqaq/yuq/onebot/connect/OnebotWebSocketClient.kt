package com.icecreamqaq.yuq.onebot.connect

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.io.Closeable

class OnebotWebSocketClient(
    private val url: String,
    private val token: String? = null
) : Closeable {

    private var isClosed = false

    val client = OkHttpClient.Builder()
        .build()
    lateinit var wsConnect: WebSocket



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
                super.onMessage(webSocket, text)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                runBlocking { reConnect() }
            }
        }
        wsConnect = client.newWebSocket(Request.Builder().url("ws://$url").build(), handler)
        connectWait.await()
    }

    override fun close() {
        isClosed = true
        if (::wsConnect.isInitialized)
            wsConnect.close(1000, "close")
    }


}