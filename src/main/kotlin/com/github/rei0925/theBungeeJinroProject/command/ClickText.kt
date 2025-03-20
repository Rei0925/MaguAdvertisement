package com.github.rei0925.theBungeeJinroProject.command

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent

object ClickText {
    /**
     * クリック可能なメニュー項目を作成 (再利用可能)
     */
    fun clickableText(label: String, command: String): TextComponent {
        val component = TextComponent(label)
        component.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
        return component
    }
}