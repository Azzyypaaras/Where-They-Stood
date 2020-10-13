package net.azzy.forgotten.mixin

import net.azzy.forgotten.util.mixin.ShiftingSlot
import net.minecraft.screen.slot.Slot
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Mutable
import org.spongepowered.asm.mixin.Shadow

@Mixin(Slot::class)
class SlotShifterMixin : ShiftingSlot {
    @Mutable
    @Final
    @Shadow
    private var x = 0

    override fun getX(): Int {
        return x
    }

    override fun setX(x: Int) {
        this.x = x
    }

    @Mutable
    @Final
    @Shadow
    private var y = 0

    override fun getY(): Int {
        return y
    }

    override fun setY(y: Int) {
        this.y = y
    }
}