package com.icecreamqaq.yuq.onebot.entity

import com.alibaba.fastjson.JSONObject
import com.icecreamqaq.yuq.YuQ
import com.icecreamqaq.yuq.entity.*
import com.icecreamqaq.yuq.event.GroupMemberJoinEvent
import com.icecreamqaq.yuq.message.Image
import com.icecreamqaq.yuq.message.Message
import com.icecreamqaq.yuq.message.MessageSource
import com.icecreamqaq.yuq.onebot.connect.OnebotWebSocketClient.Companion.action
import com.icecreamqaq.yuq.onebot.control
import com.icecreamqaq.yuq.onebot.message.AtImpl
import com.icecreamqaq.yuq.onebot.message.OneBotMessageSource
import com.icecreamqaq.yuq.onebot.message.omi
import com.icecreamqaq.yuq.post
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.io.File

abstract class ContactImpl() : Contact {

    override val yuq: YuQ
        get() = control

    private val log = LoggerFactory.getLogger(ContactImpl::class.java)

    private fun Message.send(send: () -> MessageSource) =
        control.rainBot.sendMessage(this, this@ContactImpl, Unit) { send() }

    abstract suspend fun sendMessageAction(messageArray: List<Any>): JSONObject

    override fun sendMessage(message: Message): MessageSource {
        return message.send {
            runBlocking {
                val body = ArrayList<Any>()
                message.reply?.let { body.add(omi("reply", "id" to it.id)) }
                message.body.forEach { body.add(it.toLocal(this@ContactImpl)) }
                sendMessageAction(body)
                    .getJSONObject("data")
                    .getIntValue("message_id")
                    .let { OneBotMessageSource(it, yuq.botId, id, System.currentTimeMillis()) }
            }
        }
    }

    override fun sendFile(file: File) {
        TODO("Not yet implemented")
    }

    override fun uploadImage(imageFile: File): Image =
        runBlocking { TODO() }
}

class FriendImpl(
    override val id: Long,
    name: String
) : ContactImpl(), Friend {

    override var name = name
        internal set

    override val platformId = id.toString()
    override val guid = id.toString()
    override suspend fun sendMessageAction(messageArray: List<Any>): JSONObject =
        control.clinet.action("send_private_msg", "user_id" to id, "message" to messageArray)


    override val avatar: String = "https://q1.qlogo.cn/g?b=qq&nk=$id&s=640"
    override fun click() {
        TODO()
    }

    override fun delete() {
        TODO("Not yet implemented")
    }


    override fun toString() = "Friend($name($id))"

}

//
class GroupImpl(
    override val id: Long,
    override val name: String
) : ContactImpl(), Group {
    override val platformId = id.toString()
    override val guid = "g$id"

    override var maxCount: Int = -1
    override val admins = arrayListOf<GroupMemberImpl>()
    override suspend fun sendMessageAction(messageArray: List<Any>): JSONObject =
        control.clinet.action("send_group_msg", "group_id" to id, "message" to messageArray)

    override val avatar: String
        get() = "https://p.qlogo.cn/gh/$id/$id/640"

    override val notices: GroupNoticeList
        get() = TODO("Not yet implemented")

    override operator fun get(qq: Long) = super.get(qq) as GroupMemberImpl

    override val members: UserListImpl<GroupMemberImpl> = UserListImpl()
    override lateinit var bot: GroupMemberImpl
    override lateinit var owner: Member

    init {
        refreshMember(false)
    }

    data class ObMemberResp(
        val groupId: Long,
        val userId: Long,
        val nickname: String,
        val card: String,
        val sex: String,
        val age: Int,
        val area: String,
        val joinTime: Long,
        val lastSendTime: Long,
        val level: Int,
        val role: String,
        val title: String
    ) {
        val permission: Int
            get() = when (role) {
                "owner" -> 2
                "admin" -> 1
                else -> 0
            }
    }

    fun refreshMember(event: Boolean) {
        val ofs = runBlocking { control.clinet.action("get_group_member_list", "group_id" to id) }
            .getJSONArray("data")
            .toJavaList(ObMemberResp::class.java)

        var owner: GroupMemberImpl? = null
        var bot: GroupMemberImpl? = null

        for (me in ofs) {
            var member = members[me.userId]
            if (member == null) {
                member =
                    GroupMemberImpl(this, me.userId, me.nickname, me.card, me.title, me.permission, me.lastSendTime)
                if (event)
                    GroupMemberJoinEvent(this, members[me.userId]!!).post()

                members[me.userId] = member
            }

            if (member.permission == 2) owner = member
            if (member.id == yuq.botId) bot = member

            if (member.permission == 1) admins.add(member)
        }

        this.owner = owner ?: error("Group $id Can't Find Owner!")
        this.bot = bot ?: error("Group $id Can't Find Bot!")
    }

    override fun leave() {
        TODO()
    }

    override fun isFriend() = false

    override fun toString(): String {
        return "Group($name($id))"
    }

    override fun banAll() {
        TODO()
    }

    override fun unBanAll() {
        TODO()
    }


}

//
open class GroupMemberImpl(
    final override val group: GroupImpl,
    override val id: Long,
    override val name: String,

    nameCard: String,
    title: String,
    override val permission: Int,

    override val lastMessageTime: Long
) : ContactImpl(), Member {
    override val platformId = id.toString()
    override val guid = "${group.id}_$id"

    override fun at() = AtImpl(id)

    override var nameCard: String = nameCard
        set(value) {
            TODO()
            field = value
        }

    override var title: String = title
        set(value) {
            TODO()
            field = value
        }

    override val ban: Int
        get() {
            TODO()
        }

    override fun ban(time: Int) {
        TODO()
    }

    override fun click() {
        runBlocking {
            TODO()
        }
    }

    override fun clickWithTemp() {
        TODO("Not yet implemented")
    }

    override fun unBan() {
        runBlocking {
            TODO()
        }
    }

    override fun kick(message: String) {
        runBlocking {
            TODO()
        }
    }

    override fun toString(): String {
        return "Member($nameCard($id)[${group.name}(${group.id}])"
    }

    override suspend fun sendMessageAction(messageArray: List<Any>): JSONObject {
        TODO("Not yet implemented")
    }

    override val avatar
        get() = ""


}
//
//class AnonymousMemberImpl(member: MiraiMember, group: GroupImpl) : GroupMemberImpl(member, group), AnonymousMember {
//
//    override val guid = "${id}_${group.id}"
//
//    override fun canSendMessage() = false
//    override fun isFriend() = false
//
//}
