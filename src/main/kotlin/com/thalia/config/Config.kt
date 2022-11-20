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
        min = 0,
        max = 7
    )
    var hypeSlot = 0

    @Property(
        type = PropertyType.NUMBER,
        name = "AOTV Slot",
        category = "Main",
        subcategory = "Slots",
        min = 0,
        max = 7
    )
    var aotvSlot = 2

    @Property(
        type = PropertyType.NUMBER,
        name = "Rotation Ticks",
        category = "Main",
        subcategory = "Teleport",
        min = 50,
        max = 2000,
        increment = 50
    )
    var rotationTime = 200

    @Property(
        type = PropertyType.NUMBER,
        name = "Teleport Wait",
        category = "Main",
        subcategory = "Teleport",
        min = 1,
        max = 20
    )
    var teleportWait = 1

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