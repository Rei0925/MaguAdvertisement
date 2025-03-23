package com.github.rei0925.maguAdvertisement.command

import com.github.rei0925.maguAdvertisement.ClickText.clickableText
import com.github.rei0925.maguAdvertisement.MaguAdvertisement
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
        val langManager = MaguAdvertisement.langManager

        // 多言語メッセージを取得
        val title = TextComponent("═══ ${langManager.getMessage(locale, "menu.title", "MaguAdvertisement")} ════════════════\n")
        title.color = ChatColor.of("#EA553A")

        // クリック可能なメニュー項目
        val settings = clickableText("[${langManager.getMessage(locale, "menu.settings", "Settings")}]", "/ww settings")
        settings.color = ChatColor.YELLOW

        // 複数のTextComponentをまとめて送信
        sender.sendMessage(title, settings)
    }
}
