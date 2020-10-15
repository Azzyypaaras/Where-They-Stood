package net.azzy.forgotten.mixin

import com.mojang.blaze3d.systems.RenderSystem
import net.azzy.forgotten.util.mixin.UnfuckedHandledScreen
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow

@Mixin(HandledScreen::class)
class HandledScreenMixin<T : ScreenHandler> : Screen(null), UnfuckedHandledScreen {

    //private fun drawSlot(matrices: MatrixStack, slot: Slot) {
    //    val i = slot.x
    //    val j = slot.y
    //    var itemStack = slot.stack
    //    var bl = false
    //    var bl2 = slot === this.touchDragSlotStart && !this.touchDragStack.isEmpty && !this.touchIsRightClickDrag
    //    val itemStack2: ItemStack = this.client!!.player!!.inventory.cursorStack
    //    var string: String? = null
    //    if (slot === this.touchDragSlotStart && !this.touchDragStack.isEmpty && this.touchIsRightClickDrag && !itemStack.isEmpty) {
    //        itemStack = itemStack.copy()
    //        itemStack.count = itemStack.count / 2
    //    } else if (this.cursorDragging && this.cursorDragSlots.contains(slot) && !itemStack2.isEmpty) {
    //        if (this.cursorDragSlots.size == 1) {
    //            return
    //        }
    //        if (ScreenHandler.canInsertItemIntoSlot(slot, itemStack2, true) && this.handler.canInsertIntoSlot(slot)) {
    //            itemStack = itemStack2.copy()
    //            bl = true
    //            ScreenHandler.calculateStackSize(this.cursorDragSlots, this.heldButtonType, itemStack, if (slot.stack.isEmpty) 0 else slot.stack.count)
    //            val k = itemStack.maxCount.coerceAtMost(slot.getMaxItemCount(itemStack))
    //            if (itemStack.count > k) {
    //                string = Formatting.YELLOW.toString() + k
    //                itemStack.count = k
    //            }
    //        } else {
    //            this.cursorDragSlots.remove(slot)
    //            this.calculateOffset()
    //        }
    //    }
    //    this.zOffset = 100
    //    this.itemRenderer.zOffset = 100.0f
    //    if (itemStack.isEmpty && slot.doDrawHoveringEffect()) {
    //        val pair = slot.backgroundSprite
    //        if (pair != null) {
    //            val sprite = this.client!!.getSpriteAtlas(pair.first as Identifier).apply(pair.second) as Sprite
    //            this.client!!.textureManager.bindTexture(sprite.atlas.id)
    //            DrawableHelper.drawSprite(matrices, i, j, this.zOffset, 16, 16, sprite)
    //            bl2 = true
    //        }
    //    }
    //    if (!bl2) {
    //        if (bl) {
    //            DrawableHelper.fill(matrices, i, j, i + 16, j + 16, -2130706433)
    //        }
    //        RenderSystem.enableDepthTest()
    //        this.itemRenderer.renderInGuiWithOverrides(this.client!!.player, itemStack, i, j)
    //        this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemStack, i, j, string)
    //    }
    //    this.itemRenderer.zOffset = 0.0f
    //    this.zOffset = 0
    //}

    @Shadow
    fun drawSlot(matrices: MatrixStack, slot: Slot) {}

    override fun betterDrawSlot(matrices: MatrixStack, slot: Slot) {
        drawSlot(matrices, slot)
    }

}