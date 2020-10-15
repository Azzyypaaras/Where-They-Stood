package net.azzy.forgotten.util.mixin

import net.minecraft.client.util.math.MatrixStack
import net.minecraft.screen.slot.Slot

interface UnfuckedHandledScreen {

    fun betterDrawSlot(matrices: MatrixStack, slot: Slot)
}