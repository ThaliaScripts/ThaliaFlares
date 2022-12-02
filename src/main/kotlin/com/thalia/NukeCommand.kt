package com.thalia

import com.thalia.config.Config
import com.thalia.features.GiftESP
import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.utils.GuiUtil
import gg.essential.universal.UChat
import java.util.*

object NukeCommand : Command("GiftNuker") {
    @DefaultHandler
    fun handle() {
        GiftESP.enabled = !GiftESP.enabled
        UChat.chat(GiftESP.enabled);
    }
}