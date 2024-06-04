package com.icecreamqaq.yuq.onebot.message

import com.alibaba.fastjson.JSONObject
import com.icecreamqaq.yuq.entity.Contact
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
    val m = Message(body)

    for (i in 0 until ma.size) {
        val item = ma.getJSONObject(i)

        val data = item.getJSONObject("data")
        when (item.getString("type")) {
            "text" -> TextImpl(data.getString("text"))
            "at" -> AtImpl(data.getLongValue("qq"))
            "face" -> FaceImpl(data.getIntValue("id"))
            "image" -> ImageReceive(data.getString("file"), data.getString("url"))
            "reply" -> {
                m.reply = OneBotMessageSource(data.getIntValue("id"), 0, 0, 0)
                null
            }

            else -> NoImplItemImpl(item)
        }?.let { body.append(it) }
    }

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

fun message2ObMessageArray(contact: Contact, message: Message): List<Any> {
    val body = ArrayList<Any>()
    message.reply?.let { body.add(omi("reply", "id" to it.id)) }
    body.addAll(itemChain2ObMessageArray(contact, message.body))
    return body
}

fun itemChain2ObMessageArray(contact: Contact, chain: MessageItemChain): List<Any> {
    val body = ArrayList<Any>()
    chain.forEach { item ->
        item.toLocal(contact)
            .let {
                if (it is List<*>) body.addAll(it as List<Any>)
                else body.add(it)
            }
    }
    return body
}