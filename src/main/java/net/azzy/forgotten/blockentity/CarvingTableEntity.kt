package net.azzy.forgotten.blockentity

import net.azzy.forgotten.gui.CarvingScreenHandler
import net.azzy.forgotten.registry.BlockEntityRegistry
import net.azzy.forgotten.render.util.InventoryWrapper
import net.azzy.forgotten.util.shenanigans.ScreenHandlerBirthplace
import net.minecraft.block.BlockState
import net.minecraft.block.InventoryProvider
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Tickable
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.WorldAccess

class CarvingTableEntity : BlockEntity(BlockEntityRegistry.CARVING_TABLE_ENTITY), Tickable, InventoryProvider, SidedInventory, InventoryWrapper, NamedScreenHandlerFactory {

    private val inventory: DefaultedList<ItemStack> = DefaultedList.ofSize(512, ItemStack.EMPTY)

    override fun tick() {
    }

    override fun getInventory(state: BlockState?, world: WorldAccess?, pos: BlockPos?): SidedInventory {
        return this
    }

    override fun getAvailableSlots(side: Direction?): IntArray {
        return IntArray(0)
    }

    override fun canInsert(slot: Int, stack: ItemStack?, dir: Direction?): Boolean {
        return false
    }

    override fun canExtract(slot: Int, stack: ItemStack?, dir: Direction?): Boolean {
        return false
    }

    override fun markDirty() {
        super<BlockEntity>.markDirty()
    }

    override fun getItems(): DefaultedList<ItemStack> {
        return inventory
    }

    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity?): ScreenHandler? {
        return CarvingScreenHandler(syncId, inv, this)
    }

    override fun getDisplayName(): Text {
        return TranslatableText(cachedState.block.translationKey)
    }
}