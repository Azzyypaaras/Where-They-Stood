package net.azzy.forgotten.block

import net.azzy.forgotten.Forgotten.Companion.WTSLog
import net.azzy.forgotten.blockentity.CarvingTableEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class CarvingTableBlock(settings: FabricBlockSettings) : BetterFacingBlock(settings, true), BlockEntityProvider {

    override fun getOutlineShape(state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?): VoxelShape {
        return Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0)
    }

    override fun getCullingShape(state: BlockState?, world: BlockView?, pos: BlockPos?): VoxelShape {
        return Block.createCuboidShape(0.0, 1.0, 0.0, 16.0, 14.0, 16.0)
    }

    override fun createBlockEntity(world: BlockView?): BlockEntity? {
        return CarvingTableEntity()
    }

    override fun onUse(state: BlockState?, world: World?, pos: BlockPos?, player: PlayerEntity?, hand: Hand?, hit: BlockHitResult?): ActionResult {
            val stack = player!!.getStackInHand(hand)
            return if (!player.isSneaking && stack.isEmpty) {
                player.openHandledScreen(createScreenHandlerFactory(state, world!!, pos))
                ActionResult.SUCCESS
            } else {
                val entity = world!!.getBlockEntity(pos) as CarvingTableEntity
                if (!player.isSneaking && entity.items[0].isEmpty && !stack.isEmpty) {
                    entity.setCarvingStack(stack)
                    player.setStackInHand(hand, ItemStack.EMPTY)
                    ActionResult.SUCCESS
                } else {
                    player.giveItemStack(entity.takeCarvingStack())
                    ActionResult.SUCCESS
                }
            }
        return ActionResult.SUCCESS
    }

    override fun createScreenHandlerFactory(state: BlockState?, world: World, pos: BlockPos?): NamedScreenHandlerFactory? {
        return world.getBlockEntity(pos) as NamedScreenHandlerFactory
    }
}