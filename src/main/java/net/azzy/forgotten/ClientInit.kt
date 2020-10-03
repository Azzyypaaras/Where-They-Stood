package net.azzy.forgotten

import net.azzy.forgotten.registry.client.EntityRenderRegistry
import net.fabricmc.api.ClientModInitializer

class ClientInit : ClientModInitializer {

    override fun onInitializeClient() {
        EntityRenderRegistry.init()
    }

}