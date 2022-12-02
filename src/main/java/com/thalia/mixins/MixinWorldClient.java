package com.thalia.mixins;

import com.thalia.events.BlockChangeEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({WorldClient.class})
public class MixinWorldClient {
    @Inject(method = {"invalidateRegionAndSetBlock"}, at = {@At("HEAD")}, cancellable = true)
    private void onBlockChange(BlockPos pos, IBlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (Minecraft.getMinecraft().theWorld == null)
            return;
        IBlockState old = Minecraft.getMinecraft().theWorld.getBlockState(pos);
        if (old != state &&
                MinecraftForge.EVENT_BUS.post((Event)new BlockChangeEvent(old, state, pos)))
            cir.setReturnValue(false);
    }
}