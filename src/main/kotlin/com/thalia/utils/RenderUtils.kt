package com.thalia.utils

import com.thalia.mc
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.world.World
import org.lwjgl.opengl.GL11
import java.awt.Color

object RenderUtils {
    var renderManager: RenderManager = mc.renderManager

    fun drawFilledEsp(pos: BlockPos?, c: Color) {
        val block = Minecraft.getMinecraft().theWorld.getBlockState(pos).block
        block.setBlockBoundsBasedOnState(Minecraft.getMinecraft().theWorld, pos)
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.disableDepth()
        GlStateManager.disableLighting()
        GlStateManager.color(c.red / 255.0f, c.green / 255.0f, c.blue / 255.0f, c.alpha / 255.0f)
        RenderUtils.drawFullAABB(
            block
                .getSelectedBoundingBox(Minecraft.getMinecraft().theWorld as World, pos).offset(
                    -mc.renderManager.viewerPosX,
                    -mc.renderManager.viewerPosY,
                    -mc.renderManager.viewerPosZ
                )
        )
        GlStateManager.enableDepth()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }
    fun drawOutlinedEsp(aabb: AxisAlignedBB, c: Color, width: Float) {
        GlStateManager.disableDepth()
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.disableLighting()
        GL11.glLineWidth(width)
        GlStateManager.color(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, c.getAlpha() / 255.0f)
        drawOutlinedAABB(aabb.offset(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ))
        GlStateManager.enableTexture2D()
        GL11.glLineWidth(1.0f)
        GlStateManager.disableBlend()
        GlStateManager.enableDepth()
    }

    private fun drawFullAABB(aabb: AxisAlignedBB) {
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        tessellator.draw()
    }


    private fun drawOutlinedAABB(boundingBox: AxisAlignedBB) {
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        worldrenderer.begin(3, DefaultVertexFormats.POSITION)
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex()
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex()
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex()
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex()
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex()
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex()
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex()
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex()
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex()
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex()
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex()
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex()
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex()
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex()
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex()
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex()
        tessellator.draw()
    }
}