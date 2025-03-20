package com.github.rei0925.theBungeeJinroProject

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.github.rei0925.theBungeeJinroProject.Command.WwMenu
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
}