package com.github.rei0925.maguAdvertisement

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.github.rei0925.maguAdvertisement.command.AdMenu
import com.github.rei0925.maguAdvertisement.command.AdSetting
import net.md_5.bungee.api.CommandSender


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
    @CommandCompletion("@settings @toggle")
    fun onSetting(sender: CommandSender, settings: String, value: String){
        AdSetting.setting(sender,settings,value)
    }
}