package net.azzy.forgotten.blockentity

import net.azzy.forgotten.gui.CarvingScreenHandler
import net.azzy.forgotten.registry.BlockEntityRegistry
import net.azzy.forgotten.render.util.InventoryWrapper
import net.azzy.forgotten.util.shenanigans.ScreenHandlerBirthplace
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.block.InventoryProvider
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier
import net.minecraft.util.Tickable
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.registry.Registry
import net.minecraft.world.WorldAccess

class CarvingTableEntity : BlockEntity(BlockEntityRegistry.CARVING_TABLE_ENTITY), Tickable, InventoryProvider, SidedInventory, InventoryWrapper, NamedScreenHandlerFactory, ExtendedScreenHandlerFactory, BlockEntityClientSerializable {

    private val inventory: DefaultedList<ItemStack> = DefaultedList.ofSize(32, ItemStack.EMPTY)
    var invType = 0

    override fun tick() {
        invType = when (inventory[0].item){
            Items.DIAMOND -> 1
            else -> 0
        }
        if(!world!!.isClient)
            sync()
        markDirty()
    }

    override fun getInventory(state: BlockState?, world: WorldAccess?, pos: BlockPos?): SidedInventory {
        return this
    }

    override fun getAvailableSlots(side: Direction?): IntArray {
        return IntArray(0)
    }

    fun setCarvingStack(item: ItemStack): Boolean {
        return if(inventory[0].isEmpty){
            inventory[0] = item
            true
        }
        else false
    }

    fun takeCarvingStack(): ItemStack {
        val stack = inventory[0]
        inventory[0] = ItemStack.EMPTY
        return stack
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

    override fun writeScreenOpeningData(p0: ServerPlayerEntity?, packet: PacketByteBuf?) {
        packet?.writeString(Registry.ITEM.getId(inventory[0].item).toString())
    }

    override fun toTag(tag: CompoundTag?): CompoundTag {
        Inventories.toTag(tag, inventory)
        return super.toTag(tag)
    }

    override fun fromTag(state: BlockState?, tag: CompoundTag?) {
        Inventories.fromTag(tag, inventory)
        super.fromTag(state, tag)
    }

    override fun toClientTag(tag: CompoundTag): CompoundTag {
        Inventories.toTag(tag, inventory)
        tag.putInt("invType", invType)
        return tag
    }

    override fun fromClientTag(tag: CompoundTag) {
        Inventories.fromTag(tag, inventory)
        invType = tag.getInt("invType")
    }
}