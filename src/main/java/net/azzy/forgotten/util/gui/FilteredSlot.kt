package net.azzy.forgotten.util.gui

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot
import java.util.function.Predicate

class FilteredSlot(inv: Inventory, index: Int, x: Int, y: Int, private val filter: Predicate<ItemStack>) : Slot(inv, index, x, y) {

    override fun canInsert(stack: ItemStack?): Boolean {
        return filter.test(stack!!)
    }
}