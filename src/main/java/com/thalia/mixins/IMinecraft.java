package com.thalia.mixins;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.util.Timer;

@Mixin({Minecraft.class})
public interface IMinecraft {
    @Invoker("clickMouse")
    void leftClick();

    @Invoker("rightClickMouse")
    void rightClickMouse();

    @Accessor
    Timer getTimer();
}
