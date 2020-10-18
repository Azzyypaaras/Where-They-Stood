package net.azzy.forgotten.registry.client

import net.azzy.forgotten.registry.ItemRegistry
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry

object ColorProvRegistry {

    fun init() {
        for (rune in ItemRegistry.RUNE_BASIC.values)
            ColorProviderRegistry.ITEM.register({stack, index ->
                if(index == 1) {
                    stack.tag?.getInt("color") ?: 0xFFFFFFF
                }
                else 0xFFFFFFF
            }, rune)
    }
}