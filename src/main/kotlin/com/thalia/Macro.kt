package com.thalia

import net.minecraft.entity.monster.EntityBlaze
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object Macro {
    enum class MacroState {
        Rotation,
        Teleporting,
        WaitingOnTeleport,
        Hyperion,
        WaitingOnHyperion
    }

    var listOfBlazes: List<EntityBlaze> = listOf()
    var currentBlaze: EntityBlaze? = null
    var hypeClicked: Boolean = false

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.END) {
            return
        }

        if (currentBlaze == null || currentBlaze?.isDead == true || hypeClicked) {
            scanForFlares()
            val closest = listOfBlazes.minByOrNull { it.getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ) }
            hypeClicked = false
        }

    }

    fun scanForFlares() {
        listOfBlazes = mc.theWorld.loadedEntityList
            .filter { it is EntityBlaze && it.name == "Dinnerbone" && mc.thePlayer.canEntityBeSeen(it) }
            .map { it as EntityBlaze }
    }
}