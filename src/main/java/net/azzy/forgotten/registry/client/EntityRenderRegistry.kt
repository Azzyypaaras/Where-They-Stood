package net.azzy.forgotten.registry.client

import net.azzy.forgotten.Forgotten.Companion.WTSLog
import net.azzy.forgotten.registry.EntityRegistry
import net.azzy.forgotten.render.entity.ProjectileSpellRenderer
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import net.minecraft.entity.EntityType
import java.util.function.BiConsumer

object EntityRenderRegistry {

    fun init() {
        register(EntityRegistry.PROJECTILE_SPELL_ENTITY) { a, b -> return@register ProjectileSpellRenderer(a) }
    }

    private fun register(type: EntityType<*>, factory: EntityRendererRegistry.Factory) {
        EntityRendererRegistry.INSTANCE.register(type, factory)
    }
}