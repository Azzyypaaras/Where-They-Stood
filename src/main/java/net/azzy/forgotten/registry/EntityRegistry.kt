package net.azzy.forgotten.registry

import net.azzy.forgotten.Forgotten.Companion.MOD_ID
import net.azzy.forgotten.entity.ProjectileSpellEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object EntityRegistry {

    fun init() {}

    val PROJECTILE_SPELL_ENTITY = register("projectile_spell", EntityType.Builder.create(::ProjectileSpellEntity, SpawnGroup.MISC).maxTrackingRange(4).trackingTickInterval(20).setDimensions(1f, 1f))

    private fun <T : Entity?> register(id: String, type: EntityType.Builder<T>): EntityType<T> {
        return Registry.register(Registry.ENTITY_TYPE, Identifier(MOD_ID, id), type.build(id)) as EntityType<T>
    }
}