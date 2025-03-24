package com.github.rei0925.maguAdvertisement.command

import com.github.rei0925.maguAdvertisement.MaguAdvertisement
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.*

object AdInfo {
    fun info(sender: CommandSender, adId: Int) {
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

        // 広告データを取得（例: adIdを使ってJSONからデータを取得）
        val adData = getAdDataById(adId) // adIdに基づく広告データを取得する関数

        // レベルを表示
        val levelName = TextComponent("§e${langManager.getMessage(locale, "menu.level.level_name", "AD Level")} §a:")

        // 広告のレベルを取得
        val level = adData?.getString("level")?.lowercase(Locale.getDefault()) ?: "unknown" // 小文字に変換
        val levelText = TextComponent(langManager.getMessage(locale, "menu.level.$level", "Unknown Level"))

        // レベルに応じた色を設定
        when (level) {
            "low" -> levelText.color = ChatColor.YELLOW
            "middle" -> levelText.color = ChatColor.LIGHT_PURPLE
            "high" -> levelText.color = ChatColor.AQUA
            else -> levelText.color = ChatColor.DARK_GRAY
        }

        // メッセージを送信
        sender.sendMessage(title, levelName, levelText)
    }

    // adIdに基づく広告データを取得するメソッド（仮の実装）
    private fun getAdDataById(adId: Int): JSONObject? {
        val adFile = File("plugins/MaguAdvertisement/ad_list.json")
        val adList = JSONArray(adFile.readText())

        for (i in 0 until adList.length()) {
            val adObject = adList.getJSONObject(i)
            if (adObject.getInt("adID") == adId) {
                return adObject
            }
        }
        return null // 見つからなかった場合
    }
}

