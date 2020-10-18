package net.azzy.forgotten.registry


import net.azzy.forgotten.Forgotten
import net.azzy.forgotten.Forgotten.Companion.MOD_ID
import net.azzy.forgotten.item.BlankRuneItem
import net.azzy.forgotten.item.RawPlateItem
import net.azzy.forgotten.item.RuneItem
import net.azzy.forgotten.item.RuneItem.RuneType.*
import net.azzy.forgotten.registry.InternalRegistries.Registries.*
import net.azzy.forgotten.item.TestWandItem
import net.azzy.forgotten.util.shenanigans.RarityNursery
import net.minecraft.item.Item
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry

object ItemRegistry {

    private fun default() = Item.Settings().group(Forgotten.FORGOTTEN_TAB)

    val TEST_WAND_ITEM = register("testwand", TestWandItem(Item.Settings().fireproof().rarity(SpecialRegistry.LOW_NIGHTMARE)))

    //Resources
    val EMBERS = register("ember", Item(default().fireproof().rarity(SpecialRegistry.HIGH_NIGHTMARE)))
    val BINDING_SEAL = register("binding_seal", Item(default()))

    //Focusing plates
    val PLATE_BASIC = registerPlate("vitreous_plate", Item(default().rarity(Rarity.UNCOMMON)), 0, 0)

    //Runes
    val RUNE_BASIC = registerRunes("basic_rune", default(), 0, 3)

    fun init() {
        InternalRegistries[PLATE, "basic"] = PLATE_BASIC.second
        registerRuneTypes("basic_rune", RUNE_BASIC)
    }

    private fun register(name: String, item: Item): Item {
        Registry.register(Registry.ITEM, Identifier(MOD_ID, name), item)
        return item
    }

    private fun registerPlate(name: String, item: Item, tier: Int, bonusSlots: Int): Pair<Item, Item> {
        Registry.register(Registry.ITEM, Identifier(MOD_ID, name + "_carved"), item)
        val uncarved = Registry.register(Registry.ITEM, Identifier(MOD_ID, name), RawPlateItem(Item.Settings().group(item.group), tier, bonusSlots))
        return Pair(item, uncarved)
    }

    private fun registerRunes(name: String, settings: Item.Settings, tier: Int, modSlots: Int = 1, power: Int = 0) : Map<RuneItem.RuneType, RuneItem> {
        val runes = mutableMapOf<RuneItem.RuneType, RuneItem>()
        runes[NONE] = Registry.register(Registry.ITEM, Identifier(MOD_ID, name), BlankRuneItem(settings, tier, modSlots, power))
        for(type in RuneItem.RuneType.values())
            if(type != NONE)
                runes[type] = Registry.register(Registry.ITEM, Identifier(MOD_ID, if(type == NONE) name else "${name}_${type.type}"), RuneItem(settings, type, tier))
        return runes.toMap()
    }

    private fun registerRuneTypes(name: String, runes: Map<RuneItem.RuneType, RuneItem>) {
        InternalRegistries[BLANK, "${name}_blank"] = runes[NONE]!!
        runes.filter { rune -> rune.key != NONE }.map { rune -> InternalRegistries[RUNE, "${name}_${rune.key.type}"] = rune.value }
    }
}