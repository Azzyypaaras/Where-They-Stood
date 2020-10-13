package net.azzy.forgotten.registry.client

import net.azzy.forgotten.gui.HandledCarvingScreen
import net.azzy.forgotten.registry.ScreenHandlerRegistry
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry

object GuiRegistry {

    fun init() {
        ScreenRegistry.register(ScreenHandlerRegistry.CARVING_SCREEN_HANDLER, ::HandledCarvingScreen)
    }
}