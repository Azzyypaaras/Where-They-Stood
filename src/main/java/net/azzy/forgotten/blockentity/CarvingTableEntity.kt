package net.azzy.forgotten.blockentity

import net.azzy.forgotten.Forgotten.Companion.WTSLog
import net.azzy.forgotten.gui.CarvingScreenHandler
import net.azzy.forgotten.item.RuneItem
import net.azzy.forgotten.registry.BlockEntityRegistry
import net.azzy.forgotten.registry.InternalRegistries
import net.azzy.forgotten.registry.ItemRegistry
import net.azzy.forgotten.registry.SpellRegistry
import net.azzy.forgotten.render.util.InventoryWrapper
import net.azzy.forgotten.util.context.SpellContext
import net.azzy.forgotten.util.spell.ColorHelper
import net.azzy.forgotten.util.spell.SpellStruct
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.block.InventoryProvider
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
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

    fun attemptRuneCraft(): Boolean {
        val rawRune = inventory[0].item
        if(!InternalRegistries.Registries.BLANK.contains(rawRune))
            return false
        var potency = 1.0
        val context = SpellContext[inventory[19].item] ?: return false
        if(checkSpellContext(context)) {
            val spells = getSpells()
            val outRune = ItemStack(ItemRegistry.RUNE_BASIC[RuneItem.RuneType.NONE.getByPair(context) ?: return false])
            if(inventory[16].item == Items.GLOWSTONE_DUST)
                potency += inventory[16].count / 4.0
            val tag = CompoundTag()
            tag.putString("spellKey", SpellRegistry[spell].toString())
            tag.putDouble("potency", potency)
            tag.putInt("color", ColorHelper toHex spell.color)
            outRune.tag = tag
            world!!.spawnEntity(ItemEntity(world, pos.x.toDouble(), pos.y + 1.0, pos.z.toDouble(), outRune))
            clearCategory()
            return true
        }
        return false
    }

    private fun getSpells(): List<Pair<SpellStruct, ItemStack>> {
        val firstSpell = SpellRegistry[inventory[13].item]
        val secSpell = SpellRegistry[inventory[14].item]
        val thirdSpell = SpellRegistry[inventory[15].item]
        val spells = mutableListOf<Pair<SpellStruct, ItemStack>>()
        if(firstSpell != null) spells.add(Pair(firstSpell, inventory[16]))
        if(secSpell != null) spells.add(Pair(secSpell, inventory[17]))
        if(thirdSpell != null) spells.add(Pair(thirdSpell, inventory[18]))
        return spells
    }

    private fun checkSpellContext(context: SpellContext): Boolean {
        val slotOne = inventory[13]
        val slotTwo = inventory[14]
        val slotThree = inventory[15]
        return if(!(slotOne.isEmpty && slotTwo.isEmpty && slotThree.isEmpty)) {
            (slotOne.isEmpty || SpellRegistry[slotOne.item]?.spell?.context == context) && (slotTwo.isEmpty || SpellRegistry[slotTwo.item]?.spell?.context == context) && (slotThree.isEmpty || SpellRegistry[slotThree.item]?.spell?.context == context)
        }
        else false
    }

    private fun blendSpellColors(): Int {

    }

    private fun clearCategory() {
        for(i in 0 until inventory.size)
            if (i == 0 || i > 5)
                inventory[i] = ItemStack.EMPTY
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