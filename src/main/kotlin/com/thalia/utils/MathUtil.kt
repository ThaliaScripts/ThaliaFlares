package com.thalia.utils

import com.thalia.ThaliaFlares
import com.thalia.mc
import com.thalia.mixins.IMinecraft
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3

object MathUtil {
    const val PI = 3.1415927f
    const val PI_180 = 0.017453292f
    const val ROTATION_NUMBER = 57.29577951308232
    const val PLAYER_REACH_SQ = 20.25f
    fun floor(value: Float): Int {
        val i = value.toInt()
        return if (value < i.toFloat()) i - 1 else i
    }

    fun floor(value: Double): Int {
        val i = value.toInt()
        return if (value < i.toDouble()) i - 1 else i
    }

    fun floor_long(value: Double): Long {
        val i = value.toLong()
        return if (value < i.toDouble()) i - 1L else i
    }

    fun round(value: Float): Int {
        val floor = floor(value)
        return if (value.toDouble() >= floor.toDouble() + 0.5) floor + 1 else floor
    }

    fun round(value: Double): Int {
        val floor = floor(value)
        return if (value >= floor.toDouble() + 0.5) floor + 1 else floor
    }

    fun roundUp(value: Float): Int {
        val floor = floor(value)
        return if (value.toDouble() >= floor.toDouble() + 0.5) floor + 1 else floor
    }

    fun roundUp(value: Double): Int {
        val floor = floor(value)
        return if (value >= floor.toDouble() + 0.5) floor + 1 else floor
    }

    fun roundDown(value: Float): Int {
        val floor = floor(value)
        return if (value.toDouble() > floor.toDouble() + 0.5) floor + 1 else floor
    }

    fun roundDown(value: Double): Int {
        val floor = floor(value)
        return if (value > floor.toDouble() + 0.5) floor + 1 else floor
    }

    fun ceil(value: Float): Int {
        val i = value.toInt()
        return if (value > i.toFloat()) i + 1 else i
    }

    fun ceil(value: Double): Int {
        val i = value.toInt()
        return if (value > i.toDouble()) i + 1 else i
    }

    fun abs(value: Int): Int {
        return if (value >= 0) value else -value
    }

    fun abs(value: Float): Float {
        return if (value >= 0.0f) value else -value
    }

    fun abs(value: Long): Long {
        return if (value >= 0L) value else -value
    }

    fun abs(value: Double): Double {
        return if (value >= 0.0) value else -value
    }

    fun randomDouble(): Double {
        return if (ThaliaFlares.random.nextBoolean()) ThaliaFlares.random.nextDouble() else -ThaliaFlares.random.nextDouble()
    }

    fun randomFloat(): Float {
        return if (ThaliaFlares.random.nextBoolean()) ThaliaFlares.random.nextFloat() else -ThaliaFlares.random.nextFloat()
    }

    fun positiveFloat(): Float {
        return ThaliaFlares.random.nextFloat()
    }

    fun negativeFloat(): Float {
        return -ThaliaFlares.random.nextFloat()
    }

    fun randomFloat(multiplier: Float): Float {
        return if (ThaliaFlares.random.nextBoolean()) ThaliaFlares.random.nextFloat() * multiplier else -ThaliaFlares.random.nextFloat() * multiplier
    }

    fun positiveFloat(multiplier: Float): Float {
        return ThaliaFlares.random.nextFloat() * multiplier
    }

    fun negativeFloat(multiplier: Float): Float {
        return -ThaliaFlares.random.nextFloat() * multiplier
    }

    fun randomNumber(bound: Int): Int {
        return if (ThaliaFlares.random.nextBoolean()) ThaliaFlares.random.nextInt(bound) else -ThaliaFlares.random.nextInt(bound)
    }

    fun inBetween(value: Float, i: Float, i2: Float): Boolean {
        return max(i, i2) >= value && min(i, i2) <= value
    }

    fun inBetween(value: Double, i: Float, i2: Float): Boolean {
        return max(i, i2).toDouble() >= value && min(i, i2).toDouble() <= value
    }

    fun inBetween(value: Double, i: Double, i2: Double): Boolean {
        return max(i, i2) >= value && min(i, i2) <= value
    }

    fun randomVec(): Vec3 {
        return Vec3(ThaliaFlares.random.nextDouble(), ThaliaFlares.random.nextDouble(), ThaliaFlares.random.nextDouble())
    }

    fun min(a: Double, b: Double): Double {
        return if (a < b) a else b
    }

    fun min(a: Float, b: Float): Float {
        return if (a < b) a else b
    }

    fun min(a: Long, b: Long): Long {
        return if (a < b) a else b
    }

    fun min(a: Int, b: Int): Int {
        return if (a < b) a else b
    }

    fun max(a: Double, b: Double): Double {
        return if (a >= b) a else b
    }

    fun max(a: Float, b: Float): Float {
        return if (a >= b) a else b
    }

    fun max(a: Int, b: Int): Int {
        return if (a >= b) a else b
    }

    fun max(vec: Vec3): Double {
        return max(max(vec.xCoord, vec.yCoord), vec.zCoord)
    }

    fun min(vec: Vec3): Double {
        return min(min(vec.xCoord, vec.yCoord), vec.zCoord)
    }

    fun clamp(num: Int, min: Int, max: Int): Int {
        return if (num < min) min else if (num > max) max else num
    }

    fun clamp(num: Float, min: Float, max: Float): Float {
        return if (num < min) min else if (num > max) max else num
    }

    fun clamp(num: Double, min: Double, max: Double): Double {
        return if (num < min) min else if (num > max) max else num
    }

    fun randomAABBPoint(aabb: AxisAlignedBB): Vec3 {
        return Vec3(
            getRandomInBetween(aabb.minX, aabb.maxX),
            getRandomInBetween(aabb.minY, aabb.maxY),
            getRandomInBetween(aabb.minZ, aabb.maxZ)
        )
    }

    fun randomAABBPoint(pos: BlockPos?): Vec3 {
        val aabb: AxisAlignedBB = VecUtil.getBlockAABB(pos)
        return Vec3(
            getRandomInBetween(aabb.minX, aabb.maxX),
            getRandomInBetween(aabb.minY, aabb.maxY),
            getRandomInBetween(aabb.minZ, aabb.maxZ)
        )
    }

    fun randomAABBPoint(pos: Vec3?): Vec3 {
        val aabb: AxisAlignedBB = VecUtil.getBlockAABB(pos)
        return Vec3(
            getRandomInBetween(aabb.minX, aabb.maxX),
            getRandomInBetween(aabb.minY, aabb.maxY),
            getRandomInBetween(aabb.minZ, aabb.maxZ)
        )
    }

    fun getRandomInBetween(i: Float, i2: Float): Float {
        val min = min(i, i2)
        return ThaliaFlares.random.nextFloat() * (max(i, i2) - min) + min
    }

    fun getRandomInBetween(i: Double, i2: Double): Double {
        val min = min(i, i2)
        return ThaliaFlares.random.nextDouble() * (max(i, i2) - min) + min
    }

    fun randomNegative(numb: Float): Float {
        return if (ThaliaFlares.random.nextBoolean()) numb else -numb
    }

    fun randomNegative(numb: Double): Double {
        return if (ThaliaFlares.random.nextBoolean()) numb else -numb
    }

    fun clampPitch(pitch: Float): Float {
        return if (pitch < -90.0f) -90.0f else if (pitch > 90.0f) 90.0f else pitch
    }

    fun clampPitch(pitch: Double): Float {
        return if (pitch < -90.0) -90.0f else (if (pitch > 90.0) 90.0 else pitch).toFloat()
    }

    fun getDecimals(num: Float): Float {
        return num - floor(num).toFloat()
    }

    fun getDecimals(num: Double): Double {
        return num - floor(num).toDouble()
    }

    fun getPositveOrZero(num: Int): Int {
        return if (num > 0) num else 0
    }

    fun getPositveOrZero(num: Long): Long {
        return if (num > 0L) num else 0L
    }

    fun getPositveOrZero(num: Float): Float {
        return if (num > 0.0f) num else 0.0f
    }

    fun getPositveOrZero(num: Double): Double {
        return if (num > 0.0) num else 0.0
    }

    fun compare(x: Int, y: Int): Int {
        return if (x < y) -1 else 1
    }

    fun compare(x: Long, y: Long): Long {
        return if (x < y) -1L else 1L
    }

    fun compare(x: Float, y: Float): Float {
        return if (x < y) -1.0f else 1.0f
    }

    fun compare(x: Double, y: Double): Double {
        return if (x < y) -1.0 else 1.0
    }

    fun calculateGaussianValue(x: Float, radius: Float): Float {
        return (1.0 / Math.sqrt(6.2831854820251465 * (radius * radius).toDouble()) * Math.exp((-(x * x)).toDouble() / (2.0 * (radius * radius).toDouble()))).toFloat()
    }

    fun interpolate(oldValue: Double, newValue: Double, interpolationValue: Double): Double {
        return oldValue + (newValue - oldValue) * interpolationValue
    }

    fun interpolate(oldValue: Float, newValue: Float, interpolationValue: Double): Float {
        return (oldValue.toDouble() + (newValue - oldValue).toDouble() * interpolationValue).toFloat()
    }

    fun interpolate(oldValue: Int, newValue: Int, interpolationValue: Double): Int {
        return (oldValue.toDouble() + (newValue - oldValue).toDouble() * interpolationValue).toInt()
    }

    fun interpolate(oldValue: Double, newValue: Double): Double {
        return oldValue + (newValue - oldValue) * (mc as IMinecraft).timer.renderPartialTicks as Double
    }

    fun interpolate(oldValue: Float, newValue: Float): Float {
        return oldValue + (newValue - oldValue) * (mc as IMinecraft).timer.renderPartialTicks
    }

    fun interpolate(oldValue: Int, newValue: Int): Int {
        return (oldValue.toFloat() + (newValue - oldValue).toFloat() * (mc as IMinecraft).timer.renderPartialTicks) as Int
    }

    fun getEuclideanToPos(pos: BlockPos): Double {
        return abs(mc.thePlayer.posX - pos.getX() as Double) + abs(mc.thePlayer.posZ - pos.getZ() as Double)
    }

    fun getEuclideanToPos(pos: Vec3): Double {
        return abs(mc.thePlayer.posX - pos.xCoord) + abs(mc.thePlayer.posZ - pos.zCoord)
    }

    fun getEuclideanToPos(pos: Vec3, goal: Vec3): Double {
        return abs(pos.xCoord - goal.xCoord) + abs(pos.zCoord - goal.zCoord)
    }

    fun getEuclideanToPos(pos: Entity, goal: Entity): Double {
        return abs(pos.posX - goal.posX) + abs(pos.posZ - goal.posZ)
    }

    fun formatFloat(value: Float): Float {
        return round(value * 1000.0f).toFloat() / 1000.0f
    }

    fun formatDouble(value: Double): Double {
        return round(value * 1000.0).toDouble() / 1000.0
    }
}