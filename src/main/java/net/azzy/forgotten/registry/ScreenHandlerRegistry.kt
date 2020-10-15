package net.azzy.forgotten.registry

import net.azzy.forgotten.Forgotten.Companion.MOD_ID
import net.azzy.forgotten.gui.CarvingScreenHandler
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.Identifier

object ScreenHandlerRegistry {

    fun init() {}

    val CARVING_GID = Identifier(MOD_ID, "carving_gui")
    val CARVING_SCREEN_HANDLER =  ScreenHandlerRegistry.registerExtended(CARVING_GID, ::CarvingScreenHandler)
}