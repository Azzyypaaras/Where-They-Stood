package net.azzy.forgotten.registry

import net.azzy.forgotten.Forgotten.Companion.MOD_ID
import net.azzy.forgotten.Forgotten.Companion.WTSLog
import net.azzy.forgotten.entity.ProjectileSpellEntity
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.EntityType
import net.minecraft.entity.boss.dragon.EnderDragonEntity
import net.minecraft.network.packet.s2c.play.EntityS2CPacket
import net.minecraft.text.LiteralText
import net.minecraft.util.Identifier

object EntityPacketRegistry {

    val CLIENT_ENTITY_SPAWN = Identifier(MOD_ID, "client_entity_spawn")

    fun init() {
        ClientSidePacketRegistry.INSTANCE.register(EntityPacketRegistry.CLIENT_ENTITY_SPAWN) { context, packet  ->
            val player = context.player
            val world = player.world
            val entityPacket = EntityS2CPacket()
            entityPacket.read(packet)
            context.taskQueue.execute {
                val entity = ProjectileSpellEntity(player, 0.0, 0.0, 0.0, world, arrayOf())
                entity.setProperties(player, player.pitch, player.yaw, 0.0f, 1.0f, 1.0f)
                world.spawnEntity(entity)
            }
        }
    }
}