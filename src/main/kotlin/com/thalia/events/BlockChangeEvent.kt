package com.thalia.events

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.util.BlockPos
import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
class BlockChangeEvent(val oldState: IBlockState, val currentState: IBlockState, val pos: BlockPos) : Event() {

    val oldBlock: Block
        get() = oldState.block
    val newBlock: Block
        get() = currentState.block
}
