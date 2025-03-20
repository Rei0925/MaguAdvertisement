package com.github.rei0925.theBungeeJinroProject.utils

import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import java.io.File
import java.util.*

class LanguageManager(private val langFolder: File) {

    private val languageData = mutableMapOf<String, Configuration>()

    init {
        loadLanguages()
    }

    // 言語ファイルをロード
    private fun loadLanguages() {
        langFolder.listFiles { file -> file.extension == "yml" }?.forEach { file ->
            val langCode = file.nameWithoutExtension.lowercase(Locale.getDefault())
            val config = ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(file)
            languageData[langCode] = config
        }
    }

    // プレイヤーごとの言語を取得
    fun getMessage(locale: Locale, path: String, default: String = "N/A"): String {
        val langCode = locale.language
        val config = languageData[langCode] ?: languageData["en"] ?: return default
        return config.getString(path, default)
    }
}
