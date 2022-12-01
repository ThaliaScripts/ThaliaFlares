package com.thalia.features

import com.thalia.mc
import com.thalia.utils.RenderUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityArmorStand
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

object GiftESP {

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        var validNames: List<String> = listOf("§aGreen Gift", "§fPacked Ice", "§fWhite Gift", "§aIce Bait", "§5Glacial Fragment");
        var validEntities: List<Entity> = mc.theWorld.loadedEntityList.filter { it is EntityArmorStand && it.getCurrentArmor(3)?.hasDisplayName() == true && it.getCurrentArmor(3).displayName in validNames }

        validEntities.forEach { entity -> RenderUtils.drawOutlinedEsp(entity.entityBoundingBox.expand(0.2, 0.6, 0.2).offset(0.0,1.5,0.0), Color(255, 0, 0), 3f) }
    }

}