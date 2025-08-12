package com.github.rei0925.maguAdvertisement

import co.aikar.commands.BungeeCommandManager
import com.github.rei0925.maguAdvertisement.listener.PlayerLoginListener
import com.github.rei0925.maguAdvertisement.utils.LanguageManager
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import org.json.JSONArray
import java.io.File
import java.nio.file.Files
import java.util.concurrent.Executors
import java.util.jar.JarFile
import java.util.logging.Level

class MaguAdvertisement : Plugin(), Listener {

    companion object {
        lateinit var instance: MaguAdvertisement
        lateinit var langManager: LanguageManager

        const val EDITION = "公開テスト" // エディションは定数

        val VERSION: String
            get() = instance.description.version // plugin.yml の version と同期
    }

    override fun onEnable() {
        instance = this
        logger.info("This plugin onEnable!")
        val adFile = File("plugins/MaguAdvertisement/ad_list.json")


        // 初回起動時にフォルダと言語ファイルを作成
        setupFiles()

        // 言語ファイル読み込み
        langManager = LanguageManager(File(dataFolder, "lang"))
        logger.info("Language files loaded successfully!")

        val commandManager = BungeeCommandManager(this)
        commandManager.registerCommand(CommandListener())

        proxy.pluginManager.registerListener(this, PlayerLoginListener())

        // 補完候補
        commandManager.commandCompletions.registerAsyncCompletion("bool") { _ ->
            listOf("true", "false")
        }
        commandManager.commandCompletions.registerAsyncCompletion("settings") { _ ->
            listOf("AdBlock+")
        }
        commandManager.commandCompletions.registerAsyncCompletion("AdActiveId") { _ ->
            // ad_list.json のファイルを読み込む
            val adList = JSONArray(adFile.readText())

            // "Active" ステータスの adID をフィルタリングしてリストにする
            val activeAdIds = mutableListOf<String>()
            for (i in 0 until adList.length()) {
                val adObject = adList.getJSONObject(i)
                val status = adObject.optString("status") // "status" フィールドを取得
                if (status == "Active") {
                    val adID = adObject.optString("adID") // "adID" を取得
                    activeAdIds.add(adID) // "Active" の場合リストに追加
                }
            }

            // "Active" ステータスの adID のリストを返す
            activeAdIds
        }
        // 言語ファイルの補完候補を登録
        commandManager.commandCompletions.registerAsyncCompletion("language") { _ ->
            val langFolder = File(dataFolder, "lang")

            if (langFolder.exists() && langFolder.isDirectory) {
                // フォルダ内の `.yml` ファイルを取得し、拡張子を除外してリストに追加
                langFolder.listFiles { file -> file.extension == "yml" }
                    ?.map { it.nameWithoutExtension }
                    ?.toList()
                    ?: listOf("en") // ファイルが存在しない場合のデフォルト候補
            } else {
                listOf("en")
            }
        }
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

        // JAR内の `/lang` フォルダのすべてのファイルをコピー
        copyAllLangFiles(langFolder)

        // `ad_list.json`をコピー
        val adListFile = File(dataFolder, "ad_list.json")
        copyResourceFile("ad_list.json", adListFile)
        // playerリスト
        val playerListFile = File(dataFolder, "player_list.json")
        copyResourceFile("player_list.json", playerListFile)
    }

    /**
     * リソースからファイルをコピー
     */
    private fun copyResourceFile(resourcePath: String, targetFile: File) {
        if (!targetFile.exists()) {
            getResourceAsStream(resourcePath)?.use { inputStream ->
                Files.copy(inputStream, targetFile.toPath())
                logger.info("Copied ${targetFile.name} to ${targetFile.parent}.")
            } ?: logger.warning("Failed to copy ${targetFile.name} (resource not found).")
        }
    }

    /**
     * JAR内の `/lang` フォルダ内のすべてのファイルをコピー
     */
    private fun copyAllLangFiles(targetFolder: File) {
        try {
            // JARファイルのパスを取得
            val jarFile = File(this.javaClass.protectionDomain.codeSource.location.toURI())

            if (!jarFile.isFile) {
                logger.warning("Not running from a JAR file.")
                return
            }

            JarFile(jarFile).use { jar ->
                val entries = jar.entries()

                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement()

                    // `/lang/` フォルダ内のファイルのみコピー
                    if (entry.name.startsWith("lang/") && !entry.isDirectory) {
                        val fileName = entry.name.substringAfterLast("/")
                        val targetFile = File(targetFolder, fileName)

                        if (!targetFile.exists()) {
                            getResourceAsStream(entry.name)?.use { inputStream ->
                                Files.copy(inputStream, targetFile.toPath())
                                logger.info("Copied ${targetFile.name}")
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Failed to copy language files", e)
        }
    }
}
