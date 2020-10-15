package net.azzy.forgotten.registry

import com.google.common.collect.HashBiMap
import net.azzy.forgotten.Forgotten.Companion.MOD_ID
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import kotlin.reflect.jvm.internal.impl.resolve.calls.inference.CapturedType

object InternalRegistries {

    private val PLATE_REGISTRY = HashBiMap.create<Identifier, Item>()
    private val RUNE_REGISTRY = HashBiMap.create<Identifier, Item>()

    operator fun set(registry: Registries,  id: Identifier, item: Item) {
        require(id !in registry.registry) { "No, bad, you can't just rape my registries like that, at least invite it to dinner first!" }
        val map = registry.registry as HashBiMap<Identifier, Item>
        map[id] = item
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
        RUNE(RUNE_REGISTRY);

        operator fun contains(item: Item) = registry.containsValue(item)
        operator fun contains(id: Identifier) = registry.containsKey(id)
    }
}