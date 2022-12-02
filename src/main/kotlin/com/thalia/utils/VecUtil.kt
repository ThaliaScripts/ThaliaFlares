package com.thalia.utils

import com.thalia.mc
import net.minecraft.block.Block
import net.minecraft.util.*
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World


object VecUtil {


    fun calculateInterceptLook(pos: BlockPos?, look: Vec3?, range: Float): MovingObjectPosition? {
        val aabb: AxisAlignedBB = getBlockAABB(pos)
        val vec3: Vec3 = Vec3(mc.thePlayer.posX, mc.thePlayer.posY + fastEyeHeight(), mc.thePlayer.posZ);
        return if (vec3.squareDistanceTo(look) > range * range) null else aabb.calculateIntercept(vec3, look)
    }

    fun calculateInterceptLook(pos: BlockPos?, range: Float): MovingObjectPosition? {
        val aabb: AxisAlignedBB = getBlockAABB(pos)
        val vec3: Vec3 = Vec3(mc.thePlayer.posX, mc.thePlayer.posY + fastEyeHeight(), mc.thePlayer.posZ);
        val look: Vec3 = getMiddleOfAABB(aabb)
        return if (vec3.squareDistanceTo(look) > range * range) null else aabb.calculateIntercept(vec3, look)
    }

    fun calculateInterceptLook(look: Vec3?, range: Float): MovingObjectPosition? {
        val aabb: AxisAlignedBB = getBlockAABB(BlockPos(look))
        val vec3: Vec3 = Vec3(mc.thePlayer.posX, mc.thePlayer.posY + fastEyeHeight(), mc.thePlayer.posZ);
        return if (vec3.squareDistanceTo(look) > range * range) null else aabb.calculateIntercept(vec3, look)
    }

    fun fastEyeHeight(): Float {
        return if (mc.thePlayer.isSneaking()) 1.54f else 1.62f
    }
    fun calculateInterceptAABBLook(aabb: AxisAlignedBB, look: Vec3?): MovingObjectPosition {
        return aabb.calculateIntercept(
            Vec3(
                mc.thePlayer.posX,
                mc.thePlayer.posY + fastEyeHeight(),
                mc.thePlayer.posZ
            ), look)
    }

    fun getMiddleOfAABB(aabb: AxisAlignedBB): Vec3 {
        return Vec3((aabb.maxX + aabb.minX) / 2.0, (aabb.maxY + aabb.minY) / 2.0, (aabb.maxZ + aabb.minZ) / 2.0)
    }

    fun getBlockAABB(pos: BlockPos?): AxisAlignedBB {
        val block: Block = mc.theWorld.getBlockState(pos).getBlock()
        block.setBlockBoundsBasedOnState(mc.theWorld as IBlockAccess, pos)
        return block.getSelectedBoundingBox(mc.theWorld as World, pos)
    }

    fun getBlockAABB(vec: Vec3?): AxisAlignedBB {
        val pos = BlockPos(vec)
        val block: Block = mc.theWorld.getBlockState(pos).getBlock()
        block.setBlockBoundsBasedOnState(mc.theWorld as IBlockAccess, pos)
        return block.getSelectedBoundingBox(mc.theWorld as World, pos)
    }

    fun calculateEnumfacingLook(look: Vec3?): EnumFacing {
        val position: MovingObjectPosition = calculateInterceptAABBLook(getBlockAABB(BlockPos(look)), look)
        return position.sideHit
    }
}