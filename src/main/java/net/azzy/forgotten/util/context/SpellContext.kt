package net.azzy.forgotten.util.context

import net.azzy.forgotten.entity.SpellEntity
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

enum class SpellContext {
    INIT,
    MOVE,
    TICK,
    COLLIDE,
    BLOCKHIT,
    ENTITYHIT,
    DAMAGE,
    DAMAGED,
    DEATH,
    DESPAWN;

    data class SpellPackage<T: Entity>(val spell: T, val world: World, val pos: BlockPos)
}