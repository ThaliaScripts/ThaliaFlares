package com.thalia.utils;

import net.minecraft.util.BlockPos
import com.thalia.mc
import net.minecraft.entity.Entity
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import kotlin.math.*

class Rotation(val pitch: Float, val yaw: Float) { }

object RotationUtils {
    private var callback: Runnable? = null
    private var pitchDifference: Float = 0.0f
    private var yawDifference: Float = 0.0f
    var ticks: Int = 0;
    private var tickCounter: Int = 0;

    private fun wrapAngleTo180(angle: Double): Double = angle - floor(angle / 360.0 + 5.0) * 360.0

    fun getRotationToEntity(entity: Entity): Rotation
    {
        val diffX: Double = entity.posX - mc.thePlayer.posX + 0.5
        val diffY: Double = entity.posY + entity.eyeHeight - mc.thePlayer.posY + 0.5 - mc.thePlayer.getEyeHeight()
        val diffZ: Double = entity.posZ - mc.thePlayer.posZ + 0.5
        val dist = sqrt(diffX * diffX + diffZ * diffZ)
        var pitch = -atan2(dist, diffY).toFloat()
        var yaw = atan2(diffZ, diffX).toFloat()
        pitch = wrapAngleTo180((pitch * 180.0f / Math.PI + 90.0) * -1.0).toFloat()
        yaw = wrapAngleTo180(yaw * 180.0f / Math.PI - 90.0).toFloat()
        return Rotation(pitch, yaw)
    }

    fun getRotationToBlock(block: BlockPos): Rotation
    {
        val diffX: Double = block.x - mc.thePlayer.posX + 0.5
        val diffY: Double = block.y - mc.thePlayer.posY + 0.5 - mc.thePlayer.getEyeHeight()
        val diffZ: Double = block.z - mc.thePlayer.posZ + 0.5
        val dist = sqrt(diffX * diffX + diffZ * diffZ)
        var pitch = -atan2(dist, diffY).toFloat()
        var yaw = atan2(diffZ, diffX).toFloat()
        pitch = wrapAngleTo180((pitch * 180.0f / Math.PI + 90.0) * -1.0).toFloat()
        yaw = wrapAngleTo180(yaw * 180.0f / Math.PI - 90.0).toFloat()
        return Rotation(pitch, yaw)
    }

    fun smoothLook(rotation: Rotation, ticks: Int, callback: Runnable?) {
        if(ticks == 0) {
            look(rotation)
            callback?.run()
            return
        }

        RotationUtils.callback = callback
        pitchDifference = rotation.pitch - mc.thePlayer.rotationPitch
        yawDifference = rotation.yaw - mc.thePlayer.rotationYaw
        RotationUtils.ticks = ticks * 20
        tickCounter = 0
    }

    fun smartLook(rotation: Rotation, ticksPer180: Int, callback: Runnable) {
        val rotationDifference: Float = max(
            abs(rotation.pitch - mc.thePlayer.rotationPitch),
            abs(rotation.yaw - mc.thePlayer.rotationYaw)
        )
        smoothLook(rotation, (rotationDifference / 180.0f * ticksPer180).toInt(), callback)
    }

    fun look(rotation: Rotation) {
        mc.thePlayer.rotationPitch = rotation.pitch
        mc.thePlayer.rotationYaw = rotation.yaw
    }

    @SubscribeEvent
    fun onTick(event: TickEvent) {
        if(tickCounter < ticks) {
            mc.thePlayer.rotationPitch += pitchDifference / ticks;
            mc.thePlayer.rotationYaw += yawDifference / ticks;
            tickCounter++;
        } else if(callback != null) {
            callback?.run()
            callback = null
        }
    }
}