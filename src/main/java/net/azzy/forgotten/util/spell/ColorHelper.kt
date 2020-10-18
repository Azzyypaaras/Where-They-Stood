package net.azzy.forgotten.util.spell

import org.apache.commons.lang3.tuple.Triple

object ColorHelper {

    val NULL = fromHex(0x000000)

    fun hexToRGB(hex: Int): Triple<Int, Int, Int> {
        return object : Triple<Int, Int, Int>() {
            override fun getLeft(): Int {
                return hex and 0xFF0000 shr 16
            }

            override fun getMiddle(): Int {
                return hex and 0xFF00 shr 8
            }

            override fun getRight(): Int {
                return hex and 0xFF
            }
        }
    }

    infix fun fromHex(hex: Int): RGBAWrapper {
        val rgb = hexToRGB(hex)
        return RGBAWrapper(rgb.left, rgb.middle, rgb.right)
    }

    infix fun toHex(wrapper: RGBAWrapper): Int {
        return ((wrapper.r and 0xFF) shl 16) or ((wrapper.g and 0xFF) shl 8) or ((wrapper.b and 0xFF) shl 0)
    }

    fun fromHex(hex: Int, alpha: Int): RGBAWrapper {
        val rgb = hexToRGB(hex)
        return RGBAWrapper(rgb.left, rgb.middle, rgb.right, alpha)
    }

    data class RGBAWrapper(val r: Int, val g: Int, val b: Int, val a: Int) {

        constructor(r: Int, g: Int, b: Int) : this(r, g, b, 255)

        infix fun appendAlpha(alpha: Int): RGBAWrapper {
            return RGBAWrapper(r, b, g, alpha)
        }

        fun normalize(): Triple<Float, Float, Float> {
            return Triple.of(
                    r / 255F,
                    g / 255F,
                    b / 255F
            )
        }

        fun invert(): RGBAWrapper {
            return RGBAWrapper(255 - r, 255 - g, 255 - b, a)
        }

        fun cycle(): RGBAWrapper {
            return RGBAWrapper(b, r, g, a)
        }
    }
}