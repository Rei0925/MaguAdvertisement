package com.github.rei0925.maguAdvertisement.api

import com.github.rei0925.maguAdvertisement.ad.AdLevel
import com.github.rei0925.maguAdvertisement.ad.AdService
import net.kyori.adventure.text.Component
import java.util.*

class ApiImpl(
    private val adService: AdService  // DIでインターフェースを注入
) : Api {

    override fun createAd(owner: String, ownerUUID: UUID, level: AdLevel, text: Component) {
        // interface経由で呼び出し
        adService.adAdd(
            owner = owner,
            ownerUUID = ownerUUID,
            level = level,
            text = text
        )
    }
}
