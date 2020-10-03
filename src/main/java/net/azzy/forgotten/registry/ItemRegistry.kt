package net.azzy.forgotten.registry


import net.azzy.forgotten.Forgotten.Companion.MOD_ID
import net.azzy.forgotten.item.TestWandItem
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry

object ItemRegistry {

    fun init() {}

    val TEST_WAND_ITEM = register("testwand", TestWandItem(Item.Settings().fireproof().rarity(Rarity.EPIC)))

    fun register(name: String, item: Item): Item {
        Registry.register(Registry.ITEM, Identifier(MOD_ID, name), item)
        return item
    }
}