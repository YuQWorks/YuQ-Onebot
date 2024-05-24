package com.icecreamqaq.yuq.onebot

import com.IceCreamQAQ.Yu.annotation.Config
import com.IceCreamQAQ.Yu.annotation.Default
import com.IceCreamQAQ.Yu.`as`.ApplicationService
import com.IceCreamQAQ.Yu.cache.EhcacheHelp
import com.IceCreamQAQ.Yu.controller.Router
import com.IceCreamQAQ.Yu.di.YuContext
import com.IceCreamQAQ.Yu.event.EventBus
import com.IceCreamQAQ.Yu.util.Web
import com.icecreamqaq.yuq.*
import com.icecreamqaq.yuq.controller.ContextRouter
import com.icecreamqaq.yuq.controller.ContextSession
import com.icecreamqaq.yuq.entity.*
import com.icecreamqaq.yuq.message.MessageItemFactory
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Named

open class OntBotControl : YuQ, ApplicationService, User, YuQVersion {

    companion object{
        private val log = LoggerFactory.getLogger(OntBotControl::class.java)
    }

    override lateinit var avatar: String
    override var id: Long = 0
    override lateinit var name: String
    override val platformId: String
        get() = botId.toString()

    override fun canSendMessage() = false

    override fun isFriend() = false
    override fun runtimeName() = "YuQ-Onebot"

    override fun runtimeVersion() = "0.0.1-DEV1"

    override val botId: Long
        get() = id

    override val botInfo: User
        get() = this

    override val cookieEx: YuQ.QQCookie
        get() = TODO("Not yet implemented")


    @Config("YuQ.bot.name")
    private var botName: String? = null

    @Config("YuQ.Mirai.protocol")
    @Default("HD")
    lateinit var protocol: String

    @Inject
    @field:Named("group")
    lateinit var group: Router

    @Inject
    @field:Named("priv")
    lateinit var priv: Router

    @Inject
    lateinit var contextRouter: ContextRouter

    @Inject
    override lateinit var web: Web
    override fun id2platformId(id: Long) = id.toString()

    override fun platformId2id(platformId: String) = platformId.toLong()

    @Inject
    lateinit var eventBus: EventBus

    @Inject
    override lateinit var messageItemFactory: MessageItemFactory
//    override val web: Web

    @Inject
    lateinit var rainBot: YuQInternalBotImpl

    @Inject
    @field:Named("ContextSession")
    lateinit var sessionCache: EhcacheHelp<ContextSession>

    @Inject
    lateinit var context: YuContext



    override lateinit var friends: UserListImpl<Friend>
    override lateinit var groups: UserListImpl<Group>
    override val guilds: GuildList
        get() = TODO("Not yet implemented")




//    var DefaultLogger: (identity: String?) -> MiraiLogger = { YuQMiraiLogger }

    override fun init() {
//        FPMM.getTime = { System.currentTimeMillis() }
//        FPMM.clear()

        mif = messageItemFactory
//        mf = messageFactory
        yuq = this
//        id = qq.toLong()
        com.icecreamqaq.yuq.web = web
        localEventBus = eventBus
        com.icecreamqaq.yuq.eventBus = eventBus
        control = this


    }



    override fun refreshFriends(): FriendList {
//        val friends = UserListImpl<FriendImpl>()
//        for (friend in bot.friends) {
//            friends[friend.id] = FriendImpl(friend)
//        }
//        this.friends = friends
        return friends
    }

    override fun refreshGroups(): GroupList {
//        val groups = UserListImpl<GroupImpl>()
//        for (group in bot.groups) {
//            try {
//                groups[group.id] = GroupImpl(group)
//            } catch (e: Exception) {
//                log.error("Load Group ${group.id} Error!", e)
//            }
//
//        }
//        this.groups = groups
        return groups
    }

    override fun refreshGuilds(): GuildList {
        TODO("Not yet implemented")
    }

    override fun start() {
        context.injectBean(rainBot)
    }

    override fun stop() {

    }


}