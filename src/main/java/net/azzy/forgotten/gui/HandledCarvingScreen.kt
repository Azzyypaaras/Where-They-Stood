package net.azzy.forgotten.gui

import com.mojang.blaze3d.systems.RenderSystem
import net.azzy.forgotten.registry.InternalRegistries
import net.azzy.forgotten.util.mixin.UnfuckedHandledScreen
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.screen.slot.Slot
import net.minecraft.text.Text
import net.minecraft.util.Identifier


class HandledCarvingScreen(val handler: CarvingScreenHandler, val pinv: PlayerInventory, title: Text) : HandledScreen<CarvingScreenHandler>(handler, pinv, title) {

    private val foreskin = Identifier("forgotten", "textures/gui/container/carving_gui_foreground.png")
    private val baseBackground = Identifier("forgotten", "textures/gui/container/carving_gui_background.png")
    private val plateBackground = Identifier("forgotten", "textures/gui/container/carving_gui_background_plate.png")
    private val runeBackground = Identifier("forgotten", "textures/gui/container/carving_gui_background.png")
    private val matrixBackground = Identifier("forgotten", "textures/gui/container/carving_gui_background.png")

    init {
        backgroundWidth = 256
        backgroundHeight = 256
    }

    override fun drawBackground(matrices: MatrixStack?, delta: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        client!!.textureManager.bindTexture(with(handler.inv.getStack(0)){
            if(InternalRegistries.Registries.PLATE.contains(item)) plateBackground
            else baseBackground
        })
        val x = (width - backgroundWidth) / 2
        val y = (height - backgroundHeight) / 2
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight)
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)
        drawForeground(matrices)
        super.render(matrices, mouseX, mouseY, delta)
        drawMouseoverTooltip(matrices, mouseX, mouseY)
    }
    private fun drawForeground(matrices: MatrixStack) {
        client!!.textureManager.bindTexture(foreskin)
        val x = (width - backgroundWidth) / 2
        val y = (height - backgroundHeight) / 2
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight)
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