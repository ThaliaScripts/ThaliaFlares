package com.thalia.features

import com.thalia.events.BlockChangeEvent
import com.thalia.features.GiftESP.current
import com.thalia.mc
import com.thalia.utils.NukeUtils
import com.thalia.utils.RenderUtils
import com.thalia.utils.VecUtil
import gg.essential.universal.UChat
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.init.Items
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.util.BlockPos
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import java.awt.Color


object GiftESP {

    var current: BlockPos? = null
    var enabled: Boolean = false

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if(!enabled) return
        var validNames: HashMap<String, Color> = hashMapOf(
            "§aGreen Gift" to Color(0, 255,0),
            "§fPacked Ice" to Color(0, 255, 255),
            "§aEnchanted Ice" to Color(0, 0, 255),
            "§fWhite Gift" to Color(255,155,255),
            "§aIce Bait" to Color(155, 0, 155),
            "§5Glacial Fragment" to Color(255, 0, 0),
            "§cRed Gift" to Color(255, 0, 0),
            "§aGlowy Chum Bait" to  Color(0, 125,0));

        var validEntities: List<Entity> = mc.theWorld.loadedEntityList.filter { it is EntityArmorStand && it.getCurrentArmor(3)?.hasDisplayName() == true && it.getCurrentArmor(3).displayName in validNames.keys }

        validEntities.forEach { entity -> validNames.get((entity as EntityArmorStand).getCurrentArmor(3).displayName)?.let {
            //RenderUtils.drawOutlinedEsp(entity.entityBoundingBox.expand(0.2, 0.6, 0.2).offset(0.0,1.5,0.0),
            //    it, 3f)
            var bp: BlockPos = BlockPos(entity.posX, entity.posY + 2, entity.posZ)
            RenderUtils.drawFilledEsp(bp, it)
        } }

        validEntities.filter { it.getDistanceToEntity(mc.thePlayer) < 3 }.forEach {
            if(mc.thePlayer.inventory.getCurrentItem()?.item == Items.golden_pickaxe) {
                current = BlockPos(it.posX, it.posY + 2, it.posZ)
            }
        } }

    @SubscribeEvent
    fun onTick(tickEvent: ClientTickEvent) {
        if(mc.thePlayer == null || mc.theWorld == null || current == null || !enabled) return

        if(mc.thePlayer.getDistance(current!!.x.toDouble(), current!!.y.toDouble(), current!!.z.toDouble()) < 3) NukeUtils.nuke(Vec3(current!!.x.toDouble(), current!!.y.toDouble(), current!!.z.toDouble()))
    }

    @SubscribeEvent
    fun onBlockBreak(event: BlockChangeEvent) {
        if(event.pos == current) {
            current = null
        }
    }

    private fun breakBlock(blockPos: BlockPos) {
        val objectMouseOver: MovingObjectPosition = mc.objectMouseOver
        objectMouseOver.hitVec = Vec3(blockPos)
        if (objectMouseOver.sideHit != null) {
            mc.thePlayer.sendQueue.addToSendQueue(
                C07PacketPlayerDigging(
                    C07PacketPlayerDigging.Action.START_DESTROY_BLOCK,
                    blockPos,
                    objectMouseOver.sideHit
                )
            )
        }
    }

}
