package com.icecreamqaq.yuq.onebot.entity

import com.IceCreamQAQ.Yu.toJSONObject
import com.icecreamqaq.yuq.YuQ
import com.icecreamqaq.yuq.entity.*
import com.icecreamqaq.yuq.message.Image
import com.icecreamqaq.yuq.message.Message
import com.icecreamqaq.yuq.message.MessageSource
import com.icecreamqaq.yuq.onebot.message.*
import com.icecreamqaq.yuq.onebot.control
import com.icecreamqaq.yuq.onebot.send
import com.icecreamqaq.yuq.util.WebHelper.Companion.postWithQQKey
import com.icecreamqaq.yuq.web
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.io.File

abstract class ContactImpl() : Contact {

    override val yuq: YuQ
        get() = control

    private val log = LoggerFactory.getLogger(ContactImpl::class.java)

    override fun sendMessage(message: Message): MessageSource {
//        return message.send(this, miraiContact) {
//            kotlin.runCatching {
//                MiraiMessageSource(
//                    runBlocking {
//                        miraiContact.sendMessage(message.toLocal(this@ContactImpl))
//                    }.source
//                )
//            }.getOrElse {
//                control.rainBot.messageSendFailedByReadTimeout(this, message)
//            }
//        }
        TODO()
    }

    override fun sendFile(file: File) {
        TODO("Not yet implemented")
    }

    override fun uploadImage(imageFile: File): Image =
        runBlocking { TODO() }
}

//class FriendImpl() : ContactImpl(), Friend {
//
//    override val id = friend.id
//    override val platformId = id.toString()
//    override val guid = id.toString()
//
//    override val avatar
//        get() = friend.avatarUrl
//    override val name
//        get() = friend.nick
//
//    override fun click() {
//        runBlocking {
//            friend.sendNudge(friend.nudge())
//        }
//    }
//
//    override fun delete() {
//        TODO("Not yet implemented")
//    }
//
//    override fun toString() = "Friend($name($id))"
//
//}
//
//class GroupImpl(internal val group: MiraiGroup) : ContactImpl(group), Group {
//    override val id = group.id
//    override val platformId = id.toString()
//    override val guid = "g$id"
//
//    override var maxCount: Int = -1
//    override val admins = arrayListOf<GroupMemberImpl>()
//
//    override val avatar: String
//        get() = group.avatarUrl
//
//    override val name: String = group.name
//    override val notices: GroupNoticeList
//        get() = TODO("Not yet implemented")
//    override val owner: Member
//
//    override operator fun get(qq: Long) = super.get(qq) as GroupMemberImpl
//
//    override val members: UserListImpl<GroupMemberImpl>
//    override val bot: GroupMemberImpl
//
//    init {
//        members = UserListImpl()
//        var owner: GroupMemberImpl? = null
//        for (member in group.members) {
//            val m = GroupMemberImpl(member, this)
//            members[member.id] = m
//            if (m.permission == 2) owner = m
//            if (m.permission == 1) admins.add(m)
//        }
//        bot = GroupMemberImpl(group.botAsMember, this)
//        this.owner = owner ?: if (bot.permission == 2) bot else error("Group $id Can't Find Owner!")
//
//    }
//
//    fun refreshAdmin() {
//        admins.clear()
//        for (member in group.members) {
//            val m = GroupMemberImpl(member, this)
//            members[member.id] = m
//            if (m.permission == 1) admins.add(m)
//        }
//    }
//
//    init {
////        maxCount = -1
//
//        try {
//            maxCount = web.postWithQQKey(
//                "https://qun.qq.com/cgi-bin/qun_mgr/search_group_members",
//                mapOf(
//                    "gc" to id.toString(),
//                    "st" to 0.toString(),
//                    "end" to 15.toString(),
//                    "sort" to "0",
//                    "bkn" to "{gtk}"
//                ) as MutableMap<String, String>
//            ).toJSONObject().getIntValue("max_count")
//        } catch (e: Exception) {
//        }
//    }
//
//    override fun leave() {
//        runBlocking {
//            group.quit()
//        }
//    }
//
//    override fun isFriend() = false
//
//    override fun toString(): String {
//        return "Group($name($id))"
//    }
//
//    override fun banAll() {
//        group.settings.isMuteAll = true
//    }
//
//    override fun unBanAll() {
//        group.settings.isMuteAll = false
//    }
//
//
//}
//
//open class GroupMemberImpl(
//    internal val member: MiraiMember,
//    final override val group: GroupImpl
//) : ContactImpl(member),
//    Member {
//    override val platformId = id.toString()
//    override val guid = "${group.id}_$id"
//
//    override val permission
//        get() = member.permission.level
//    override var nameCard
//        get() = member.nameCard
//        set(value) {
//            member.nameCard = value
//        }
//    override var title
//        get() = member.specialTitle
//        set(value) {
//            member.specialTitle = value
//        }
//
//    override fun at() = AtImpl(id)
//
//    override val ban: Int
//        get() {
//            return member.muteTimeRemaining
//        }
//
//    override fun ban(time: Int) {
//        runBlocking {
//            member.mute(time)
//        }
//    }
//
//    override fun click() {
//        runBlocking {
//            group.group.sendNudge(member.nudge())
//        }
//    }
//
//    override fun clickWithTemp() {
//        TODO("Not yet implemented")
//    }
//
//    override fun unBan() {
//        runBlocking {
//            member.unmute()
//        }
//    }
//
//    override fun kick(message: String) {
//        runBlocking {
//            member.kick(message)
//        }
//    }
//
//    override fun toString(): String {
//        return "Member($nameCard($id)[${group.name}(${group.id}])"
//    }
//
//    final override val id
//        get() = member.id
//    override val lastMessageTime: Long
//        get() = member.lastSpeakTimestamp.toLong() * 1000
//    override val avatar
//        get() = member.avatarUrl
//    override val name
//        get() = member.nick
//
//
//}
//
//class AnonymousMemberImpl(member: MiraiMember, group: GroupImpl) : GroupMemberImpl(member, group), AnonymousMember {
//
//    override val guid = "${id}_${group.id}"
//
//    override fun canSendMessage() = false
//    override fun isFriend() = false
//
//}
