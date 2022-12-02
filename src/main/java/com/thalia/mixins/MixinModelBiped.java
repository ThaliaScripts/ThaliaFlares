package com.thalia.mixins;

import com.thalia.features.FakeRotater;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {ModelBiped.class}, priority = 999)
public abstract class MixinModelBiped extends ModelBase {
    @Shadow
    public ModelRenderer bipedHead;

    @Inject(method = {"setRotationAngles"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/util/MathHelper;cos(F)F", ordinal = 0)})
    private void changeRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo ci) {
        if (FakeRotater.Companion.getLastPitch() != 420.0F && entityIn instanceof net.minecraft.client.entity.EntityPlayerSP)
            if (FakeRotater.Companion.getRotater() != null) {
                this.bipedHead.rotateAngleX = FakeRotater.Companion.getRotater().setRotationHeadPitch();
            } else {
                this.bipedHead.rotateAngleX = FakeRotater.Companion.getLastPitch() / 57.29578F;
            }
    }
}
