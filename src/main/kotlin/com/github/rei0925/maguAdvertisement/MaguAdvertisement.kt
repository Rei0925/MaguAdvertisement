package com.github.rei0925.maguAdvertisement

import co.aikar.commands.BungeeCommandManager
import com.github.rei0925.maguAdvertisement.utils.LanguageManager
import net.md_5.bungee.api.plugin.Plugin
import java.io.File
import java.nio.file.Files

class MaguAdvertisement : Plugin() {

    companion object {
        lateinit var instance: MaguAdvertisement
        lateinit var langManager: LanguageManager
    }

    override fun onEnable() {
        instance = this
        logger.info("This plugin onEnable!")

        // 初回起動時にフォルダと言語ファイルを作成
        setupFiles()

        // 言語ファイル読み込み
        langManager = LanguageManager(File(dataFolder, "lang"))
        logger.info("Language files loaded successfully!")

        val commandManager = BungeeCommandManager(this)
        commandManager.registerCommand(CommandListener())

        // boolean補完候補
        commandManager.commandCompletions.registerAsyncCompletion("bool") { _ ->
            listOf("true", "false")
        }
        // 設定補完候補
        commandManager.commandCompletions.registerAsyncCompletion("settings") { _ ->
            listOf("language")
        }
        //AD補完候補
        commandManager.commandCompletions.registerAsyncCompletion("ad") { _ ->
            listOf("Enter the text to add to the advertisement...")
        }
    }

    override fun onDisable() {
        logger.info("This plugin onDisable!")
    }

    /**
     * 初回起動時に言語フォルダとファイルを作成
     * ad_list.jsonをコピー
     */
    private fun setupFiles() {
        val langFolder = File(dataFolder, "lang")
        if (!langFolder.exists()) {
            langFolder.mkdirs()
        }

        // 言語ファイルをコピー
        listOf("en.yml", "ja.yml").forEach { langFile ->
            copyResourceFile("/lang/$langFile", File(langFolder, langFile))
        }

        // `ad_list.json`をコピー
        val adListFile = File(dataFolder, "ad_list.json")
        copyResourceFile("/ad_list.json", adListFile)
    }

    /**
     * リソースからファイルをコピー
     */
    private fun copyResourceFile(resourcePath: String, targetFile: File) {
        if (!targetFile.exists()) {
            val inputStream = getResourceAsStream(resourcePath)
            if (inputStream != null) {
                Files.copy(inputStream, targetFile.toPath())
                logger.info("Copied ${targetFile.name} to ${targetFile.parent}.")
            } else {
                logger.warning("Failed to copy ${targetFile.name} (resource not found).")
            }
        }
    }
}
