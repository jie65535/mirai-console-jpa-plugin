package top.jie65535.jpa

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand

object JPACommand : CompositeCommand(
    JPictureArchiving,"jpa",
     description = "图片存档插件命令"
) {

    @SubCommand("setDir")
    @Description("设置图片存档目录")
    suspend fun CommandSender.setDir(dir: String) {
        JPAPluginConfig.archiveDirectory = dir
        sendMessage("OK")
    }

    @SubCommand("reset")
    @Description("重置存档目录到插件数据目录")
    suspend fun CommandSender.resetDir() {
        JPAPluginConfig.archiveDirectory = ""
        sendMessage("OK")
    }
}