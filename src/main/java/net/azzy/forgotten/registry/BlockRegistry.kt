package net.azzy.forgotten.registry

import net.azzy.forgotten.Forgotten.Companion.MOD_ID
import net.azzy.forgotten.block.CarvingTableBlock
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.Material
import net.minecraft.block.MaterialColor
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object BlockRegistry {

    fun init() {}

    val CARVING_TABLE = register("engraving_table", CarvingTableBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.RED).strength(3.0f).sounds(BlockSoundGroup.NETHER_STEM)))

    private fun register(id: String, block: Block, settings: FabricItemSettings = FabricItemSettings()): Block {
        Registry.register(Registry.BLOCK, Identifier(MOD_ID, id), block)
        Registry.register(Registry.ITEM, Identifier(MOD_ID, id), BlockItem(block, settings))
        return block
    }

    val transBlocks = mutableListOf(
            CARVING_TABLE
    )
    val partBlocks = mutableListOf(
            CARVING_TABLE
    )
}