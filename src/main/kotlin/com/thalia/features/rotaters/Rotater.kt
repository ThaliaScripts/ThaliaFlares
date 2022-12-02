package com.thalia.features.rotaters

import com.thalia.ThaliaFlares
import com.thalia.mc
import com.thalia.utils.MathUtil
import com.thalia.utils.RotationUtil
import com.thalia.utils.VecUtil
import net.minecraft.util.BlockPos
import net.minecraft.util.MathHelper
import net.minecraft.util.Vec3


class Rotater : IRotater {
    protected var divider = 190.0f
    protected var changedDivider = 0f
    protected var timeElapsed: Long = 0
    protected var yaw: Float
    protected var pitch: Float
    protected var changedYaw = 0f
    protected var changedPitch = 0f
    protected var startYaw: Float
    protected var startPitch: Float
    protected var goalYaw: Float
    protected var goalPitch: Float

    constructor(target: Vec3) {
        val diffX: Double = target.xCoord - mc.thePlayer.posX
        val diffY: Double = target.yCoord - mc.thePlayer.posY + VecUtil.fastEyeHeight()
        val diffZ: Double = target.zCoord - mc.thePlayer.posZ
        yaw = MathHelper.wrapAngleTo180_double(
            MathHelper.atan2(diffZ, diffX) * 57.29577951308232 - 90.0 - mc.thePlayer.rotationYaw
        ).toFloat()
        pitch = MathHelper.wrapAngleTo180_double(
            -(MathHelper.atan2(
                diffY,
                MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ).toDouble()
            ) * 57.29577951308232) - mc.thePlayer.rotationPitch
        ).toFloat()
        startPitch = mc.thePlayer.rotationPitch
        startYaw = mc.thePlayer.rotationYaw
        goalYaw = yaw + mc.thePlayer.rotationYaw
        goalPitch = pitch + mc.thePlayer.rotationPitch
    }

    constructor(pos: BlockPos) {
        val target = Vec3(pos.x + 0.5, mc.thePlayer.posY + 1.6200000047683716, pos.z + 0.5)
        val diffX: Double = target.xCoord - mc.thePlayer.posX
        val diffY: Double = target.yCoord - mc.thePlayer.posY + VecUtil.fastEyeHeight()
        val diffZ: Double = target.zCoord - mc.thePlayer.posZ
        yaw = RotationUtil.getYawClosestTo90Degrees(
            MathHelper.wrapAngleTo180_double(
                MathHelper.atan2(diffZ, diffX) * 57.29577951308232 - 90.0 - mc.thePlayer.rotationYaw
            ).toFloat()
        )
        pitch = MathHelper.wrapAngleTo180_double(
            -(MathHelper.atan2(
                diffY,
                MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ).toDouble()
            ) * 57.29577951308232) - mc.thePlayer.rotationPitch
        ).toFloat()
        startPitch = mc.thePlayer.rotationPitch
        startYaw = mc.thePlayer.rotationYaw
        goalYaw = yaw + mc.thePlayer.rotationYaw
        goalPitch = pitch + mc.thePlayer.rotationPitch
    }

    constructor(yawIn: Float, pitchIn: Float) {
        yaw = yawIn
        pitch = pitchIn
        startPitch = mc.thePlayer.rotationPitch
        startYaw = mc.thePlayer.rotationYaw
        goalYaw = yaw + mc.thePlayer.rotationYaw
        goalPitch = pitch + mc.thePlayer.rotationPitch
    }

    fun setPitch(pitch: Float): Rotater {
        this.pitch = pitch
        goalPitch = pitch + mc.thePlayer.rotationPitch
        return this
    }

    fun setYaw(yaw: Float): Rotater {
        this.yaw = yaw
        goalYaw = yaw + mc.thePlayer.rotationYaw
        return this
    }

    fun addPitch(pitch: Float): Rotater {
        this.pitch += pitch
        goalPitch += pitch
        return this
    }

    fun addYaw(yaw: Float): Rotater {
        this.yaw += yaw
        goalYaw += yaw
        return this
    }

    fun randomPitch(): Rotater {
        pitch = MathUtil.randomFloat()
        goalPitch = pitch + mc.thePlayer.rotationPitch
        return this
    }

    fun randomYaw(): Rotater {
        yaw = MathUtil.randomFloat()
        goalYaw = yaw + mc.thePlayer.rotationYaw
        return this
    }

    fun setRotationAmount(rotation: Int): Rotater {
        divider = (rotation * 10).toFloat()
        return this
    }

    override fun rotate() {
        rotating = true
        changedDivider = divider
        yaw /= divider
        pitch /= divider
        changedPitch = pitch
        changedYaw = yaw
        ThaliaFlares.rotater = this
        timeElapsed = System.currentTimeMillis()
    }

    override fun add() {
        val elapsed = (System.currentTimeMillis() - timeElapsed).toFloat()
        if (elapsed >= changedDivider) {
            mc.thePlayer.rotationYaw = goalYaw
            mc.thePlayer.rotationPitch = MathUtil.clampPitch(goalPitch)
            ThaliaFlares.rotater = null
            return
        }
        if (elapsed > 80.0f) {
            val progress = 1.0f + (elapsed - 80.0f) / changedDivider
            val diff = progress * progress / 1.8f
            changedYaw = yaw / diff
            changedPitch = pitch / diff
            changedDivider = divider * diff
        }
        mc.thePlayer.rotationYaw = startYaw + changedYaw * elapsed
        mc.thePlayer.rotationPitch = MathUtil.clampPitch(startPitch + changedPitch * elapsed)
    }

    fun addRandom(): Rotater {
        yaw += MathUtil.randomFloat()
        pitch += MathUtil.randomFloat()
        goalYaw = yaw + mc.thePlayer.rotationYaw
        goalPitch = pitch + mc.thePlayer.rotationPitch
        return this
    }

    fun addRandomRotateAmount(): Rotater {
        divider += MathUtil.floor(ThaliaFlares.random.nextGaussian() * 8.0)
        return this
    }

    fun addSlightRandomRotateAmount(): Rotater {
        divider += MathUtil.floor(ThaliaFlares.random.nextGaussian() * 5.0)
        return this
    }

    fun addBigRandomRotateAmount(): Rotater {
        divider += MathUtil.floor(ThaliaFlares.random.nextGaussian() * 13.0)
        return this
    }

    fun addRandomRotateAmount(multiplier: Float): Rotater {
        divider += MathUtil.floor(ThaliaFlares.random.nextGaussian() * multiplier)
        return this
    }

    fun randomYaw(min: Float, rand: Float, positive: Boolean): Rotater {
        yaw += if (positive) MathUtil.positiveFloat() * rand + min else MathUtil.negativeFloat() * rand - min
        goalYaw = yaw + mc.thePlayer.rotationYaw
        return this
    }

    fun randomPitch(min: Float, rand: Float, positive: Boolean): Rotater {
        val nextPitch: Float =
            if (positive) MathUtil.positiveFloat() * rand + min else MathUtil.negativeFloat() * rand - min + pitch
        val nextGoalPitch: Float = nextPitch + mc.thePlayer.rotationPitch
        if (nextGoalPitch > 90.0f || nextGoalPitch < -90.0f) return randomPitch(min, rand, !positive)
        pitch += nextPitch
        goalPitch = nextGoalPitch
        return this
    }

    fun randomYaw(min: Float, rand: Float): Rotater {
        yaw += if (ThaliaFlares.random.nextBoolean()) MathUtil.positiveFloat() * rand + min else MathUtil.negativeFloat() * rand - min
        goalYaw = yaw + mc.thePlayer.rotationYaw
        return this
    }

    fun randomPitch(min: Float, rand: Float): Rotater {
        pitch += if (ThaliaFlares.random.nextBoolean()) MathUtil.positiveFloat() * rand + min else MathUtil.negativeFloat() * rand - min
        goalPitch = pitch + mc.thePlayer.rotationPitch
        return this
    }

    fun antiSus(amt: Float): Rotater {
        divider =
            (MathUtil.round((MathUtil.abs(yaw) + MathUtil.abs(pitch) + MathUtil.randomFloat()) / 8.0f) + amt + 2.0f) * 10.0f
        return this
    }

    fun antiSus(divider: Float, amt: Float): Rotater {
        this.divider =
            (MathUtil.round((MathUtil.abs(yaw) + MathUtil.abs(pitch) + MathUtil.randomFloat()) / divider) + amt + 2.0f) * 10.0f
        return this
    }

    fun antiSus(): Rotater {
        divider =
            ((MathUtil.round((MathUtil.abs(yaw) + MathUtil.abs(pitch) + MathUtil.randomFloat()) / 8.0f) + 22) * 10).toFloat()
        return this
    }

    companion object {
        @kotlin.jvm.JvmField
        var rotating = false
        fun rotateTo(yaw: Float, pitch: Float): Rotater {
            return Rotater(
                yaw - mc.thePlayer.rotationYaw,
                MathUtil.clampPitch(pitch - mc.thePlayer.rotationPitch)
            )
        }
    }
}
