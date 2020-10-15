package net.azzy.forgotten.util.gui

import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot
import java.util.function.Predicate

class StateVariantSlot(inv: Inventory, index: Int, x: Int, y: Int, private val condition: () -> Boolean) : Slot(inv, index, x, y) {

    override fun doDrawHoveringEffect(): Boolean {
        return condition()
    }
}