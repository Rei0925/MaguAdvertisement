package com.github.rei0925.maguAdvertisement.ad

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AdCreateImpl : AdService {

    // ファイルパス
    private val adFile = File("plugins/MaguAdvertisement/ad_list.json")

    // 初回起動時にJSONファイルが存在しない場合に作成
    init {
        if (!adFile.exists()) {
            adFile.createNewFile()
            adFile.writeText("[]") // 空のJSONArrayで初期化
        }
    }

    // オーバーライドで実装
    override fun adAdd(owner: String, ownerUUID: UUID, level: AdLevel, text: Component, expiryDate: LocalDateTime) {
        val adMessage = Component.text("ADを保存しました: ").append(text)
        val serializedMessage = LegacyComponentSerializer.legacyAmpersand().serialize(adMessage)

        // expiryDate を ISO 8601 形式で文字列化
        val expiryDateString = expiryDate.format(DateTimeFormatter.ISO_DATE_TIME)

        // JSONオブジェクトの作成
        val adObject = JSONObject().apply {
            put("owner", owner)
            put("ownerUUID", ownerUUID.toString())  // UUIDを文字列として保存
            put("level", level.name)
            put("message", serializedMessage)
            put("timestamp", System.currentTimeMillis()) // 保存日時
            put("expiryDate", expiryDateString)  // expiryDate を文字列として保存
        }

        // 既存の広告リストを読み込み
        val adList = JSONArray(adFile.readText())

        // 新しい広告を追加
        adList.put(adObject)

        // 更新されたリストをファイルに保存
        adFile.writeText(adList.toString(4)) // インデント付きで保存
    }
}
