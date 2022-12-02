package com.thalia

import com.thalia.config.Config
import com.thalia.features.GiftESP
import com.thalia.utils.NukeUtils
import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.utils.GuiUtil
import gg.essential.universal.UChat
import java.util.*

object FakeCommand : Command("fakerot") {
    @DefaultHandler
    fun handle() {
        NukeUtils.fake = !NukeUtils.fake
        UChat.chat(NukeUtils.fake);
    }
}