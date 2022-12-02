package com.thalia.utils;

import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.util.*
import org.lwjgl.util.vector.Vector3f
import kotlin.math.*

class Rotation2(var yaw: Float, var pitch: Float) {

    fun addYaw(yaw: Float) {
        this.yaw += yaw
    }

    fun addPitch(pitch: Float) {
        this.pitch += pitch
    }

    val value: Float
        get() = abs(yaw) + abs(pitch)

    override fun toString(): String {
        return "Rotation2{yaw=$yaw, pitch=$pitch}"
    }
}

object Rotation2Utils {
    private val mc = Minecraft.getMinecraft()
    var startRot: Rotation2? = null
    var neededChange: Rotation2? = null
    var endRot: Rotation2? = null
    var startTime: Long = 0
    var endTime: Long = 0
    var done = true
    private val BLOCK_SIDES = arrayOf(
        floatArrayOf(0.5f, 0.01f, 0.5f),
        floatArrayOf(0.5f, 0.99f, 0.5f),
        floatArrayOf(0.01f, 0.5f, 0.5f),
        floatArrayOf(0.99f, 0.5f, 0.5f),
        floatArrayOf(0.5f, 0.5f, 0.01f),
        floatArrayOf(0.5f, 0.5f, 0.99f)
    )

    fun getRotation2(vec: Vec3): Rotation2 {
        val eyes = mc.thePlayer.getPositionEyes(1.0f)
        return getRotation2(eyes, vec)
    }

    fun getRotation2(from: Vec3, to: Vec3): Rotation2 {
        val diffX = to.xCoord - from.xCoord
        val diffY = to.yCoord - from.yCoord
        val diffZ = to.zCoord - from.zCoord
        return Rotation2(
            MathHelper.wrapAngleTo180_float((Math.toDegrees(atan2(diffZ, diffX)) - 90.0).toFloat())
                , -Math.toDegrees(atan2(diffY, sqrt(diffX * diffX + diffZ * diffZ))).toFloat()
        )
    }

    fun getRotation2(bp: BlockPos): Rotation2 {
        val vec = Vec3(bp.x + 0.5, bp.y + 0.5, bp.z + 0.5)
        return getRotation2(vec)
    }

    fun setup(rot: Rotation2, aimTime: Long) {
        done = false
        startRot = Rotation2(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)
        neededChange = getNeededChange(startRot, rot)
        endRot = Rotation2(startRot!!.yaw + neededChange!!.yaw, startRot!!.pitch + neededChange!!.pitch)
        startTime = System.currentTimeMillis()
        endTime = System.currentTimeMillis() + aimTime
    }

    fun reset() {
        done = true
        startRot = null
        neededChange = null
        endRot = null
        startTime = 0L
        endTime = 0L
    }

    fun update() {
        if (System.currentTimeMillis() <= endTime) {
            mc.thePlayer.rotationYaw = interpolate(startRot!!.yaw, endRot!!.yaw)
            mc.thePlayer.rotationPitch = interpolate(startRot!!.pitch, endRot!!.pitch)
        } else if (!done) {
            mc.thePlayer.rotationYaw = endRot!!.yaw
            mc.thePlayer.rotationPitch = endRot!!.pitch
            reset()
        }
    }

    fun snapAngles(rot: Rotation2) {
        mc.thePlayer.rotationYaw = rot.yaw
        mc.thePlayer.rotationPitch = rot.pitch
    }

    private fun interpolate(start: Float, end: Float): Float {
        val spentMillis = (System.currentTimeMillis() - startTime).toFloat()
        val relativeProgress = spentMillis / (endTime - startTime).toFloat()
        return (end - start) * easeOutCubic(relativeProgress.toDouble()) + start
    }

    fun easeOutCubic(number: Double): Float {
        return max(0.0, min(1.0, 1.0 - (1.0 - number).pow(3.0))).toFloat()
    }

    fun getNeededChange(startRot: Rotation2?, endRot: Rotation2): Rotation2 {
        var yawChng =
            MathHelper.wrapAngleTo180_float(endRot.yaw) - MathHelper.wrapAngleTo180_float(startRot!!.yaw)
        if (yawChng <= -180.0f) {
            yawChng += 360.0f
        } else if (yawChng > 180.0f) {
            yawChng += -360.0f
        }
        return Rotation2(
            yawChng, endRot.pitch - startRot.pitch
        )
    }

    fun fovFromEntity(en: Entity): Double {
        return ((mc.thePlayer.rotationYaw - fovToEntity(en)) % 360.0 + 540.0) % 360.0 - 180.0
    }

    fun fovFromVec3(vec: Vec3): Double {
        return ((mc.thePlayer.rotationYaw - fovToVec3(vec)) % 360.0 + 540.0) % 360.0 - 180.0
    }

    fun fovToVec3(vec: Vec3): Float {
        val x = vec.xCoord - mc.thePlayer.posX
        val z = vec.zCoord - mc.thePlayer.posZ
        val yaw = atan2(x, z) * 57.2957795
        return (yaw * -1.0).toFloat()
    }

    fun fovToEntity(ent: Entity): Float {
        val x = ent.posX - mc.thePlayer.posX
        val z = ent.posZ - mc.thePlayer.posZ
        val yaw = atan2(x, z) * 57.2957795
        return (yaw * -1.0).toFloat()
    }

    fun getNeededChange(endRot: Rotation2): Rotation2 {
        val startRot = Rotation2(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)
        return getNeededChange(startRot, endRot)
    }

    fun getBlockSides(bp: BlockPos?): List<Vec3> {
        val ret: MutableList<Vec3> = ArrayList()
        for (side in BLOCK_SIDES) ret.add(
            Vec3(bp as Vec3i?).addVector(
                side[0].toDouble(),
                side[1].toDouble(),
                side[2].toDouble()
            )
        )
        return ret
    }

    fun lookingAt(blockPos: BlockPos, range: Float): Boolean {
        val stepSize = 0.15f
        var position = Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ)
        val look = mc.thePlayer.getLook(0.0f)
        val step = Vector3f(look.xCoord.toFloat(), look.yCoord.toFloat(), look.zCoord.toFloat())
        step.scale(stepSize / step.length())
        var i = 0
        while (i < floor((range / stepSize).toDouble()) - 2.0) {
            val blockAtPos = BlockPos(position.xCoord, position.yCoord, position.zCoord)
            if (blockAtPos == blockPos) return true
            position = position.add(Vec3(step.x.toDouble(), step.y.toDouble(), step.z.toDouble()))
            i++
        }
        return false
    }

    fun getVectorForRotation2(pitch: Float, yaw: Float): Vec3 {
        val f2 = -MathHelper.cos(-pitch * 0.017453292f)
        return Vec3(
            (MathHelper.sin(-yaw * 0.017453292f - 3.1415927f) * f2).toDouble(),
            MathHelper.sin(-pitch * 0.017453292f).toDouble(),
            (MathHelper.cos(-yaw * 0.017453292f - 3.1415927f) * f2).toDouble()
        )
    }

    fun getLook(vec: Vec3): Vec3 {
        val diffX = vec.xCoord - mc.thePlayer.posX
        val diffY = vec.yCoord - mc.thePlayer.posY + mc.thePlayer.getEyeHeight()
        val diffZ = vec.zCoord - mc.thePlayer.posZ
        val dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ).toDouble()
        return getVectorForRotation2(
            -(MathHelper.atan2(diffY, dist) * 180.0 / Math.PI).toFloat(),
            (MathHelper.atan2(diffZ, diffX) * 180.0 / Math.PI - 90.0).toFloat()
        )
    }

    fun calculateEnumfacing(pos: Vec3): EnumFacing? {
        val x = MathHelper.floor_double(pos.xCoord)
        val y = MathHelper.floor_double(pos.yCoord)
        val z = MathHelper.floor_double(pos.zCoord)
        val position = calculateIntercept(
            AxisAlignedBB(
                x.toDouble(),
                y.toDouble(),
                z.toDouble(),
                (x + 1).toDouble(),
                (y + 1).toDouble(),
                (z + 1).toDouble()
            ), pos, 50.0f
        )
        return position.sideHit
    }

    fun calculateIntercept(aabb: AxisAlignedBB, block: Vec3, range: Float): MovingObjectPosition {
        val vec3 = mc.thePlayer.getPositionEyes(1.0f)
        val vec4 = getLook(block)
        return aabb.calculateIntercept(
            vec3,
            vec3.addVector(vec4.xCoord * range, vec4.yCoord * range, vec4.zCoord * range)
        )
    }

    fun getPointsOnBlock(bp: BlockPos?): List<Vec3> {
        val ret: MutableList<Vec3> = ArrayList()
        for (side in BLOCK_SIDES) {
            for (i in 0..19) {
                var x = side[0]
                var y = side[1]
                var z = side[2]
                if (x.toDouble() == 0.5) x = (100..999).random() / 1000.0f
                if (y.toDouble() == 0.5) y = (100..999).random() / 1000.0f
                if (z.toDouble() == 0.5) z = (100..999).random() / 1000.0f
                ret.add(Vec3(bp as Vec3i?).addVector(x.toDouble(), y.toDouble(), z.toDouble()))
            }
        }
        return ret
    }
}
