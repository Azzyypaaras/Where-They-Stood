package net.azzy.forgotten.registry.client

import net.azzy.forgotten.registry.BlockEntityRegistry
import net.azzy.forgotten.render.blockentity.CarvingTableRenderer
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer

object BlockEntityRenderRegistry {

    fun init() {
        fun <T : BlockEntity> register(entity: BlockEntityType<T>, dispatcher: (BlockEntityRenderDispatcher) -> BlockEntityRenderer<T>) = BlockEntityRendererRegistry.INSTANCE.register(entity, dispatcher)

        register(BlockEntityRegistry.CARVING_TABLE_ENTITY, ::CarvingTableRenderer)
    }

}