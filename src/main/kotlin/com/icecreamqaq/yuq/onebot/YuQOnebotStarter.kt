package com.icecreamqaq.yuq.onebot

import com.IceCreamQAQ.Yu.DefaultStarter
import com.IceCreamQAQ.Yu.loader.AppClassloader
import com.icecreamqaq.yuq.YuQStarter
import org.slf4j.LoggerFactory
import java.lang.reflect.Method

//@Deprecated(
//        message = "建议使用 YuQ 提供的启动类，YuQStarter",
//        level = DeprecationLevel.WARNING,
//        replaceWith = ReplaceWith("YuQStarter", "com.icecreamqaq.yuq.YuQStarter")
//)

class YuQOnebotStarter {


    companion object {
        private val log = LoggerFactory.getLogger(YuQOnebotStarter::class.java)

        @JvmStatic
        fun start() {
//            AppClassloader.registerBackList(arrayListOf("net.mamoe.", "javafx."))


            val startTime = System.currentTimeMillis()
            val classloader = AppClassloader(YuQStarter::class.java.classLoader)
            Thread.currentThread().contextClassLoader = classloader

            val yuClass = classloader.loadClass("com.IceCreamQAQ.Yu.DefaultApp")
            val start: Method? = yuClass.getMethod("start")

            val yu = yuClass.newInstance()
            start!!.invoke(yu)

            val overTime = System.currentTimeMillis()

            log.info("Done! ${(overTime - startTime).toDouble() / 1000}s.")

            println(
                " __  __     ____ \n" +
                        " \\ \\/ /_ __/ __ \\\n" +
                        "  \\  / // / /_/ /\n" +
                        "  /_/\\_,_/\\___\\_\\\n"
            )
            println("感谢您使用 YuQ 进行开发，在您使用中如果遇到任何问题，可以到 Github，Gitee 提出 issue，您也可以添加 YuQ 的开发交流群（787049553）进行交流。")
        }

        @JvmStatic
        fun start(args: Array<String>) {
            DefaultStarter.init(args)
            start()
        }

    }

}