package com.icecreamqaq.yuq.onebot.message

import com.icecreamqaq.yuq.message.MessageSource

class OneBotMessageSource(
    override val id: Int,
    override val sender: Long,
    override val sendTo: Long,
    override val sendTime: Long
):MessageSource {

    override val liteMsg: String
        get() = ""

    override fun recall(): Int {
        TODO("Not yet implemented")
    }

}