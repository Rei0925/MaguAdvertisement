package com.github.rei0925.maguAdvertisement.command

import com.github.rei0925.maguAdvertisement.MaguAdvertisement
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender

object AdSystemInfo {
    fun systemInfo(sender: CommandSender) {
        val title = TextComponent("\n###システム情報###")
        title.color = ChatColor.GREEN

        val version = TextComponent("\nVersion: ${MaguAdvertisement.VERSION}")
        version.color = ChatColor.YELLOW

        val edition = TextComponent("\nEdition: ${MaguAdvertisement.EDITION}")
        edition.color = ChatColor.YELLOW

        val creator = TextComponent("\nCreator: MaguPlugin")
        creator.color = ChatColor.YELLOW

        // まとめて送るために配列にする
        sender.sendMessage(title, version, edition, creator)
    }
}