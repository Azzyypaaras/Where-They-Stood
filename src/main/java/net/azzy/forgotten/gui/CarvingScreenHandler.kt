package net.azzy.forgotten.gui

import net.azzy.forgotten.Forgotten.Companion.WTSLog
import net.azzy.forgotten.registry.InternalRegistries
import net.azzy.forgotten.registry.ScreenHandlerRegistry
import net.azzy.forgotten.util.gui.ImmutableSlot
import net.azzy.forgotten.util.gui.StateVariantSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry


class CarvingScreenHandler(syncId: Int, val pinv: PlayerInventory, val inv: Inventory) : ScreenHandler(ScreenHandlerRegistry.CARVING_SCREEN_HANDLER, syncId) {

    private var storedStack: ItemStack = ItemStack.EMPTY

    constructor(syncId: Int, pinv: PlayerInventory, byteBuf: PacketByteBuf) : this(syncId, pinv, SimpleInventory(32)) {
        storedStack = ItemStack(Registry.ITEM[Identifier(byteBuf.readString())])
        WTSLog.error(storedStack)
    }

    init {
        if (!inv.getStack(0).isEmpty)
            storedStack = inv.getStack(0)
        //The non-wonky part of the carving inventory
        addSlot(Slot(inv, 1, 22, 22))
        addSlot(Slot(inv, 2, 60, 22))
        addSlot(Slot(inv, 3, 180, 22))
        addSlot(Slot(inv, 4, 218, 22))
        addSlot(Slot(inv, 5, 120, 22))

        //The player inventory
        var slot = 10
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
        //O H B O Y
        addSlot(ImmutableSlot(inv, 0, 120, 112))

        //uh oh, stinky
        addSlot(StateVariantSlot(inv, 6, 63, 84, this::plateTest))
        addSlot(StateVariantSlot(inv, 7, 177, 84, this::plateTest))
        addSlot(StateVariantSlot(inv, 8, 63, 141, this::plateTest))
        addSlot(StateVariantSlot(inv, 9, 177, 141, this::plateTest))
        addSlot(StateVariantSlot(inv, 10, 32, 112, this::plateTest))
        addSlot(StateVariantSlot(inv, 11, 208, 112, this::plateTest))
        addSlot(StateVariantSlot(inv, 12, 120, 84, this::plateTest))
    }

    private fun plateTest(): Boolean {
        return InternalRegistries.Registries.PLATE.contains(storedStack.item)
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