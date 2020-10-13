package net.azzy.forgotten.registry

import net.azzy.forgotten.Forgotten.Companion.MOD_ID
import net.azzy.forgotten.blockentity.CarvingTableEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object BlockEntityRegistry {

    fun init() {}

    val CARVING_TABLE_ENTITY = register("carving_table_entity", BlockEntityType.Builder.create(::CarvingTableEntity, BlockRegistry.CARVING_TABLE).build(null))

    private fun <T : BlockEntity> register(id: String, entity: BlockEntityType<T>) : BlockEntityType<T> {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, Identifier(MOD_ID, id), entity)
    }
}