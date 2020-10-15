package net.azzy.forgotten.util.shenanigans

import sun.misc.Unsafe

internal object Artillery {

    internal val theArtillery: Unsafe

    init {
        val unsafeField = Unsafe::class.java.getDeclaredField("theUnsafe")
        unsafeField.isAccessible = true
        theArtillery = unsafeField.get(null) as Unsafe
    }
}