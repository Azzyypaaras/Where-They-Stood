package net.azzy.forgotten.util.gui

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot

class ImmutableSlot(inventory: Inventory, index: Int, x: Int, y: Int) : Slot(inventory, index, x, y) {

    override fun canInsert(stack: ItemStack?): Boolean {
        return false
    }

    override fun canTakeItems(playerEntity: PlayerEntity?): Boolean {
        return false
    }
}