package net.azzy.forgotten.gui

import net.azzy.forgotten.Forgotten.Companion.WTSLog
import net.azzy.forgotten.registry.ScreenHandlerRegistry
import net.azzy.forgotten.util.gui.ImmutableSlot
import net.azzy.forgotten.util.mixin.ShiftingSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot


class CarvingScreenHandler(syncId: Int, val pinv: PlayerInventory, val inv: Inventory) : ScreenHandler(ScreenHandlerRegistry.CARVING_SCREEN_HANDLER, syncId) {

    constructor(syncId: Int, pinv: PlayerInventory) : this(syncId, pinv, SimpleInventory(512))

    val shiftingSlots: Array<ShiftingSlot>

    init {
        //The non-wonky part of the carving inventory
        addSlot(ImmutableSlot(inv, 0, 120, 0))
        addSlot(Slot(inv, 1, 22, 22))
        addSlot(Slot(inv, 2, 60, 22))
        addSlot(Slot(inv, 3, 180, 22))
        addSlot(Slot(inv, 4, 218, 22))

        val slotBuilder = mutableListOf<Slot>()

        var slot = 5
        for(i in 0..2)
            for(j in 0..2) {
                val carvingSlot = Slot(inv, slot, 101 + (18 * j), 83 + (18 * i))
                slotBuilder.add(carvingSlot)
                addSlot(carvingSlot)
                slots.remove(carvingSlot)
                slot++
            }
        shiftingSlots = slotBuilder.toTypedArray() as Array<ShiftingSlot>

        //The player inventory
        slot = 10
        for(i in 0..2)
            for(j in 0..8) {
                addSlot(Slot(pinv, slot, 84 + (18 * j), 192 + (18 * i)))
                slot++
            }
        //The player Hotbar
        slot = 0
        for(i in 0..2)
            for(j in 0..2) {
                addSlot(Slot(pinv, slot, 12 + (18 * j), 192 + (18 * i)))
                slot++
            }
    }

    override fun sendContentUpdates() {
        super.sendContentUpdates()
    }

    override fun updateSlotStacks(stacks: MutableList<ItemStack>?) {
        super.updateSlotStacks(stacks)
    }

    override fun transferSlot(player: PlayerEntity?, invSlot: Int): ItemStack? {
        var newStack = ItemStack.EMPTY
        val slot = slots[invSlot]
        if (slot != null && slot.hasStack()) {
            val originalStack = slot.stack
            newStack = originalStack.copy()
            if (invSlot < this.inv.size()) {
                if (!insertItem(originalStack, this.inv.size(), slots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!insertItem(originalStack, 0, this.inv.size(), false)) {
                return ItemStack.EMPTY
            }
            if (originalStack.isEmpty) {
                slot.stack = ItemStack.EMPTY
            } else {
                slot.markDirty()
            }
        }
        return newStack
    }



    override fun canUse(player: PlayerEntity?): Boolean {
        return this.inv.canPlayerUse(player)
    }
}