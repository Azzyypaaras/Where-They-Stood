package net.azzy.forgotten.util.context

import net.azzy.forgotten.Forgotten.Companion.WTSLog
import net.azzy.forgotten.entity.SpellEntity
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

enum class SpellContext(val key: Item?) {
    INIT(Items.CRYING_OBSIDIAN),
    MOVE(Items.SPECTRAL_ARROW),
    POSCHECK(null),
    TICK(Items.CLOCK),
    COLLIDE(Items.TARGET),
    BLOCKHIT(Items.GOLDEN_PICKAXE),
    ENTITYHIT(Items.GOLDEN_SWORD),
    DAMAGE(null),
    DAMAGED(null),
    DEATH(Items.WITHER_SKELETON_SKULL),
    DESPAWN(Items.DIAMOND);

    companion object ContextGetter {

        private val contextMap = mutableMapOf<Item, SpellContext>()

        operator fun get(key: Item): SpellContext? {
            return contextMap[key]
        }

        init {
            for (context in values())
                contextMap[context.key ?: continue] = context
        }
    }

    data class SpellPackage<T: Entity>(val spell: T, val world: World, var pos: BlockPos, val hitResult: HitResult? = null, val target: Entity? = null)
}