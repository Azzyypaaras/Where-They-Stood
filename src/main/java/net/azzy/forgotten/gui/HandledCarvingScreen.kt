package net.azzy.forgotten.gui

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot
import net.minecraft.text.Text
import net.minecraft.util.Identifier


class HandledCarvingScreen(val handler: CarvingScreenHandler, val pinv: PlayerInventory, title: Text) : HandledScreen<CarvingScreenHandler>(handler, pinv, title) {

    private val foreskin = Identifier("forgotten", "textures/gui/container/carving_gui_foreskin.png")
    private val background = Identifier("forgotten", "textures/gui/container/carving_gui_background.png")
    private val midground = Identifier("forgotten", "textures/gui/container/carving_gui_pinv.png")
    private val shiftSlots = handler.shiftingSlots

    init {
        backgroundWidth = 256
        backgroundHeight = 256
    }

    override fun drawBackground(matrices: MatrixStack?, delta: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.color4f(0.75f, 0.75f, 0.9f, 1.0f)
        client!!.textureManager.bindTexture(background)
        val x = (width - backgroundWidth) / 2
        val y = (height - backgroundHeight) / 2
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight)
    }

    private fun drawPinv(matrices: MatrixStack?) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        client!!.textureManager.bindTexture(midground)
        val x = (width - backgroundWidth) / 2
        val y = (height - backgroundHeight) / 2
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight)
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        //if(pinv.player.world.time % 20 == 0L) {
        //    (handler.shiftSlot as ShiftingSlot).setY(handler.shiftSlot.y + 1)
        //}
        renderBackground(matrices)
        drawPinv(matrices)
        super.render(matrices, mouseX, mouseY, delta)
        drawForeground(matrices!!)
        drawMouseoverTooltip(matrices, mouseX, mouseY)
    }

    private fun drawItems(matrices: MatrixStack) {
        //for (slot in shiftSlots)
            drawSlot(matrices, slot as Slot);
        //drawItem(itemStack, mouseX - i - 8, mouseY - j - r, string)
    }

    private fun drawForeground(matrices: MatrixStack) {
        client!!.textureManager.bindTexture(foreskin)
        val x = (width - backgroundWidth) / 2
        val y = (height - backgroundHeight) / 2
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        when(keyCode) {
            //up
            265 -> shiftSlots.map { slot -> slot.setY(slot.getY() - 18) }
            //down
            264 -> shiftSlots.map { slot -> slot.setY(slot.getY() + 18) }
            //left
            263 -> shiftSlots.map { slot -> slot.setX(slot.getX() - 18) }
            //right
            262 -> shiftSlots.map { slot -> slot.setX(slot.getX() + 18) }
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    private fun drawItem(stack: ItemStack, xPosition: Int, yPosition: Int, amountText: String) {
        RenderSystem.translatef(0.0f, 0.0f, 32.0f)
        zOffset = 200
        itemRenderer.zOffset = 200.0f
        itemRenderer.renderInGuiWithOverrides(stack, xPosition, yPosition)
        zOffset = 0
        itemRenderer.zOffset = 0.0f
    }


    override fun init() {
        super.init()
        // Center the title
        titleX = -1000
        playerInventoryTitleX = -1000
    }
}