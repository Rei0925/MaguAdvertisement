package com.github.rei0925.theBungeeJinroProject

import co.aikar.commands.BungeeCommandManager
import com.github.rei0925.theBungeeJinroProject.utils.LanguageManager
import net.md_5.bungee.api.plugin.Plugin
import java.io.File
import java.nio.file.Files

class TheBungeeJinroProject : Plugin() {

    companion object {
        lateinit var instance: TheBungeeJinroProject
        lateinit var langManager: LanguageManager
    }

    override fun onEnable() {
        instance = this
        logger.info("This plugin onEnable!")

        // 初回起動時にフォルダと言語ファイルを作成
        setupLanguageFiles()

        // 言語ファイル読み込み
        langManager = LanguageManager(File(dataFolder, "lang"))
        logger.info("Language files loaded successfully!")

        val commandManager = BungeeCommandManager(this)
        commandManager.registerCommand(CommandListener())
    }

    override fun onDisable() {
        logger.info("This plugin onDisable!")
    }

    /**
     * 初回起動時に言語フォルダとファイルを作成
     */
    private fun setupLanguageFiles() {
        val langFolder = File(dataFolder, "lang")
        if (!langFolder.exists()) {
            langFolder.mkdirs()
        }

        // リソースから言語ファイルをコピー
        listOf("en.yml", "ja.yml").forEach { langFile ->
            val targetFile = File(langFolder, langFile)

            if (!targetFile.exists()) {
                val resourcePath = "/lang/$langFile"
                val inputStream = getResourceAsStream(resourcePath)
                if (inputStream != null) {
                    Files.copy(inputStream, targetFile.toPath())
                    logger.info("Copied $langFile to lang folder.")
                } else {
                    logger.warning("Failed to copy $langFile (resource not found).")
                }
            }
        }
    }
}
