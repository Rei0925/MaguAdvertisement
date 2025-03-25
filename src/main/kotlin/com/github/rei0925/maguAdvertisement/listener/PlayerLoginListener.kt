package com.github.rei0925.maguAdvertisement.listener

import com.github.rei0925.maguAdvertisement.MaguAdvertisement
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.nio.file.Files
import java.util.Locale

class PlayerLoginListener : Listener {

    private val playerListFile = File(MaguAdvertisement.instance.dataFolder, "player_list.json")

    @EventHandler
    fun onPostLogin(event: PostLoginEvent) {
        val player = event.player
        val uuid = player.uniqueId.toString()
        val name = player.name

        // プレイヤーのロケールを取得 (nullの場合は英語にフォールバック)
        val locale = player.locale ?: Locale.ENGLISH
        val defaultLang = locale.language.lowercase()  // 言語コード (例: "ja", "en", "fr")

        // ファイルが存在しない場合は新規作成
        if (!playerListFile.exists()) {
            Files.createFile(playerListFile.toPath())
            playerListFile.writeText("[]")  // 空のJSON配列を作成
        }

        // プレイヤーリストを読み込み
        val playerList = JSONArray(playerListFile.readText())

        // 重複チェック
        val exists = playerList.any {
            (it as JSONObject).optString("uuid") == uuid
        }

        // 既に登録済みでなければ追加
        if (!exists) {
            val playerData = JSONObject().apply {
                put("name", name)
                put("uuid", uuid)
                put("language", defaultLang)  // ロケールに基づいた言語
            }

            playerList.put(playerData)

            // ファイルに書き込み
            playerListFile.writeText(playerList.toString(4))  // フォーマット済みで保存
            MaguAdvertisement.instance.logger.info("Registered new player: $name ($uuid) with lang: $defaultLang")
        } else {
            MaguAdvertisement.instance.logger.info("Player already registered: $name ($uuid)")
        }
    }
}
