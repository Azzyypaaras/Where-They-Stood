package net.azzy.forgotten.item

import net.azzy.forgotten.Forgotten.Companion.WTSLog
import net.azzy.forgotten.util.context.SpellContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

open class RuneItem(settings: Item.Settings, val type: RuneType, val tier: Int) : Item(settings) {

    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
        val stack = user?.getStackInHand(hand!!)
        if(stack?.tag?.contains("potency") == true)
            user.sendMessage(LiteralText("" + stack.tag?.getDouble("potency")), true)
        return super.use(world, user, hand)
    }

    companion object {
        val retroTypeMap = mutableMapOf<SpellContext, RuneType>()
    }

    enum class RuneType(val type: String, val pair: SpellContext?) {
        NONE("", null),
        BLOCK("block", SpellContext.BLOCKHIT),
        MOVE("move", SpellContext.MOVE),
        DESPAWN("despawn", SpellContext.DESPAWN),
        ENTITY_HIT("entity_hit", SpellContext.ENTITYHIT);

        init {
            if (pair != null) {
                retroTypeMap[pair] = this
            }
        }

        fun getByPair(context: SpellContext): RuneType? =  retroTypeMap[context]
    }
}