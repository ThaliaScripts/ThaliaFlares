package com.thalia.mixins;

import com.thalia.ThaliaFlares;
import com.thalia.features.FakeRotater;
import com.thalia.features.rotaters.Rotater;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EntityRenderer.class})
public class MixinEntityRenderer {

    @Inject(method = {"orientCamera"}, at = {@At("HEAD")})
    private void onOrientCamera(CallbackInfo ci) {
        if (FakeRotater.Companion.getLastPitch() != 420.0F)
            if (FakeRotater.Companion.getRotater() != null) {
                FakeRotater.Companion.getRotater().setRotationHeadYaw();
            } else {
                Minecraft.getMinecraft().thePlayer.rotationYawHead = FakeRotater.Companion.getLastYaw();
            }
        if (ThaliaFlares.rotater != null)
            if (Minecraft.getMinecraft().currentScreen == null) {
                ThaliaFlares.rotater.add();
            } else {
                ThaliaFlares.rotater = null;
                Rotater.rotating = false;
            }
    }
}
