package net.azzy.forgotten.gui

import net.azzy.forgotten.Forgotten.Companion.WTSLog
import net.azzy.forgotten.item.BlankRuneItem
import net.azzy.forgotten.item.RawPlateItem
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
        //Ok I need to do actual comments because this is getting exceedingly complex
        /**
         * These slots are dependant on the carving stack being some kind of raw focusing plate.
         * Indices six through nine hold the four runes that are required to make carve a plate,
         * those being (in order) RuneTypes DESPAWN, ENTITY_HIT, BLOCK, and MOVE.
         * Indices 10 and 11 are the modifier slots, with no particular specialization (so far),
         * these are not required for a successful craft.
         * Index 12 holds the form item, the item that determines the form of the spell cast,
         * implementation pending.
         */
        addSlot(StateVariantSlot(inv, 6, 63, 84, this::plateTest))
        addSlot(StateVariantSlot(inv, 7, 177, 84, this::plateTest))
        addSlot(StateVariantSlot(inv, 8, 63, 141, this::plateTest))
        addSlot(StateVariantSlot(inv, 9, 177, 141, this::plateTest))
        addSlot(StateVariantSlot(inv, 10, 32, 112, this::plateTest))
        addSlot(StateVariantSlot(inv, 11, 208, 112, this::plateTest))
        addSlot(StateVariantSlot(inv, 12, 120, 84, this::plateTest))

        //It only gets worse
        /**
         * These are all optional slots and not required for creating focusing plates.
         * Unlike the previous slots, these slots can hold pretty much any type of
         * modifier, including runes. These slots are intended for further
         * rune usage but they may be used for other modifiers.
         */
        addSlot(StateVariantSlot(inv, 29, 120, 140) { tierTest(1, setOf(2, 4)) })
        addSlot(StateVariantSlot(inv, 28, 102, 140) { tierTest(2) })
        addSlot(StateVariantSlot(inv, 30, 138, 140) { tierTest(2) })
        addSlot(StateVariantSlot(inv, 27, 84, 140) { tierTest(4) })
        addSlot(StateVariantSlot(inv, 31, 156, 140) { tierTest(4) })

        //Oh god oh fuck, runes!
        addSlot(StateVariantSlot(inv, 13, 89, 112) { slotTest(1) })
        addSlot(StateVariantSlot(inv, 14, 62, 112) { slotTest(2) })
        addSlot(StateVariantSlot(inv, 15, 35, 112) { slotTest(3) })
        addSlot(StateVariantSlot(inv, 16, 151, 112) { slotTest(1) })
        addSlot(StateVariantSlot(inv, 17, 178, 112) { slotTest(2) })
        addSlot(StateVariantSlot(inv, 18, 205, 112) { slotTest(3) })
        addSlot(StateVariantSlot(inv, 19, 120, 81, this::runeTest))
        addSlot(StateVariantSlot(inv, 20, 120, 142, this::runeTest))
    }

    private fun plateTest(): Boolean = InternalRegistries.Registries.PLATE.contains(storedStack.item)

    private fun runeTest(): Boolean = InternalRegistries.Registries.BLANK.contains(storedStack.item)

    private fun tierTest(min: Int, skip: Set<Int> = setOf()): Boolean {
        return if(storedStack.item is RawPlateItem && plateTest()) {
            val plate = storedStack.item as RawPlateItem
            return with(plate) { if(skip.isEmpty()) extra >= min else extra >= min && !skip.contains(extra)}
        }
        else false
    }

    private fun slotTest(min: Int): Boolean {
        return runeTest() && (storedStack.item as BlankRuneItem).modSlots >= min
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