package net.azzy.forgotten.util.shenanigans

import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*
import java.util.function.BiFunction

object ScreenHandlerBirthplace {
    fun create(world: World, pos: BlockPos): ScreenHandlerContext {
        return object : ScreenHandlerContext {
            override fun <T> run(function: BiFunction<World, BlockPos, T>): Optional<T> {
                return Optional.of(function.apply(world, pos))
            }
        }
    }
}