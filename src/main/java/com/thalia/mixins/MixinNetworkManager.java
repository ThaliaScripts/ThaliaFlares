package com.thalia.mixins;

import com.thalia.events.PacketReceivedEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({NetworkManager.class})
public class MixinNetworkManager {

    @Inject(method = {"channelRead0*"}, at = {@At("HEAD")}, cancellable = true)
    private void onReceivePacket(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
        if(MinecraftForge.EVENT_BUS.post((Event) new PacketReceivedEvent(packet)))
            callbackInfo.cancel();
    }

}
