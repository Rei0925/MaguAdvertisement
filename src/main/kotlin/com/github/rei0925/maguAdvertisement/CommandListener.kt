package com.github.rei0925.maguAdvertisement

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.github.rei0925.maguAdvertisement.ad.AdCreateImpl
import com.github.rei0925.maguAdvertisement.ad.AdLevel
import com.github.rei0925.maguAdvertisement.api.Api
import com.github.rei0925.maguAdvertisement.api.ApiImpl
import com.github.rei0925.maguAdvertisement.command.AdInfo
import com.github.rei0925.maguAdvertisement.command.AdMenu
import com.github.rei0925.maguAdvertisement.command.AdSetting
import com.github.rei0925.maguAdvertisement.command.AdSystemInfo
import net.kyori.adventure.text.Component
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.time.LocalDateTime

@CommandAlias("advertisement|ad")
@Description("広告主要コマンド")
class CommandListener : BaseCommand() {
    @Default
    @Subcommand("menu")
    @Description("基本メニュー")
    fun onWwMenu(sender: CommandSender){
        AdMenu.menu(sender)
    }

    @Subcommand("setting")
    @Description("設定メニュー表示")
    @CommandCompletion("@settings true|false")
    fun onSetting(sender: CommandSender, settings: String, value: String){
        AdSetting.setting(sender,settings,value)
    }

    @Subcommand("setting language")
    @Description("設定メニュー表示")
    @CommandCompletion("@language")
    fun onLanguage(sender: CommandSender, settings: String){
        AdSetting.setLanguage(sender, settings)
    }

    @Subcommand("add")
    @Description("Add a new advertisement")
    @CommandCompletion("LOW|MIDDLE|HIGH 1day|3day|5day|1week|2week @ad")
    fun onAdd(sender: CommandSender, arg1: String, arg2: String, arg3: String) {
        if (sender is ProxiedPlayer) {  // CommandSender が Player であることを確認
            val level = when (arg1.uppercase()) {
                "LOW" -> AdLevel.LOW
                "MIDDLE" -> AdLevel.MIDDLE
                "HIGH" -> AdLevel.HIGH
                else -> AdLevel.MIDDLE // Default to MEDIUM if invalid level
            }

            val duration = arg2 // e.g., "1day", "3day", "1week"
            val adText = arg3 // The ad text as a string
            val componentText = Component.text(adText)

            // Parse duration to calculate expiry date
            val expiryDate = parseDurationToExpiryDate(duration)

            // Create the AdService and Api instances
            val adService = AdCreateImpl()
            val api: Api = ApiImpl(adService)

            // Create the ad
            api.createAd(
                owner = sender.name,
                ownerUUID = sender.uniqueId, // PlayerのuniqueIdを使用
                level = level,
                text = componentText,
                expiryDate = expiryDate
            )
        } else {
            sender.sendMessage(TextComponent("This command can only be used by players."))
        }
    }

    @Subcommand("info")
    @Description("その広告の情報")
    @CommandCompletion("AdId|@AdActiveId")
    fun adInfo(sender: CommandSender, arg1: Int){
        AdInfo.info(sender, arg1)
    }

    @Subcommand("system info")
    @Description("プラグインの情報")
    @CommandPermission("advertisement.ad_info") // 権限名は任意
    fun adInfoCommand(sender: CommandSender) {
        if (!sender.hasPermission("advertisement.ad_info")) {
            val permissionError = TextComponent("You do not have permission to execute this command.")
            permissionError.color = ChatColor.RED
            sender.sendMessage(permissionError)
            AdSystemInfo.systemInfo(sender)
            return
        }

    }

    // Helper function to parse duration (e.g., "1day", "1week") to LocalDateTime
    private fun parseDurationToExpiryDate(duration: String): LocalDateTime {
        val now = LocalDateTime.now()
        return when {
            duration.endsWith("day") -> now.plusDays(duration.replace("day", "").toLong())
            duration.endsWith("week") -> now.plusWeeks(duration.replace("week", "").toLong())
            else -> now
        }
    }
}