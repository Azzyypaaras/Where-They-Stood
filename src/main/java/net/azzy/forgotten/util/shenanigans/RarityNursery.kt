package net.azzy.forgotten.util.shenanigans

import net.azzy.forgotten.Forgotten.Companion.WTSLog
import net.minecraft.enchantment.Enchantment
import net.minecraft.util.Formatting
import net.minecraft.util.Rarity
import sun.misc.Unsafe
import java.lang.reflect.Field


object RarityNursery {

    private val rarityField = Rarity::class.java.getDeclaredField("formatting")
    private val ordinalField = Enum::class.java.getDeclaredField("ordinal")

    init {
        rarityField.isAccessible = true
        ordinalField.isAccessible = true
    }

    fun createRarity(formatting: Formatting, tier: Int): Rarity {
        val rarity = Artillery.theArtillery.allocateInstance(Rarity::class.java) as Rarity
        rarityField.set(rarity, formatting)
        ordinalField.setInt(rarity, tier)
        return rarity
    }
}

