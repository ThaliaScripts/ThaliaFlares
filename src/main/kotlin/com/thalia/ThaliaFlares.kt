package com.thalia

import net.minecraft.client.Minecraft
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import org.apache.logging.log4j.Logger

var mc: Minecraft = Minecraft.getMinecraft()
lateinit var logger: Logger

const val MODID = "thaliaflares"
const val VERSION = "1.0"

@Mod(modid = MODID, version = VERSION)
class ThaliaFlares {


    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        logger = event.modLog
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent?) {
        mc = Minecraft.getMinecraft()
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun input(event: InputEvent?) {

    }

}