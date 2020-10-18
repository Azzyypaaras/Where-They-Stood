package net.azzy.forgotten.registry.spellTypes

import net.azzy.forgotten.Forgotten.Companion.MOD_ID
import net.azzy.forgotten.registry.SpellRegistry
import net.azzy.forgotten.util.context.ContextConsumer
import net.azzy.forgotten.util.context.SpellContext
import net.azzy.forgotten.util.spell.SpellStruct
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction

object CollisionSpells {

    fun init() {
        SpellRegistry[Identifier(MOD_ID, "bounce")] = SpellStruct(1, 0xbaf04f, 1.0, 1.0, Items.SLIME_BALL, ContextConsumer({ pkg ->
            with(pkg.spell) {
                val world = pkg.world
                val collisionDir = Direction.values().filter { dir -> !world.isAir(blockPos.offset(dir)) }
                if(collisionDir.isNotEmpty())
                        when(collisionDir[0].axis) {
                        Direction.Axis.X -> setVelocity(-velocity.x, velocity.y, velocity.z)
                        Direction.Axis.Y -> setVelocity(velocity.x, -velocity.y, velocity.z)
                        Direction.Axis.Z -> setVelocity(velocity.x, velocity.y, -velocity.z)
                        else -> setVelocity(-velocity.x, -velocity.y, -velocity.z)
                    }
            }
        }, SpellContext.BLOCKHIT))
        SpellRegistry[Identifier(MOD_ID, "break")] = SpellStruct(1, 0x9e705a, 1.0, 1.0, Items.GOLDEN_PICKAXE, ContextConsumer({ pkg ->
            pkg.world.breakBlock(pkg.pos, true)
            pkg.spell.kill()
        }, SpellContext.BLOCKHIT))
    }
}