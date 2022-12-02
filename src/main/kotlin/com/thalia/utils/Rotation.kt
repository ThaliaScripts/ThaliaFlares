package com.thalia.utils

import com.thalia.mc
import net.minecraft.util.MathHelper

import net.minecraft.util.Vec3
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Rotation(val yaw: Float, val pitch: Float) {
    fun max(): Float {
        return max(abs(yaw), abs(pitch))
    }

    fun min(): Float {
        return min(abs(yaw), abs(pitch))
    }

    fun sum(): Float {
        return abs(yaw) + abs(pitch)
    }

    val look: Vec3
        get() {
            val f2 = -MathHelper.cos(-pitch * 0.017453292f)
            return Vec3(
                (MathHelper.sin(-yaw * 0.017453292f - 3.1415927f) * f2).toDouble(),
                MathHelper.sin(-pitch * 0.017453292f).toDouble(),
                (MathHelper.cos(-yaw * 0.017453292f - 3.1415927f) * f2).toDouble()
            )
        }

    override fun toString(): String {
        return "{yaw=" + yaw + ", pitch=" + pitch + "}"
    }

    companion object {
        fun getRotationDifference(target: Vec3): Rotation {
            val diffX: Double = target.xCoord - mc.thePlayer.posX
            val diffY: Double = target.yCoord - mc.thePlayer.posY + VecUtil.fastEyeHeight()
            val diffZ: Double = target.zCoord - mc.thePlayer.posZ
            val dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ).toDouble()
            return Rotation(
                MathHelper.wrapAngleTo180_double(
                    MathHelper.atan2(
                        diffZ,
                        diffX
                    ) * 57.29577951308232 - 90.0 - mc.thePlayer.rotationYaw
                ).toFloat(),
                MathHelper.wrapAngleTo180_double(
                    -(MathHelper.atan2(
                        diffY,
                        dist
                    ) * 57.29577951308232) - mc.thePlayer.rotationPitch
                ).toFloat()
            )
        }

        fun getRotationDifference(target: Vec3, playerYaw: Float, playerPitch: Float): Rotation {
            val diffX: Double = target.xCoord - mc.thePlayer.posX
            val diffY: Double = target.yCoord - mc.thePlayer.posY + VecUtil.fastEyeHeight()
            val diffZ: Double = target.zCoord - mc.thePlayer.posZ
            val dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ).toDouble()
            return Rotation(
                MathHelper.wrapAngleTo180_double(
                    MathHelper.atan2(
                        diffZ,
                        diffX
                    ) * 57.29577951308232 - 90.0 - playerYaw
                ).toFloat(),
                MathHelper.wrapAngleTo180_double(-(MathHelper.atan2(diffY, dist) * 57.29577951308232) - playerPitch)
                    .toFloat()
            )
        }

        fun getRotation(target: Vec3): Rotation {
            val diffX: Double = target.xCoord - mc.thePlayer.posX
            val diffY: Double = target.yCoord - mc.thePlayer.posY + VecUtil.fastEyeHeight()
            val diffZ: Double = target.zCoord - mc.thePlayer.posZ
            val dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ).toDouble()
            return Rotation(
                MathHelper.wrapAngleTo180_double(
                    MathHelper.atan2(
                        diffZ,
                        diffX
                    ) * 57.29577951308232 - 90.0 - mc.thePlayer.rotationYaw
                ).toFloat() + mc.thePlayer.rotationYaw,
                MathHelper.wrapAngleTo180_double(
                    -(MathHelper.atan2(
                        diffY,
                        dist
                    ) * 57.29577951308232) - mc.thePlayer.rotationPitch
                ).toFloat() + mc.thePlayer.rotationPitch
            )
        }

        fun getRotation(position: Vec3, target: Vec3): Rotation {
            val diffX = target.xCoord - position.xCoord
            val diffY = target.yCoord - position.yCoord
            val diffZ = target.zCoord - position.zCoord
            val dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ).toDouble()
            return Rotation(
                MathHelper.wrapAngleTo180_double(
                    MathHelper.atan2(
                        diffZ,
                        diffX
                    ) * 57.29577951308232 - 90.0 - mc.thePlayer.rotationYaw
                ).toFloat() + mc.thePlayer.rotationYaw,
                MathHelper.wrapAngleTo180_double(
                    -(MathHelper.atan2(
                        diffY,
                        dist
                    ) * 57.29577951308232) - mc.thePlayer.rotationPitch
                ).toFloat() + mc.thePlayer.rotationPitch
            )
        }

        fun getYaw(target: Vec3): Float {
            return MathHelper.wrapAngleTo180_double(
                MathHelper.atan2(
                    target.zCoord - mc.thePlayer.posZ,
                    target.xCoord - mc.thePlayer.posX
                ) * 57.29577951308232 - 90.0 - mc.thePlayer.rotationYaw
            ).toFloat() + mc.thePlayer.rotationYaw
        }

        fun getYawDifference(target: Vec3): Float {
            return MathHelper.wrapAngleTo180_double(
                MathHelper.atan2(
                    target.zCoord - mc.thePlayer.posZ,
                    target.xCoord - mc.thePlayer.posX
                ) * 57.29577951308232 - 90.0 - mc.thePlayer.rotationYaw
            ).toFloat()
        }
    }
}
