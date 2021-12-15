package top.jie65535.jpa

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object JPAPluginConfig : AutoSavePluginConfig("jpa") {
    @ValueDescription("指定存档根目录 为空则保存到插件数据目录")
    var archiveDirectory: String by value()
}