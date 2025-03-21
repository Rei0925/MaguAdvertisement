package com.github.rei0925.theBungeeJinroProject.room

import com.github.rei0925.theBungeeJinroProject.TheBungeeJinroProject
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.*

object RoomCommandListener {
    fun openMenu(sender: CommandSender){
        if (sender !is ProxiedPlayer) {
            sender.sendMessage(TextComponent("This command can only be used by players."))
            return
        }

        // プレイヤーごとの言語を取得
        val locale: Locale = sender.locale ?: Locale.ENGLISH
        val langManager = TheBungeeJinroProject.langManager

        // 多言語メッセージを取得
        val title = TextComponent("═══ ${langManager.getMessage(locale, "menu.room_menu", "Werewolf Room Menu")} ════════════════\n")
        title.color = ChatColor.of("#EA553A")

        // 多言語対応 説明
        val description = TextComponent(
            langManager.getMessage(
                locale,
                "menu.room_description",
                "This is the menu for managing Werewolf rooms. Use the buttons below to join a game or create a new room!"
            )
        )
        description.color = ChatColor.AQUA

        // クリック可能なメニュー項目


        // 複数のTextComponentをまとめて送信
        sender.sendMessage(title, description)
    }
}