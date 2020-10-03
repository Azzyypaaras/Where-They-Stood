package net.azzy.forgotten.item

import net.azzy.forgotten.entity.ProjectileSpellEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.FireballEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class TestWandItem(settings: Settings) : Item(settings) {

    override fun use(world: World, user: PlayerEntity, hand: Hand?): TypedActionResult<ItemStack>? {
        val itemStack = user.getStackInHand(hand)
        world.playSound(null as PlayerEntity?, user.x, user.eyeY, user.z, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (RANDOM.nextFloat() * 0.4f + 0.8f))
        val entity = ProjectileSpellEntity(user, 0.0, 0.0, 0.0, world)
        entity.setProperties(user, user.pitch, user.yaw, 0.0f, 1.0f, 1.0f)
        world.spawnEntity(entity)
        return TypedActionResult.success(itemStack)
    }
}