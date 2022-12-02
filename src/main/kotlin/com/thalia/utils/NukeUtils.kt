package com.thalia.utils

import com.thalia.features.FakeRotater
import com.thalia.mc
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.util.Vec3


object NukeUtils {

    var fake: Boolean = true;

    fun nuke(vec: Vec3): Boolean {
        if(fake) FakeRotater.rotate(vec)
        val block = BlockPos(vec)
        if (!mc.thePlayer.isUsingItem()) if (mc.currentScreen == null) {
            val facing: EnumFacing = VecUtil.calculateEnumfacingLook(vec)
            if (mc.playerController.onPlayerDamageBlock(block, facing)) {
                mc.effectRenderer.addBlockHitEffects(block, facing)

                return true
            }
        } else {
            mc.playerController.resetBlockRemoving()
        }
        return false
    }
}