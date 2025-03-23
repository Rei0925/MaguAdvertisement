package com.github.rei0925.maguAdvertisement.api

import com.github.rei0925.maguAdvertisement.ad.AdLevel
import net.kyori.adventure.text.Component
import java.util.*

interface Api {
    fun createAd(owner: String, ownerUUID: UUID, level: AdLevel, text: Component)
}
