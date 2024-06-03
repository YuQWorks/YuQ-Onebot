package com.icecreamqaq.yuq.onebot.message

import com.alibaba.fastjson.JSONObject
import com.icecreamqaq.yuq.message.Message
import com.icecreamqaq.yuq.message.MessageItem
import com.icecreamqaq.yuq.message.MessageItemChain
import com.icecreamqaq.yuq.message.MessageSource

class OneBotMessageSource(
    override val id: Int,
    override val sender: Long,
    override val sendTo: Long,
    override val sendTime: Long
) : MessageSource {

    override val liteMsg: String
        get() = ""

    override fun recall(): Int {
        TODO("Not yet implemented")
    }

}

fun obMessageArray2Message(event: JSONObject): Message {
    val ma = event.getJSONArray("message")
    val body = MessageItemChain()

    for (i in 0 until ma.size) {
        val item = ma.getJSONObject(i)

        val data = item.getJSONObject("data")
        when (item.getString("type")) {
            "text" -> TextImpl(data.getString("text"))
            else -> TextImpl("未知消息类型：${item.getString("type")}")
        }.let { body.append(it) }
    }

    val m = Message(body)
    m.source = OneBotMessageSource(event.getIntValue("message_id"), 0, 0, event.getLongValue("time") * 1000)

    val pathArray = ArrayList<MessageItem>()
    body.forEach {
        if (it is TextImpl)
            pathArray.addAll(it.text.split(" ").map { s -> TextImpl(s) })
        else pathArray.add(it)
    }

    m.path = pathArray
    m.sourceMessage = event
    return m
}