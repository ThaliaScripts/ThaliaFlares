package com.thalia

import com.thalia.config.Config
import com.thalia.events.PacketReceivedEvent
import com.thalia.mixins.IMinecraft
import com.thalia.utils.RaytraceUtils
import com.thalia.utils.RenderUtils
import com.thalia.utils.Rotation2Utils
import gg.essential.universal.UChat
import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.monster.EntityBlaze
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent
import java.awt.Color
import kotlin.math.sqrt

object Macro {
    enum class MacroState {
        Pause,
        None,
        CantFindBlaze,
        Rotation,
        WaitingOnRotation,
        Teleporting,
        WaitingOnTeleport,
        Hyperion,
        WaitingOnHyperion
    }

    enum class MacroCantFindState {
        None,
        Rotation,
        WaitingOnRotation,
        Teleport,
        WaitingOnTeleport,
    }

    val middle = Vec3(-366.5, 92.0, -806.5)

    var state = MacroState.Pause
        set(value) {
            if (value == MacroState.Teleporting || value == MacroState.Hyperion) {
                teleportCooldown = Config.teleportWait
            }
            field = value
        }
    var cantFindState = MacroCantFindState.None
        set(value) {
            if (value == MacroCantFindState.Teleport) {
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

        if (state != MacroState.CantFindBlaze && (currentBlaze == null || currentBlaze?.isDead == true || currentBlaze?.health == 0.0f)) {
            state = MacroState.None
        }

        when (state) {
            MacroState.None -> {
                scanForFlares()
                currentBlaze = listOfBlazes.minByOrNull { it.getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ) }
                if (currentBlaze == null) {
                    state = MacroState.CantFindBlaze
                    cantFindState = MacroCantFindState.None
                }
                else {
                    state = MacroState.Rotation
                }
            }
            MacroState.CantFindBlaze -> {
                scanForFlares()
                currentBlaze = listOfBlazes.minByOrNull { it.getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ) }

                if (currentBlaze != null) {
                    state = MacroState.Rotation
                }
                else {
                    when (cantFindState) {
                        MacroCantFindState.None -> {
                            cantFindState = MacroCantFindState.Rotation
                        }
                        MacroCantFindState.Rotation -> {
//                            val rotation = Rotation2Utils.getRotation(middle)
//                            Rotation2Utils.setup(rotation, Config.rotationTime.toLong())
                            cantFindState = MacroCantFindState.WaitingOnRotation
                        }
                        MacroCantFindState.Teleport -> {
                            teleportCooldown--

                            if (mc.thePlayer.inventory.currentItem != Config.aotvSlot) {
                                mc.thePlayer.inventory.currentItem = Config.aotvSlot
                                return
                            }

                            if (teleportCooldown < 0) {
                                cantFindState = MacroCantFindState.WaitingOnTeleport
                                (mc as IMinecraft).rightClickMouse()
                            }
                        }
                        else -> {}
                    }
                }
            }
            MacroState.Rotation -> {
                val vec3 = currentBlaze!!.positionVector.add(Vec3(0.0, currentBlaze!!.eyeHeight.toDouble(), 0.0))
//                val rotation = Rotation2Utils.getRotation(vec3)
//                Rotation2Utils.setup(rotation, Config.rotationTime.toLong())
                state = MacroState.WaitingOnRotation
            }
            MacroState.Teleporting -> {

                // idea: add check if player is stuck (behind wall or block) at 3x3 coords and if so jump();
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.keyCode, true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.keyCode, false);
                teleportCooldown--

                if (mc.thePlayer.inventory.currentItem != Config.aotvSlot) {
                    mc.thePlayer.inventory.currentItem = Config.aotvSlot
                    return
                }

                if (teleportCooldown < 0) {
                    state = MacroState.WaitingOnTeleport
                    (mc as IMinecraft).rightClickMouse()
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
    fun onRenderEvent(event: RenderTickEvent) {
        if (state == MacroState.WaitingOnRotation || (state == MacroState.CantFindBlaze && cantFindState == MacroCantFindState.WaitingOnRotation)) {
            if (Rotation2Utils.done) {
                if (state == MacroState.WaitingOnRotation) {
                    teleportAgain(mc.thePlayer.positionVector)
                }
                else {
                    cantFindState = MacroCantFindState.Teleport
                }
                return
            }
            Rotation2Utils.update()
        }
    }

    @SubscribeEvent
    fun on3DRender(event: RenderWorldLastEvent) {
        if(state != MacroState.Pause && currentBlaze != null && currentBlaze?.isDead == false) {
            RenderUtils.drawOutlinedEsp(currentBlaze!!.entityBoundingBox.expand(0.4, 0.6, 0.4), Color(255,0,0), 2.0f)
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
        //UChat.chat("Teleported: distance: $distance")
        if (distance > 15) {
            state = MacroState.Pause
            return
        }

        when (state) {
            MacroState.CantFindBlaze -> {
                if (cantFindState == MacroCantFindState.WaitingOnTeleport) {
                    cantFindState = MacroCantFindState.Teleport
                }
            }
            MacroState.WaitingOnTeleport -> teleportAgain(Vec3(packet.x, packet.y, packet.z))
            MacroState.WaitingOnHyperion -> state = MacroState.None
            else -> {}
        }
    }

    fun teleportAgain(vec3: Vec3) {
        val blazeDistance = toOneDim(vec3.xCoord - currentBlaze!!.posX, vec3.yCoord - currentBlaze!!.posY, vec3.zCoord - currentBlaze!!.posZ)

        if (currentBlaze != null) {
            val res = RaytraceUtils.rayTrace(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, blazeDistance + 5.0)
            if (res != currentBlaze) {
                state = MacroState.Rotation
                return
            }
        }

        state = if (blazeDistance < Config.hyperionRange + 10) {
            MacroState.Hyperion
        } else {
            MacroState.Teleporting
        }
    }

    fun toOneDim(x: Double, y: Double, z: Double): Double =
        sqrt(x * x + y * y + z * z)

    fun scanForFlares() {
        listOfBlazes = mc.theWorld.loadedEntityList
            .filter { it is EntityBlaze && it.name == "Dinnerbone" && mc.thePlayer.canEntityBeSeen(it) && !it.isDead && it.health > 0.0f }
            .map { it as EntityBlaze }
    }
}