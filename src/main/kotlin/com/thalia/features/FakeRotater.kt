package com.thalia.features

import com.thalia.mc
import com.thalia.utils.Rotation
import com.thalia.utils.VecUtil
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.BlockPos
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.Vec3


abstract class FakeRotater {
    var realYaw = 0f
    var realPitch = 0f

    init {
        if (lastPitch == 420.0f) {
            lastPitch = mc.thePlayer.rotationPitch
            lastYaw = mc.thePlayer.rotationYaw
        }
    }

    fun storeCurrentRotation() {
        realYaw = mc.thePlayer.rotationYaw
        realPitch = mc.thePlayer.rotationPitch
    }

    fun rotateBack() {
        lastYaw = mc.thePlayer.rotationYaw
        lastPitch = mc.thePlayer.rotationPitch
        mc.thePlayer.rotationPitch = realPitch
        mc.thePlayer.rotationYaw = realYaw
    }

    fun rotateToGoal(yaw: Float, pitch: Float) {
        mc.thePlayer.rotationYaw = yaw
        mc.thePlayer.rotationPitch = pitch
    }

    fun rotateToGoal(rotateVec: Vec3) {
        val rotation: Rotation = Rotation.getRotation(rotateVec)
        mc.thePlayer.rotationYaw = rotation.yaw
        mc.thePlayer.rotationPitch = rotation.pitch
    }

    fun onOpenGui() {}
    fun use() {
        rotater = this
    }

    fun terminate() {
        rotater = null
    }

    abstract fun onPlayerMovePre()
    abstract fun onPlayerMovePost()
    fun setRotationHeadYaw() {
        mc.thePlayer.rotationYawHead = lastYaw
    }

    fun setRotationHeadPitch(): Float {
        return lastPitch / 57.29578f
    }

    companion object {
        var lastYaw = 0f
        var lastPitch = 420.0f
        var rotater: FakeRotater? = null
        fun leftClick(rotationVec: Vec3) {
            rotater = object : NormalFakeRotater(rotationVec) {
                override fun interact() {
                    mc.thePlayer.swingItem()
                }
            }
        }

        fun rotate(rotationVec: Vec3) {
            rotater = object : NormalFakeRotater(rotationVec) {
                override fun interact() {}
            }
        }

        fun rotate(rotation: Rotation) {
            rotater = object : NormalFakeRotater(rotation) {
                override fun interact() {}
            }
        }

        fun clickEntity(rotation: Rotation, entity: Entity) {
            rotater = object : NormalFakeRotater(rotation) {
                override fun interact() {
                    mc.playerController.interactWithEntitySendPacket(
                        mc.thePlayer as EntityPlayer,
                        entity
                    )
                }
            }
        }

        fun clickEntity(rotationVec: Vec3, entity: Entity?) {
            rotater = object : NormalFakeRotater(rotationVec) {
                override fun interact() {
                    mc.playerController.interactWithEntitySendPacket(
                        mc.thePlayer as EntityPlayer,
                        entity
                    )
                }
            }
        }

        fun rightClick(rotationVec: Vec3, hitPos: BlockPos, clickAmount: Int) {
            rotater = object : NormalFakeRotater(rotationVec) {
                override fun interact() {
                    val position: MovingObjectPosition? = VecUtil.calculateInterceptLook(hitPos, rotationVec, 4.5f)
                    if (position != null) for (i in 0 until clickAmount) {
                        if (mc.playerController.onPlayerRightClick(
                                mc.thePlayer,
                                mc.theWorld,
                                mc.thePlayer.inventory.getCurrentItem(),
                                hitPos,
                                position.sideHit,
                                position.hitVec
                            )
                        ) mc.thePlayer.swingItem()
                    }
                }
            }
        }

        fun rightClickWithItem(rotationVec: Vec3, hitPos: BlockPos?, itemSlot: Int) {
            rotater = object : NormalFakeRotater(rotationVec) {
                override fun interact() {
                    val position: MovingObjectPosition? = VecUtil.calculateInterceptLook(hitPos, rotationVec, 4.5f)
                    if (position != null) {
                        val lastSlot: Int = mc.thePlayer.inventory.currentItem
                        mc.thePlayer.inventory.currentItem = itemSlot
                        if (mc.playerController.onPlayerRightClick(
                                mc.thePlayer,
                                mc.theWorld,
                                mc.thePlayer.inventory.getCurrentItem(),
                                hitPos,
                                position.sideHit,
                                position.hitVec
                            )
                        ) mc.thePlayer.swingItem()
                        mc.thePlayer.inventory.currentItem = lastSlot
                    }
                }
            }
        }

        fun rightClickWithItem(rotationVec: Vec3, hitPos: BlockPos?, clickAmount: Int, itemSlot: Int) {
            rotater = object : NormalFakeRotater(rotationVec) {
                override fun interact() {
                    val position: MovingObjectPosition? = VecUtil.calculateInterceptLook(hitPos, rotationVec, 4.5f)
                    if (position != null) {
                        val lastSlot: Int = mc.thePlayer.inventory.currentItem
                        mc.thePlayer.inventory.currentItem = itemSlot
                        for (i in 0 until clickAmount) {
                            if (mc.playerController.onPlayerRightClick(
                                    mc.thePlayer,
                                    mc.theWorld,
                                    mc.thePlayer.inventory.getCurrentItem(),
                                    hitPos,
                                    position.sideHit,
                                    position.hitVec
                                )
                            ) mc.thePlayer.swingItem()
                        }
                        mc.thePlayer.inventory.currentItem = lastSlot
                    }
                }
            }
        }
    }
}
