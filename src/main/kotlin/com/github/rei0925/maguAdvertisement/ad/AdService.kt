package com.github.rei0925.maguAdvertisement.ad

import net.kyori.adventure.text.Component
import java.util.*

interface AdService {
    fun adAdd(owner: String, ownerUUID: UUID, level: AdLevel, text: Component)
}
