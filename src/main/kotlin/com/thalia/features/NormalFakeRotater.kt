package com.thalia.features

import com.thalia.utils.Rotation
import net.minecraft.util.Vec3


abstract class NormalFakeRotater : FakeRotater {
    var rotateYaw: Float
    var rotatePitch: Float

    constructor(rotationVec: Vec3) {
        val rotation: Rotation = Rotation.getRotation(rotationVec)
        rotateYaw = rotation.yaw
        rotatePitch = rotation.pitch
    }

    constructor(rotation: Rotation) {
        rotateYaw = rotation.yaw
        rotatePitch = rotation.pitch
    }

    override fun onPlayerMovePre() {
        storeCurrentRotation()
        rotateToGoal(rotateYaw, rotatePitch)
    }

    override fun onPlayerMovePost() {
        interact()
        rotateBack()
        terminate()
    }

    abstract fun interact()
}
