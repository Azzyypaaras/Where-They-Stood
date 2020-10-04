package net.azzy.forgotten.registry

import net.azzy.forgotten.Forgotten.Companion.MOD_ID
import net.azzy.forgotten.entity.ProjectileSpellEntity
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object EntityRegistry {

    fun init() {}

    val PROJECTILE_SPELL_ENTITY: EntityType<ProjectileSpellEntity> = register("projectile_spell", FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::ProjectileSpellEntity).trackRangeChunks(4).dimensions(EntityDimensions.fixed(0.5f, 0.5f)).trackedUpdateRate(20))

    private fun <T : Entity?> register(id: String, type: FabricEntityTypeBuilder<T>): EntityType<T> {
        return Registry.register(Registry.ENTITY_TYPE, Identifier(MOD_ID, id), type.build()) as EntityType<T>
    }
}