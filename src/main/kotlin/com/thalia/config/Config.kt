package com.thalia.config

import java.io.File;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType

object Config : Vigilant(File("./config/ThaliaFlares/config.toml")) {

    @Property(
        type = PropertyType.NUMBER,
        name = "Hype Slot",
        category = "Main",
        subcategory = "Slots",
        min = 1,
        max = 8
    )
    var hypeSlot = 1

    @Property(
        type = PropertyType.NUMBER,
        name = "AOTV Slot",
        category = "Main",
        subcategory = "Slots",
        min = 1,
        max = 8
    )
    var aotvSlot = 3

    @Property(
        type = PropertyType.NUMBER,
        name = "Rotation Ticks",
        category = "Main",
        subcategory = "Teleport",
        min = 1,
        max = 20
    )
    var rotationTicks = 10

    @Property(
        type = PropertyType.NUMBER,
        name = "Teleport Wait",
        category = "Main",
        subcategory = "Teleport",
        min = 1,
        max = 20
    )
    var teleportWait = 3

    @Property(
        type = PropertyType.NUMBER,
        name = "Hype Range",
        category = "Main",
        subcategory = "Teleport",
        min = 1,
        max = 10
    )
    var hyperionRange = 8
}