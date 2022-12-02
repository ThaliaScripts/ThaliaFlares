package com.thalia

import com.thalia.config.Config
import com.thalia.features.GiftESP
import com.thalia.features.rotaters.IRotater
import gg.essential.api.EssentialAPI
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import org.apache.logging.log4j.Logger
import org.lwjgl.input.Keyboard
import java.util.*

var mc: Minecraft = Minecraft.getMinecraft()
lateinit var logger: Logger

const val MODID = "thaliaflares"
const val VERSION = "1.0"

@Mod(modid = MODID, version = VERSION)
class ThaliaFlares {
    lateinit var key: KeyBinding

    companion object {
        @kotlin.jvm.JvmField
        var rotater: IRotater? = null;
        var random: Random = Random()
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        logger = event.modLog
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent?) {
        mc = Minecraft.getMinecraft()

        Config.initialize(); // this mi0 guy forgor to load the config 💀

        MinecraftForge.EVENT_BUS.register(this)
        //MinecraftForge.EVENT_BUS.register(Macro)
//        MinecraftForge.EVENT_BUS.register(RotationUtils)
        MinecraftForge.EVENT_BUS.register(GiftESP)

        EssentialAPI.getCommandRegistry().registerCommand(FlaresCommand)
        EssentialAPI.getCommandRegistry().registerCommand(NukeCommand)
        EssentialAPI.getCommandRegistry().registerCommand(FakeCommand)

        key = KeyBinding("Run macro idiot", Keyboard.KEY_SEMICOLON, "Flares")
        ClientRegistry.registerKeyBinding(key)
    }

    @SubscribeEvent
    fun input(event: InputEvent) {
        if (key.isPressed) {
            if (Macro.state == Macro.MacroState.Pause) {
                Macro.state = Macro.MacroState.None
            }
            else {
                Macro.state = Macro.MacroState.Pause
            }
        }
    }

}