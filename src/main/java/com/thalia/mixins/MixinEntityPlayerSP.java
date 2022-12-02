package com.thalia.mixins;

import com.thalia.features.FakeRotater;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {EntityPlayerSP.class}, priority = 999)
public abstract class MixinEntityPlayerSP {

    @Shadow
    private boolean serverSprintState;

    @Shadow
    private boolean serverSneakState;

    @Shadow
    private float lastReportedYaw;

    @Shadow
    private double lastReportedPosY;

    @Shadow
    private double lastReportedPosX;

    @Shadow
    private double lastReportedPosZ;

    @Shadow
    private int positionUpdateTicks;

    @Shadow
    private float lastReportedPitch;
    
    @Inject(method = {"onUpdateWalkingPlayer"}, at = {@At("HEAD")}, cancellable = true)
    private void onPlayerWalkPre(CallbackInfo ci) {
        if (FakeRotater.Companion.getRotater() != null) {
            FakeRotater.Companion.getRotater().onPlayerMovePre();
            boolean flag = Minecraft.getMinecraft().thePlayer.isSprinting();
            if (flag != this.serverSprintState) {
                if (flag) {
                    Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)Minecraft.getMinecraft().thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                } else {
                    Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)Minecraft.getMinecraft().thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                }
                this.serverSprintState = flag;
            }
            boolean flag1 = Minecraft.getMinecraft().thePlayer.isSneaking();
            if (flag1 != this.serverSneakState) {
                if (flag1) {
                    Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)Minecraft.getMinecraft().thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                } else {
                    Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)Minecraft.getMinecraft().thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                }
                this.serverSneakState = flag1;
            }
            if (Minecraft.getMinecraft().getRenderViewEntity() == Minecraft.getMinecraft().thePlayer) {
                double d0 = Minecraft.getMinecraft().thePlayer.posX - this.lastReportedPosX;
                double d1 = (Minecraft.getMinecraft().thePlayer.getEntityBoundingBox()).minY - this.lastReportedPosY;
                double d2 = Minecraft.getMinecraft().thePlayer.posZ - this.lastReportedPosZ;
                double d3 = (Minecraft.getMinecraft().thePlayer.rotationYaw - this.lastReportedYaw);
                double d4 = (Minecraft.getMinecraft().thePlayer.rotationPitch - this.lastReportedPitch);
                boolean flag2 = (d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D || this.positionUpdateTicks >= 20);
                boolean flag3 = (d3 != 0.0D || d4 != 0.0D);
                if (Minecraft.getMinecraft().thePlayer.ridingEntity == null) {
                    if (flag2 && flag3) {
                        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(Minecraft.getMinecraft().thePlayer.posX, (Minecraft.getMinecraft().thePlayer.getEntityBoundingBox()).minY, Minecraft.getMinecraft().thePlayer.posZ, Minecraft.getMinecraft().thePlayer.rotationYaw, Minecraft.getMinecraft().thePlayer.rotationPitch, Minecraft.getMinecraft().thePlayer.onGround));
                    } else if (flag2) {
                        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, (Minecraft.getMinecraft().thePlayer.getEntityBoundingBox()).minY, Minecraft.getMinecraft().thePlayer.posZ, Minecraft.getMinecraft().thePlayer.onGround));
                    } else if (flag3) {
                        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C05PacketPlayerLook(Minecraft.getMinecraft().thePlayer.rotationYaw, Minecraft.getMinecraft().thePlayer.rotationPitch, Minecraft.getMinecraft().thePlayer.onGround));
                    } else {
                        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer(Minecraft.getMinecraft().thePlayer.onGround));
                    }
                } else {
                    Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(Minecraft.getMinecraft().thePlayer.motionX, -999.0D, Minecraft.getMinecraft().thePlayer.motionZ, Minecraft.getMinecraft().thePlayer.rotationYaw, Minecraft.getMinecraft().thePlayer.rotationPitch, Minecraft.getMinecraft().thePlayer.onGround));
                    flag2 = false;
                }
                this.positionUpdateTicks++;
                if (flag2) {
                    this.lastReportedPosX = Minecraft.getMinecraft().thePlayer.posX;
                    this.lastReportedPosY = (Minecraft.getMinecraft().thePlayer.getEntityBoundingBox()).minY;
                    this.lastReportedPosZ = Minecraft.getMinecraft().thePlayer.posZ;
                    this.positionUpdateTicks = 0;
                }
                if (flag3) {
                    this.lastReportedYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
                    this.lastReportedPitch = Minecraft.getMinecraft().thePlayer.rotationPitch;
                }
            }
            FakeRotater.Companion.getRotater().onPlayerMovePost();
            ci.cancel();
        } else {
            FakeRotater.Companion.setLastPitch(420.0f);
        }
    }

}
