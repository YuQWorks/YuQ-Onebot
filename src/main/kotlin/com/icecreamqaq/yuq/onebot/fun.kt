package com.icecreamqaq.yuq.onebot

import com.IceCreamQAQ.Yu.event.EventBus
import com.icecreamqaq.yuq.entity.Contact
import com.icecreamqaq.yuq.message.Message
import com.icecreamqaq.yuq.message.MessageSource

internal lateinit var localEventBus: EventBus
internal lateinit var control: OntBotControl

internal fun <T> Message.send(contact: Contact, obj: T, send: (T) -> MessageSource) = control.rainBot.sendMessage(this, contact, obj, send)

