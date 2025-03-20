package com.github.rei0925.theBungeeJinroProject.command

import com.github.rei0925.theBungeeJinroProject.TheBungeeJinroProject
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.*

object WwSetting {
    fun setting(sender: CommandSender, settings: String, value: String) {
        if (settings.isBlank()) {

            if (sender !is ProxiedPlayer) {
                sender.sendMessage(TextComponent("This command can only be used by players."))
                return
            }

            // プレイヤーごとの言語を取得
            val locale: Locale = sender.locale ?: Locale.ENGLISH
            val langManager = TheBungeeJinroProject.langManager

            // 多言語メッセージを取得
            val title =
                TextComponent("═══ ${langManager.getMessage(locale, "menu.title", "Werewolf Game")} ════════════════\n")
            title.color = ChatColor.of("#EA553A")

            // 複数のTextComponentをまとめて送信
            sender.sendMessage(title)

        }else{
            sender.sendMessage(TextComponent("${ChatColor.YELLOW}設定を変更しました → $settings = $value"))
        }
    }
}