package jikud.tdgame.gui.hud.components

import com.jogamp.opengl.util.awt.TextRenderer
import jikud.tdgame.core.Drawing
import jikud.tdgame.helpers.CColor
import java.awt.Color

open class HudText() : HudComponent() {
    var textColor = CColor(Color.WHITE)
    private val f = Drawing.FontRendering()
    var text: String = ""

    constructor(text: String) : this() {
        this.text = text
    }

    init {
        showBackground = false
        if (text.isNotEmpty()) {
            val b = f.bounds(text)
            val s = ((b.width - width) / width).toFloat()
            f.renderer = TextRenderer(f.font.deriveFont(s))
        }
    }

    override fun draw() {
        super.draw()
        if (text.isEmpty()) return
        val x = pos.x + anchor.x + (width) / 2
        val y = pos.y + anchor.y + (height) / 2
        f.drawString(textColor, text, x, y)
    }
}