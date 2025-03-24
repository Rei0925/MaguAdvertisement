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

    private val adFile = File("plugins/MaguAdvertisement/ad_list.json")

    init {
        if (!adFile.exists()) {
            adFile.createNewFile()
            adFile.writeText("[]")
        }
    }

    override fun adAdd(owner: String, ownerUUID: UUID, level: AdLevel, text: Component, expiryDate: LocalDateTime) {
        val serializedMessage = LegacyComponentSerializer.legacyAmpersand().serialize(text)
        val expiryDateString = expiryDate.format(DateTimeFormatter.ISO_DATE_TIME)

        // 既存の広告リストを取得
        val adList = if (adFile.exists() && adFile.readText().isNotEmpty()) {
            JSONArray(adFile.readText())
        } else {
            JSONArray()
        }

        // 現在の最大IDを取得し、1プラス
        val maxId = (0 until adList.length())
            .mapNotNull { idx -> adList.optJSONObject(idx)?.optInt("adID") }
            .maxOrNull() ?: 0
        val newId = maxId + 1

        // 新しい広告オブジェクト
        val adObject = JSONObject().apply {
            put("adID", newId)  // 自動インクリメントされたIDを割り当て
            put("owner", owner)
            put("ownerUUID", ownerUUID.toString())
            put("level", level.name)
            put("status", "Active")
            put("message", serializedMessage)
            put("timestamp", System.currentTimeMillis())
            put("expiryDate", expiryDateString)
        }

        // 新広告を追加
        adList.put(adObject)

        // JSONファイルに保存
        adFile.writeText(adList.toString(4))  // インデント付きで保存
    }
}
