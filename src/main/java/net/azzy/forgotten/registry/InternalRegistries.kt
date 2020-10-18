package net.azzy.forgotten.registry

import com.google.common.collect.HashBiMap
import net.azzy.forgotten.Forgotten.Companion.MOD_ID
import net.azzy.forgotten.item.BlankRuneItem
import net.azzy.forgotten.item.RawPlateItem
import net.azzy.forgotten.item.RuneItem
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.util.Identifier

object InternalRegistries {

    private val PLATE_REGISTRY = HashBiMap.create<Identifier, RawPlateItem>()
    private val RUNE_REGISTRY = HashBiMap.create<Identifier, RuneItem>()
    private val BLANK_REGISTRY = HashBiMap.create<Identifier, BlankRuneItem>()

    @Suppress("UNCHECKED_CAST")
    operator fun set(registry: Registries,  id: Identifier, item: Item) {
        require(id !in registry.registry) { "No no, bad! You can't just rape my registries like that! At least invite them to dinner first!" }
        (registry.registry as HashBiMap<Identifier, Item>)[id] = item
    }

    internal operator fun set(registry: Registries, id: String, item: Item) { InternalRegistries[registry, Identifier(MOD_ID, id)] = item }

    operator fun get(registry: Registries, id: Identifier): Item {
        return registry.registry[id] ?: Items.AIR
    }

    operator fun get(registry: Registries, item: Item): Identifier? {
        return registry.registry.inverse()[item]
    }

    enum class Registries(val registry: HashBiMap<Identifier, out Item>) {
        PLATE(PLATE_REGISTRY),
        RUNE(RUNE_REGISTRY),
        BLANK(BLANK_REGISTRY);

        operator fun contains(item: Item) = registry.containsValue(item)
        operator fun contains(id: Identifier) = registry.containsKey(id)
    }
}