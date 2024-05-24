package com.icecreamqaq.yuq.onebot

import com.IceCreamQAQ.Yu.di.YuContext
import com.IceCreamQAQ.Yu.module.Module
import com.icecreamqaq.yuq.onebot.util.YuQInternalFunImpl
import com.icecreamqaq.yuq.util.YuQInternalFun
import javax.inject.Inject

class OnebotModule : Module {

    @Inject
    private lateinit var context: YuContext

    override fun onLoad() {
        context.putBean(YuQInternalFun::class.java, "", YuQInternalFunImpl())
    }
}