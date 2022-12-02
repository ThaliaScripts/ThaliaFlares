package com.thalia

import com.thalia.config.Config
import com.thalia.utils.RaytraceUtils
import com.thalia.utils.RenderUtils
import com.thalia.utils.RotationUtils
import gg.essential.universal.UChat
import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.Entity
import net.minecraft.entity.monster.EntityCreeper
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.awt.Color

object GhostMacro {
    enum class State {
        NotRunning,
        None,
        LookingForGhost,
        WalkingTowardsGhost,
        HittingGhost
    }

    private var state: State = State.NotRunning
    private var currentGhost: EntityCreeper? = null

    fun toggle() {
        if (state == State.NotRunning) {
            if (mc.currentScreen == null) {
                state = State.None
            }
        }
        else {
            state = State.NotRunning
        }

        UChat.chat("Macro is ${if (running) "§aRunning§f!" else "§cNot Running§f!"}")
    }

    @SubscribeEvent
    fun onTick(tick: TickEvent.ClientTickEvent) {
        if (tick.phase != TickEvent.Phase.END || !running) {
            return
        }

        when (state) {
            State.None -> {
                state = State.LookingForGhost
            }
            State.LookingForGhost -> {
                val potentialGhosts = mc.theWorld.loadedEntityList.filter(this::validGhost)

                currentGhost = potentialGhosts.minByOrNull { mc.thePlayer.getDistance(it.posX, it.posY, it.posZ) } as EntityCreeper?
                state = if (currentGhost == null) {
                    State.None
                }
                else {
                    currentGhost = currentGhost as EntityCreeper
                    State.WalkingTowardsGhost
                }
            }
            State.WalkingTowardsGhost -> {
                if (currentGhost == null || !validGhost(currentGhost!!)) {
                    state = State.LookingForGhost
                    return
                }

                if (mc.objectMouseOver.entityHit != null && validGhost(mc.objectMouseOver.entityHit)) {
                    state = State.HittingGhost
                    return
                }

                val distance = mc.thePlayer.getDistance(currentGhost!!.posX, currentGhost!!.posY, currentGhost!!.posZ)
                val shouldWalkForwards = distance > 2
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.keyCode, shouldWalkForwards)

                if (RotationUtils.done) {
                    if (!aimingAtGhost(currentGhost!!)) {
                        val vec3 = currentGhost!!.positionVector.add(
                            Vec3(0.0,
                                if (currentGhost!!.posY == mc.thePlayer.posY) currentGhost!!.eyeHeight.toDouble() else 0.0,
                                0.0)
                        )

                        val rotation = RotationUtils.getRotation(vec3)
                        RotationUtils.setup(rotation, Config.rotationTime.toLong())
                    }
                }
            }
            State.HittingGhost -> {
                if (currentGhost == null || !validGhost(currentGhost!!) || !aimingAtGhost(currentGhost!!)) {
                    state = State.LookingForGhost
                }
            }
            else -> {
                // a guard statement guards this else
            }
        }
    }

    @SubscribeEvent
    fun onRenderEvent(event: TickEvent.RenderTickEvent) {
        if (!running) {
            return
        }

        if (!RotationUtils.done) {
            RotationUtils.update()
        }
    }

    @SubscribeEvent
    fun on3DRender(event: RenderWorldLastEvent) {
        if (!running) {
            return
        }

        if (currentGhost != null && validGhost(currentGhost!!)) {
            RenderUtils.drawOutlinedEsp(currentGhost!!.entityBoundingBox.expand(0.4, 0.6, 0.4), Color(255,0,0), 2.0f)
        }
    }

    private fun validGhost(entity: Entity): Boolean =
        entity is EntityCreeper && !entity.isDead && entity.health > 30.0 && entity.maxHealth > 30.0 && mc.thePlayer.canEntityBeSeen(entity) && entity.posY - 3 < mc.thePlayer.posY && entity.posX > 111.0

    private fun aimingAtGhost(entity: Entity): Boolean =
        RaytraceUtils.rayTrace(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.getDistance(entity.posX, entity.posY, entity.posZ) + 5.0) == currentGhost

    private val running: Boolean
        get() {
            return state != State.NotRunning && mc.currentScreen == null
        }
}