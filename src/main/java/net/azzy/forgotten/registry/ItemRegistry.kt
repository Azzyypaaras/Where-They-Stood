package net.azzy.forgotten.registry


import net.azzy.forgotten.Forgotten
import net.azzy.forgotten.Forgotten.Companion.MOD_ID
import net.azzy.forgotten.registry.InternalRegistries.Registries.*
import net.azzy.forgotten.item.TestWandItem
import net.azzy.forgotten.util.shenanigans.RarityNursery
import net.minecraft.item.Item
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry

object ItemRegistry {

    val TEST_WAND_ITEM = register("testwand", TestWandItem(Item.Settings().fireproof().rarity(SpecialRegistry.LOW_NIGHTMARE)))

    //Resources
    val EMBERS = register("ember", Item(Item.Settings().fireproof().rarity(SpecialRegistry.HIGH_NIGHTMARE).group(Forgotten.FORGOTTEN_TAB)))

    //Focusing plates
    val PLATE_BASIC = registerPlate("vitreous_plate", Item(Item.Settings().rarity(Rarity.UNCOMMON).group(Forgotten.FORGOTTEN_TAB)))

    fun init() {
        InternalRegistries[PLATE, "basic"] = PLATE_BASIC.second
    }

    private fun register(name: String, item: Item): Item {
        Registry.register(Registry.ITEM, Identifier(MOD_ID, name), item)
        return item
    }

    private fun registerPlate(name: String, item: Item): Pair<Item, Item> {
        Registry.register(Registry.ITEM, Identifier(MOD_ID, name + "_carved"), item)
        val uncarved = Registry.register(Registry.ITEM, Identifier(MOD_ID, name), Item(Item.Settings().group(item.group)))
        return Pair(item, uncarved)
    }
}