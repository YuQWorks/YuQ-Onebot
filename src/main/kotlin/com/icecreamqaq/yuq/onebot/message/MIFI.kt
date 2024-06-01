package com.icecreamqaq.yuq.onebot.message

import com.icecreamqaq.yuq.entity.Member
import com.icecreamqaq.yuq.message.*
import com.icecreamqaq.yuq.web
import java.awt.image.BufferedImage
import java.io.File
import java.io.InputStream

class MIFI : MessageItemFactory {

    override fun text(text: String) = TextImpl(text)

    override fun at(member: Member) = AtMemberImpl(member)

    override fun at(qq: Long) = AtImpl(qq)

    override fun face(id: Int) = FaceImpl(id)


    @Deprecated("Image 创建 API 调整，使得命名语义更加清晰。", replaceWith = ReplaceWith("imageByFile(file)"))
    override fun image(file: File) = imageByFile(file)

    @Deprecated("Image 创建 API 调整，使得命名语义更加清晰。", replaceWith = ReplaceWith("imageByUrl(url)"))
    override fun image(url: String) = imageByUrl(url)

    override fun imageByBufferedImage(bufferedImage: BufferedImage) = TODO()//ImageSend(bufferedImage.data.)

    override fun imageByFile(file: File) = TODO()//ImageSend(file.toExternalResource())

    override fun imageById(id: String) = TODO()//ImageReceive(id, "")

    override fun imageByInputStream(inputStream: InputStream) = TODO()//ImageSend(inputStream.toExternalResource().apply { inputStream.close() })

    override fun imageByUrl(url: String) = imageByInputStream(web.download(url))

    override fun imageToFlash(image: Image) = TODO()//FlashImageImpl(image)

    override fun voiceByInputStream(inputStream: InputStream) = TODO()//VoiceSend(inputStream)

    override fun xmlEx(serviceId: Int, value: String): XmlEx = TODO()//XmlImpl(serviceId, value)

    override fun jsonEx(value: String) = TODO()//JsonImpl(value)
    override fun messagePackage(flag: Int, body: MutableList<IMessageItemChain>): MessagePackage {
        TODO("Not yet implemented")
    }
}