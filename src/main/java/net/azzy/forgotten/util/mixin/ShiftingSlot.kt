package net.azzy.forgotten.util.mixin

interface ShiftingSlot {
    fun setX(x: Int)

    fun setY(y: Int)

    fun getX(): Int

    fun getY(): Int
}