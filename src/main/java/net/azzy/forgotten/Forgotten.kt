package net.azzy.forgotten

import net.azzy.forgotten.registry.EntityRegistry
import net.azzy.forgotten.registry.ItemRegistry
import net.fabricmc.api.ModInitializer
import org.apache.logging.log4j.LogManager

class Forgotten : ModInitializer{

    override fun onInitialize() {
        ItemRegistry.init()
        EntityRegistry.init()
    }

    companion object{
        const val MOD_ID = "forgotten"
        val WTSLog = LogManager.getLogger(MOD_ID)
    }
}