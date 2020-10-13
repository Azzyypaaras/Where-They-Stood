package net.azzy.forgotten.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FacingBlock
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.util.math.Direction

open class BetterFacingBlock(settings: FabricBlockSettings, val horizontal: Boolean) : FacingBlock(settings) {

    init {
        defaultState = defaultState.with(FACING, Direction.NORTH);
    }

    override fun getPlacementState(ctx: ItemPlacementContext?): BlockState? {
        return super.getPlacementState(ctx)?.with(FACING, (if(horizontal) ctx?.playerFacing?.opposite else ctx?.playerLookDirection?.opposite))
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
        builder?.add(FACING)
        super.appendProperties(builder)
    }
}