package com.github.rei0925.maguAdvertisement.command

import com.github.rei0925.maguAdvertisement.MaguAdvertisement
import com.github.rei0925.maguAdvertisement.MaguAdvertisement.Companion.langManager
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import org.json.JSONArray
import java.io.File
import java.io.FileWriter
import java.util.*

object AdSetting {

    fun setting(sender: CommandSender, settings: String, value: String) {
        if (settings.isBlank()) {

            if (sender !is ProxiedPlayer) {
                sender.sendMessage(TextComponent("This command can only be used by players."))
                return
            }

            val uuid = sender.uniqueId.toString()
            val langCode = getLanguageByUUID(uuid)  // UUIDで言語取得
            val locale = toLocale(langCode)          // String → Locale に変換

            val langManager = MaguAdvertisement.langManager


            // 多言語メッセージを取得
            val title = TextComponent(
                "═══ ${langManager.getMessage(locale, "menu.title", "MaguAdvertisement")} ════════════════\n"
            )
            title.color = ChatColor.of("#EA553A")
            title.isBold = true

            // メッセージ送信
            sender.sendMessage(title)

        } else {
            sender.sendMessage(TextComponent("${ChatColor.YELLOW}設定を変更しました → $settings = $value"))
        }
    }

    fun setLanguage(sender: CommandSender, language: String) {
        if (sender !is ProxiedPlayer) {
            sender.sendMessage(TextComponent("This command can only be used by players."))
            return
        }

        val uuid = sender.uniqueId
        val langFolder = File(MaguAdvertisement.instance.dataFolder, "lang")

        // 言語フォルダが存在し、その中に指定された言語ファイルがあるか確認
        val availableLanguages = langFolder.listFiles { file -> file.extension == "yml" }
            ?.map { it.nameWithoutExtension }
            ?.toList()
            ?: listOf("en") // 言語ファイルがない場合は英語をデフォルトに

        if (language in availableLanguages) {
            // 言語が存在する場合、その言語を設定
            updateLanguageForUUID(uuid.toString(), language)  // player_list.json を更新

            sender.sendMessage(TextComponent(langManager.getMessage(toLocale(language), "menu.language.change", "Language has been changed to $language!")))
        } else {
            // 言語が存在しない場合、英語に変更してエラーメッセージを表示
            sender.sendMessage(TextComponent("Sorry, the language '$language' is not available. The language has been set to English."))
        }
    }

    /**
     * UUIDで`player_list.json`から言語を取得
     */
    fun getLanguageByUUID(uuid: String): String {
        val playerListFile = File(MaguAdvertisement.instance.dataFolder, "player_list.json")

        // ファイルが存在しない場合はデフォルト言語を返す
        if (!playerListFile.exists()) {
            return "en"
        }

        val jsonData = playerListFile.readText()
        val playerList = JSONArray(jsonData)

        for (i in 0 until playerList.length()) {
            val player = playerList.getJSONObject(i)

            if (player.optString("uuid") == uuid) {
                return player.optString("language", "en")  // 言語がない場合は英語
            }
        }

        // 該当プレイヤーが存在しない場合は英語を返す
        return "en"
    }

    /**
     * 言語コード(String)をLocaleに変換
     */
    fun toLocale(langCode: String): Locale {
        return try {
            if (langCode.contains("_")) {
                // ロケールが「言語_国」形式の場合 (例: ja_JP)
                val parts = langCode.split("_")
                Locale.of(parts[0], parts.getOrElse(1) { "" })
            } else {
                // 単一言語コードの場合 (例: ja, en)
                Locale.of(langCode)
            }
        } catch (e: Exception) {
            Locale.ENGLISH  // 不明な場合は英語にフォールバック
        }
    }

    /**
     * プレイヤーのUUIDに基づいて言語設定を`player_list.json`に保存
     */
    private fun updateLanguageForUUID(uuid: String, language: String) {
        val playerListFile = File(MaguAdvertisement.instance.dataFolder, "player_list.json")

        // ファイルが存在しない場合は何もせず終了
        if (!playerListFile.exists()) {
            return
        }

        // 既存のデータを読み込む
        val jsonData = playerListFile.readText()
        val playerList = JSONArray(jsonData)

        var playerFound = false

        // プレイヤーがすでに存在する場合、言語を更新
        for (i in 0 until playerList.length()) {
            val player = playerList.getJSONObject(i)

            if (player.optString("uuid") == uuid) {
                player.put("language", language)  // 言語を更新
                playerFound = true
                break
            }
        }

        // プレイヤーが見つかった場合のみファイルを書き換え
        if (playerFound) {
            // ファイルに書き込み
            val writer = FileWriter(playerListFile)
            writer.write(playerList.toString(4))  // インデントをつけて書き込み
            writer.close()
        }
    }
}
