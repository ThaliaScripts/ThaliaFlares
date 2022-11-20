package com.thalia

import com.thalia.config.Config
import com.thalia.events.PacketReceivedEvent
import com.thalia.mixins.IMinecraft
import com.thalia.utils.RotationUtils
import net.minecraft.entity.monster.EntityBlaze
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import kotlin.math.sqrt

object Macro {
    enum class MacroState {
        Pause,
        None,
        Rotation,
        Teleporting,
        WaitingOnTeleport,
        Hyperion,
        WaitingOnHyperion
    }

    var state = MacroState.Pause
        set(value) {
            if (value == MacroState.Teleporting || value == MacroState.Hyperion) {
                teleportCooldown = Config.teleportWait
            }
            field = value
        }
    var listOfBlazes: List<EntityBlaze> = listOf()
    var currentBlaze: EntityBlaze? = null
    var teleportCooldown = 0

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.END) {
            return
        }

        if (mc.currentScreen != null || state == MacroState.Pause) {
            return
        }

        logger.info("Current State: ${state.name}")

        if (currentBlaze == null || currentBlaze?.isDead == true) {
            state = MacroState.None
        }

        when (state) {
            MacroState.None -> {
                scanForFlares()
                currentBlaze = listOfBlazes.minByOrNull { it.getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ) }
                state = MacroState.Rotation
            }
            MacroState.Rotation -> {
                if (currentBlaze == null) {
                    state = MacroState.None
                    return
                }

                RotationUtils.smoothLook(RotationUtils.getRotationToEntity(currentBlaze!!), Config.rotationTicks ) {
                    state = MacroState.Teleporting
                }
            }
            MacroState.Teleporting -> {
                teleportCooldown--

                if (mc.thePlayer.inventory.currentItem != Config.aotvSlot) {
                    mc.thePlayer.inventory.currentItem = Config.aotvSlot - 1
                    return
                }

                if (teleportCooldown < 0) {
                    (mc as IMinecraft).rightClickMouse()
                    state = MacroState.WaitingOnTeleport
                }
            }
            MacroState.Hyperion -> {
                teleportCooldown--

                if (mc.thePlayer.inventory.currentItem != Config.hypeSlot) {
                    mc.thePlayer.inventory.currentItem = Config.hypeSlot
                    return
                }

                if (teleportCooldown < 0) {
                    (mc as IMinecraft).rightClickMouse()
                    state = MacroState.WaitingOnHyperion
                }
            }
            else -> {}
        }
    }

    @SubscribeEvent
    fun onReceivePacket(event: PacketReceivedEvent) {
        if (event.packet !is S08PacketPlayerPosLook) {
            return
        }

        val packet = event.packet as S08PacketPlayerPosLook
        val diffX = packet.x - mc.thePlayer.posX
        val diffY = packet.y - mc.thePlayer.posY
        val diffZ = packet.z - mc.thePlayer.posZ

        val distance = toOneDim(diffX, diffY, diffZ)
        if (distance > 15) {
            state = MacroState.Pause
            return
        }

        when (state) {
            MacroState.WaitingOnTeleport -> {
                val blazeDistance = toOneDim(packet.x - currentBlaze!!.posX, packet.y - currentBlaze!!.posY, packet.z - currentBlaze!!.posZ)

                state = if (blazeDistance < Config.hyperionRange + 10) {
                    MacroState.Hyperion
                }
                else {
                    MacroState.Teleporting
                }
            }
            MacroState.WaitingOnHyperion -> state = MacroState.None
            else -> {}
        }
    }

    fun toOneDim(x: Double, y: Double, z: Double): Double =
        sqrt(x * x + y * y + z * z)

    fun scanForFlares() {
        listOfBlazes = mc.theWorld.loadedEntityList
            .filter { it is EntityBlaze && it.name == "Dinnerbone" && mc.thePlayer.canEntityBeSeen(it) }
            .map { it as EntityBlaze }
    }
}