package com.thalia.events

import net.minecraft.network.Packet
import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
class PacketReceivedEvent : Event {
    var packet: Packet<*>

    constructor(packet: Packet<*>) {
        this.packet = packet
    }
}