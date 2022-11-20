package com.thalia.utils;

import com.thalia.mixins.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.List;

public class RaytraceUtils {
    public static EntityLivingBase rayTrace(float yaw, float pitch, double distance) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld != null && mc.thePlayer != null) {
            Vec3 position = mc.thePlayer.getPositionEyes(((IMinecraft)mc).getTimer().renderPartialTicks);
            Vec3 lookVector = getVectorForRotation(pitch, yaw);
            double reachDistance = distance;
            Entity pointedEntity = null;
            List<Entity> var5 = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().addCoord(lookVector.xCoord * mc.playerController.getBlockReachDistance(), lookVector.yCoord * mc.playerController.getBlockReachDistance(), lookVector.zCoord * mc.playerController.getBlockReachDistance()).expand(reachDistance, reachDistance, reachDistance));
            for (int var6 = 0; var6 < var5.size(); ++var6) {
                Entity currentEntity = (Entity) var5.get(var6);
                if (currentEntity.canBeCollidedWith()) {
                    MovingObjectPosition objPosition = currentEntity.getEntityBoundingBox().expand((double) currentEntity.getCollisionBorderSize(), (double) currentEntity.getCollisionBorderSize(), (double) currentEntity.getCollisionBorderSize()).contract(0.1, 0.1, 0.1).calculateIntercept(position, position.addVector(lookVector.xCoord * reachDistance, lookVector.yCoord * reachDistance, lookVector.zCoord * reachDistance));
                    if (objPosition != null) {
                        double range = position.distanceTo(objPosition.hitVec);
                        if (range < reachDistance) {
                            if (currentEntity == mc.thePlayer.ridingEntity && reachDistance == 0.0D) {
                                pointedEntity = currentEntity;
                            } else {
                                pointedEntity = currentEntity;
                                reachDistance = range;
                            }
                        }
                    }
                }
            }
            if (pointedEntity != null && (pointedEntity instanceof EntityLivingBase))
                return (EntityLivingBase) pointedEntity;
        }
        return null;
    }

    public static final Vec3 getVectorForRotation(float pitch, float yaw)
    {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }}
