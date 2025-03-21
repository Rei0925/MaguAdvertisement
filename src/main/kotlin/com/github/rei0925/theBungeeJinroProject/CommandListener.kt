package com.github.rei0925.theBungeeJinroProject

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.github.rei0925.theBungeeJinroProject.command.WwMenu
import com.github.rei0925.theBungeeJinroProject.command.WwSetting
import com.github.rei0925.theBungeeJinroProject.room.RoomCommandListener
import net.md_5.bungee.api.CommandSender


@CommandAlias("were-wolf|ww")
@Description("人狼主要コマンド")
class CommandListener : BaseCommand() {
    @Default
    @Subcommand("menu")
    @Description("基本メニュー")
    fun onWwMenu(sender: CommandSender){
        WwMenu.menu(sender)
    }

    @Subcommand("setting")
    @Description("設定メニュー表示")
    @CommandCompletion("@settings @toggle")
    fun onSetting(sender: CommandSender, settings: String, value: String){
        WwSetting.setting(sender,settings,value)
    }

    @Subcommand("room")
    @Description("ルームのメニュー")
    fun onRoom(sender: CommandSender){
        RoomCommandListener.openMenu(sender)
    }
}