package com.icecreamqaq.yuq.onebot.message

import com.alibaba.fastjson.JSONObject
import com.icecreamqaq.yuq.entity.Contact
import com.icecreamqaq.yuq.entity.Group
import com.icecreamqaq.yuq.entity.Member
import com.icecreamqaq.yuq.message.*
import com.icecreamqaq.yuq.message.At
import com.icecreamqaq.yuq.message.Face
import com.icecreamqaq.yuq.message.FlashImage
import com.icecreamqaq.yuq.message.Image
import com.icecreamqaq.yuq.message.Voice
import com.icecreamqaq.yuq.onebot.entity.ContactImpl
import kotlinx.coroutines.runBlocking
import java.io.InputStream


fun omi(type: String, vararg args: Pair<String, Any>) = mapOf("type" to type, "data" to mapOf(*args))

class TextImpl(override var text: String) : MessageItemBase(), Text {

    override fun toLocal(contact: Contact) = omi("text", "text" to text)

}

//
class AtImpl(override var user: Long) : MessageItemBase(), At {

    override fun toLocal(contact: Contact) =
        if (contact is Group)
            if (user == -1L) omi("at", "qq" to "all")
            else omi("at", "qq" to user)
        else omi("text", "text" to "@$user")

}

//
class AtMemberImpl(override val member: Member) : MessageItemBase(), AtByMember {
    override fun toLocal(contact: Contact) = omi("at", "qq" to member.id)
}

//
class FaceImpl(override val faceId: Int) : MessageItemBase(), Face {

    override fun toLocal(contact: Contact) = omi("face", "id" to faceId)

}

//
class ImageSend(private val file: String) : MessageItemBase(), Image {

    override val id: String = "Onebot 下主动发送不提供 id"
    override val url: String = "Onebot 下主动发送不提供 url"

    override fun toLocal(contact: Contact): Any =
        omi("image", "file" to file)

    override fun toPath() = "图片"

}

//
open class ImageReceive(override val id: String, override val url: String) : MessageItemBase(), Image {

    override fun toLocal(contact: Contact): Any =
        omi("image", "file" to id)

}
//
//
class FlashImageImpl(override val image: Image) : MessageItemBase(), FlashImage {

    override fun toLocal(contact: Contact): Any {
        return (image.toLocal(contact) as JSONObject).getJSONObject("data").set("type", "flash")
    }

    override fun toPath() = "闪照"
}
//
//class VoiceRecv(
//    val miraiVoice: OnlineAudio
//) : MessageItemBase(), Voice {
//
//    override val id: String = miraiVoice.filename
//    override val url: String = miraiVoice.urlForDownload ?: ""
//
//    override fun toLocal(contact: Contact) = miraiVoice
//}
//
//class VoiceSend(val inputStream: InputStream) : MessageItemBase(), Voice {
//
//    lateinit var miraiVoice: MiraiVoice
//
//    override fun toPath() = if (::miraiVoice.isInitialized) miraiVoice.filename ?: "" else "语音"
//
//    override val id: String
//        get() = miraiVoice.filename
//    override val url: String
//        get() = ""
//
//    override fun toLocal(contact: Contact): Any {
//        return if (::miraiVoice.isInitialized) miraiVoice
//        else if (contact is GroupImpl)
//            runBlocking {
//                contact.group.uploadAudio(inputStream.toExternalResource())
//            }.apply { miraiVoice = this }
//        else error("mirai send voice only supposed group!")
//    }
//
//}
//
//class XmlImpl(override val serviceId: Int, override val value: String) : MessageItemBase(), XmlEx {
//
//    override fun toLocal(contact: Contact) = SimpleServiceMessage(serviceId, value)
//
//}
//
//class JsonImpl(override val value: String) : MessageItemBase(), JsonEx {
//
//    override fun toLocal(contact: Contact) = LightApp(value)
//
//}
//
class NoImplItemImpl(override var source: Any) : MessageItemBase(), NoImplItem {
    override fun toLocal(contact: Contact) = source
}

