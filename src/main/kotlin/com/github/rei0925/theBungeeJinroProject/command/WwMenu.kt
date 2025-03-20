package com.github.rei0925.theBungeeJinroProject.command

import com.github.rei0925.theBungeeJinroProject.command.ClickText.clickableText
import com.github.rei0925.theBungeeJinroProject.TheBungeeJinroProject
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.*

object WwMenu {

    fun menu(sender: CommandSender) {
        if (sender !is ProxiedPlayer) {
            sender.sendMessage(TextComponent("This command can only be used by players."))
            return
        }

        // プレイヤーごとの言語を取得
        val locale: Locale = sender.locale ?: Locale.ENGLISH
        val langManager = TheBungeeJinroProject.langManager

        // 多言語メッセージを取得
        val title = TextComponent("═══ ${langManager.getMessage(locale, "menu.title", "Werewolf Game")} ════════════════\n")
        title.color = ChatColor.of("#EA553A")

        // クリック可能なメニュー項目
        val settings = clickableText("[${langManager.getMessage(locale, "menu.settings", "Settings")}]", "/ww settings")
        val play = clickableText("[${langManager.getMessage(locale, "menu.play", "Play")}]", "/ww play")
        val exit = clickableText("[${langManager.getMessage(locale, "menu.exit", "Exit")}]", "/ww exit")

        // 複数のTextComponentをまとめて送信
        sender.sendMessage(title, settings, play, exit)
    }
}
