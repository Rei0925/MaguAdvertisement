package com.github.rei0925.maguAdvertisement.ad

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import java.util.*

class AdCreateImpl : AdService {

    // オーバーライドで実装
    override fun adAdd(owner: String, ownerUUID: UUID, level: AdLevel, text: Component) {
        val adMessage = Component.text("ADを保存しました: ").append(text)

        // printで表示（レガシー形式で表示）
        println(LegacyComponentSerializer.legacyAmpersand().serialize(adMessage))
    }
}
