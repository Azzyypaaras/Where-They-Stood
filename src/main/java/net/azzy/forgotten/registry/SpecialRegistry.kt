package net.azzy.forgotten.registry

import net.azzy.forgotten.util.shenanigans.RarityNursery
import net.minecraft.util.Formatting

object SpecialRegistry {

    fun init() {}

    //Rarities
    val LOW_NIGHTMARE = RarityNursery.createRarity(Formatting.DARK_RED, 1)
    val HIGH_NIGHTMARE = RarityNursery.createRarity(Formatting.LIGHT_PURPLE, 3)
    val TRUE_NIGHTMARE = RarityNursery.createRarity(Formatting.RED, 4)
}