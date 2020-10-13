package net.azzy.forgotten.util.gui

import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.WText
import net.minecraft.text.LiteralText
import kotlin.random.Random

class WTickingPanel(val guiDescription: SyncedGuiDescription) : WPlainPanel() {

    val random = Random(12414125)

    override fun tick() {
        super.tick()
        add(WText(LiteralText("aaa")), random.nextInt(300), random.nextInt(200))
        validate(guiDescription)
    }

}