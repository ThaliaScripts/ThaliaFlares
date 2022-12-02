package com.thalia.utils

import com.thalia.mc
import net.minecraft.util.MathHelper

import net.minecraft.util.Vec3


object RotationUtil {
    fun getYawDifference(yaw: Float): Float {
        return MathHelper.wrapAngleTo180_double((yaw - mc.thePlayer.rotationYaw).toDouble()).toFloat()
    }

    fun getPitchDifference(pitch: Float): Float {
        return MathHelper.wrapAngleTo180_double((pitch - mc.thePlayer.rotationPitch).toDouble()).toFloat()
    }

    val yawClosestTo90Degrees: Float
        get() {
            if (mc.thePlayer.rotationYaw > 0.0f) {
                val f: Float = mc.thePlayer.rotationYaw % 90.0f
                return if (f > 45.0f) mc.thePlayer.rotationYaw + 90.0f - f else mc.thePlayer.rotationYaw - f
            }
            val yaw: Float = -mc.thePlayer.rotationYaw
            val i = yaw % 90.0f
            return -if (i > 45.0f) yaw + 90.0f - i else yaw - i
        }

    fun getYawClosestTo90Degrees(yawIn: Float): Float {
        if (yawIn > 0.0f) {
            val f = yawIn % 90.0f
            return if (f > 45.0f) yawIn + 90.0f - f else yawIn - f
        }
        val yaw = -yawIn
        val i = yaw % 90.0f
        return -if (i > 45.0f) yaw + 90.0f - i else yaw - i
    }

    fun getYawDifference(target: Vec3): Float {
        val yaw = MathHelper.atan2(
            target.zCoord - mc.thePlayer.posZ,
            target.xCoord - mc.thePlayer.posX
        ) * 57.29577951308232 - 90.0
        return MathHelper.wrapAngleTo180_double(yaw - mc.thePlayer.rotationYaw).toFloat()
    }

    fun getPitchDifference(target: Vec3): Float {
        val diffX: Double = target.xCoord - mc.thePlayer.posX
        val diffZ: Double = target.zCoord - mc.thePlayer.posZ
        val pitch = -(MathHelper.atan2(
            target.yCoord - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
            MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ).toDouble()
        ) * 57.29577951308232)
        return MathHelper.wrapAngleTo180_double(pitch - mc.thePlayer.rotationPitch).toFloat()
    }
}