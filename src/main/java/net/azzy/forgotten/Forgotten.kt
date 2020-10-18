package net.azzy.forgotten

import net.azzy.forgotten.registry.*
import net.azzy.forgotten.util.context.SpellContext
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.s2c.play.EntityS2CPacket
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

class Forgotten : ModInitializer{

    override fun onInitialize() {
        BlockRegistry.init()
        ItemRegistry.init()
        BlockEntityRegistry.init()
        ScreenHandlerRegistry.init()
        EntityRegistry.init()
        SpellRegistry.init()
    }

    companion object{
        internal const val MOD_ID = "forgotten"
        val FORGOTTEN_TAB: ItemGroup = FabricItemGroupBuilder.build(Identifier(MOD_ID, "resources")) { ItemStack(ItemRegistry.PLATE_BASIC.first) }
        val WTSLog = LogManager.getLogger(MOD_ID)
    }
}