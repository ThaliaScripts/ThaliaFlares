package com.thalia

import com.thalia.config.Config
import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.utils.GuiUtil
import java.util.*

object FlaresCommand : Command("flares") {
    @DefaultHandler
    fun handle() {
        GuiUtil.open(Objects.requireNonNull(Config.gui()))
    }
}