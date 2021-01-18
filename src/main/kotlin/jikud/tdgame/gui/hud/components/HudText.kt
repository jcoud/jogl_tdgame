package jikud.tdgame.gui.hud.components

import jikud.tdgame.core.Drawing
import jikud.tdgame.helpers.CColor
import java.awt.Color

open class HudText(open var text: String = "") : HudComponent() {
    var textColor = CColor(Color.WHITE)
    private val f = Drawing.FontRendering()

    init {
        showBackground = false
    }

    override fun draw() {
        super.draw()
        if (text.isEmpty()) return
        val x = pos.x + anchor.x + (width) / 2
        val y = pos.y + anchor.y + (height) / 2
        f.drawString(textColor, text, x, y)
    }
}