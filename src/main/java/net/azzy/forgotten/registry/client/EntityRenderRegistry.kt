package net.azzy.forgotten.registry.client

import net.azzy.forgotten.registry.EntityRegistry
import net.azzy.forgotten.render.entity.ProjectileSpellRenderer
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.entity.EntityType
import java.util.function.BiConsumer

object EntityRenderRegistry {

    fun init() {
        register(EntityRegistry.PROJECTILE_SPELL_ENTITY, ::ProjectileSpellRenderer)
    }

    private fun register(type: EntityType<*>, factory: EntityRendererRegistry.Factory) {
        EntityRendererRegistry.INSTANCE.register(type, factory)
    }
}