package net.azzy.forgotten.item

import net.azzy.forgotten.entity.ProjectileSpellEntity
import net.azzy.forgotten.registry.SpellRegistry
import net.azzy.forgotten.util.context.ContextConsumer
import net.azzy.forgotten.util.context.SpellContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.FireballEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import net.minecraft.world.explosion.Explosion

class TestWandItem(settings: Settings) : Item(settings) {

    override fun use(world: World, user: PlayerEntity, hand: Hand?): TypedActionResult<ItemStack>? {
        val itemStack = user.getStackInHand(hand)
        world.playSound(null as PlayerEntity?, user.x, user.eyeY, user.z, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (RANDOM.nextFloat() * 0.4f + 0.8f))
        val entity = ProjectileSpellEntity(user, 0.0, 0.0, 0.0, world, arrayOf(
                ContextConsumer({ pkg ->
                    val pos = pkg.pos
                    pkg.world.createExplosion(pkg.spell, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 4f, Explosion.DestructionType.NONE)
                    pkg.spell.kill()
                }, SpellContext.DESPAWN), SpellRegistry[Items.GOLDEN_PICKAXE]!!.spell
        ))
        entity.setItem(itemStack)
        entity.setProperties(user, user.pitch, user.yaw, 0.0f, 0.333f, 0.333f)
        world.spawnEntity(entity)
        return TypedActionResult.success(itemStack)
    }
}