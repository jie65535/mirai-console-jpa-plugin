package top.jie65535.jpa

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.utils.info
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.TimeUnit


object JPictureArchiving : KotlinPlugin(
    JvmPluginDescription(
        id = "top.jie65535.mirai-console-jpa-plugin",
        name = "J Picture Archiving",
        version = "0.1.0"
    ) {
        author("jie65535")
        info("这个插件只做一件事，将机器人收到的所有图片存档")
    }
) {
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    override fun onEnable() {
        logger.info { "Plugin loaded" }
        JPAPluginConfig.reload()
        JPACommand.register()

        globalEventChannel().subscribeAlways<MessageEvent> {
            message.filterIsInstance<Image>().forEach {
                val url = it.queryUrl()
                val filePath = "${this.subject.id}/${it.imageId}"
                val file = if (JPAPluginConfig.archiveDirectory.isBlank()) {
                    resolveDataFile(filePath)
                } else {
                    File(JPAPluginConfig.archiveDirectory, filePath)
                }
                if (!file.exists()) {
                    val request = Request.Builder().url(url).build()
                    val imageByte = okHttpClient.newCall(request).execute().body!!.bytes()
                    val fileParent = file.parentFile
                    if (!fileParent.exists()) fileParent.mkdirs()
                    file.writeBytes(imageByte)
                    logger.info("Saved ${file.path}.")
                }
            }
        }
    }
}
