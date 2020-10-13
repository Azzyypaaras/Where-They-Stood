package net.azzy.forgotten

import net.azzy.forgotten.registry.*
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.network.packet.s2c.play.EntityS2CPacket
import org.apache.logging.log4j.LogManager

class Forgotten : ModInitializer{

    override fun onInitialize() {
        BlockRegistry.init()
        ItemRegistry.init()
        BlockEntityRegistry.init()
        ScreenHandlerRegistry.init()
        EntityRegistry.init()
        EntityPacketRegistry.init()
    }

    companion object{
        const val MOD_ID = "forgotten"
        val WTSLog = LogManager.getLogger(MOD_ID)
    }
}