package net.azzy.forgotten.registry

import com.google.common.collect.HashBiMap
import net.azzy.forgotten.entity.ProjectileSpellEntity
import net.azzy.forgotten.registry.spellTypes.CollisionSpells
import net.azzy.forgotten.util.context.ContextConsumer
import net.azzy.forgotten.util.context.SpellContext
import net.azzy.forgotten.util.spell.SpellStruct
import net.minecraft.item.Item
import net.minecraft.util.Identifier

private typealias SpellContextConsumer =  ContextConsumer<SpellContext.SpellPackage<ProjectileSpellEntity>, SpellContext>

object SpellRegistry {

    private val spells = HashBiMap.create<Identifier, SpellStruct>()
    private val spellKeys = hashMapOf<Item, SpellStruct>()

    fun init() {
        CollisionSpells.init()
    }

    operator fun get(id: Identifier): SpellStruct? = spells[id]
    operator fun get(key: Item): SpellStruct? = spellKeys[key]
    operator fun get(spell: SpellStruct): Identifier = spells.inverse()[spell]!!

    operator fun contains(id: Identifier): Boolean = id in spells
    operator fun contains(key: Item): Boolean = key in spellKeys

    operator fun set(id: Identifier, spell: SpellStruct) {
        spells[id] = spell
        spellKeys[spell.key] = spell
    }
}