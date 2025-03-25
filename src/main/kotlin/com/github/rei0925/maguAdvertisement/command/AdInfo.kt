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

        // UUIDで言語を取得
        val uuid = sender.uniqueId.toString()
        val langCode = AdSetting.getLanguageByUUID(uuid)  // UUIDで言語取得
        val locale = AdSetting.toLocale(langCode)          // 言語コード → Locale に変換

        val langManager = MaguAdvertisement.langManager

        // 多言語メッセージを取得
        val title = TextComponent(
            "═══ ${langManager.getMessage(locale, "menu.title", "MaguAdvertisement")} ════════════════\n"
        )
        title.color = ChatColor.of("#EA553A")
        title.isBold = true

        // 広告データを取得
        val adData = getAdDataById(adId)

        if (adData == null) {
            sender.sendMessage(TextComponent("${ChatColor.RED}広告が見つかりませんでした: ID $adId"))
            return
        }

        // レベルを表示
        val levelName = TextComponent("§e${langManager.getMessage(locale, "menu.level.level_name", "AD Level")} §a: ")

        // 広告のレベルを取得
        val level = adData.optString("level", "unknown").lowercase(Locale.getDefault())
        val levelText = TextComponent(langManager.getMessage(locale, "menu.level.$level", "Unknown Level"))

        // レベルに応じた色を設定
        when (level) {
            "low" -> levelText.color = ChatColor.YELLOW
            "middle" -> levelText.color = ChatColor.LIGHT_PURPLE
            "high" -> levelText.color = ChatColor.AQUA
            else -> levelText.color = ChatColor.DARK_GRAY
        }

        // 広告情報の表示
        val adTitle = TextComponent("${ChatColor.GOLD}${adData.optString("title", "No Title")}")
        val adContent = TextComponent("${ChatColor.WHITE}${adData.optString("content", "No Content")}")

        // メッセージを送信
        sender.sendMessage(title, levelName, levelText, adTitle, adContent)
    }

    /**
     * adIdに基づく広告データを取得するメソッド
     */
    private fun getAdDataById(adId: Int): JSONObject? {
        val adFile = File("plugins/MaguAdvertisement/ad_list.json")
        if (!adFile.exists()) {
            return null
        }

        val adList = JSONArray(adFile.readText())

        for (i in 0 until adList.length()) {
            val adObject = adList.getJSONObject(i)
            if (adObject.getInt("adID") == adId) {
                return adObject
            }
        }
        return null
    }
}
