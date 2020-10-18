package net.azzy.forgotten.util.spell

import net.azzy.forgotten.entity.ProjectileSpellEntity
import net.azzy.forgotten.util.context.ContextConsumer
import net.azzy.forgotten.util.context.SpellContext
import net.minecraft.item.Item

data class SpellStruct(val tier: Int, val color: ColorHelper.RGBAWrapper, val cinderCost: Double, val staminaCost: Double, val key: Item, val spell: ContextConsumer<SpellContext.SpellPackage<ProjectileSpellEntity>, SpellContext>)