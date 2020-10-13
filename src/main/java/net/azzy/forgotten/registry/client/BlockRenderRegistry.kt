package net.azzy.forgotten.registry.client

import net.azzy.forgotten.registry.BlockRegistry
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.block.Block
import net.minecraft.client.render.RenderLayer
import net.minecraft.fluid.Fluid

object BlockRenderRegistry {

    fun init() {
        initTransparency(BlockRegistry.transBlocks)
        initPartialblocks(BlockRegistry.partBlocks)
    }

    fun initTransparency(transparentblocks: List<Block>) {
        for (item in transparentblocks) BlockRenderLayerMap.INSTANCE.putBlock(item, RenderLayer.getTranslucent())
    }

    //fun initFluidTransparency(transparentfluids: List<Fluid?>) {
    //    for (item in transparentfluids) BlockRenderLayerMap.INSTANCE.putFluid(item, RenderLayer.getTranslucent())
    //}

    fun initPartialblocks(partialblocks: List<Block>) {
        for (item in partialblocks) BlockRenderLayerMap.INSTANCE.putBlock(item, RenderLayer.getCutoutMipped())
    }
}