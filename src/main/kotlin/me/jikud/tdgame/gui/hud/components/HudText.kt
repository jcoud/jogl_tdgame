package me.jikud.tdgame.gui.hud.components

import com.jogamp.opengl.util.awt.TextRenderer
import me.jikud.tdgame.core.Drawing
import me.jikud.tdgame.helpers.CColor
import java.awt.Color

open class HudText(width: Int, height: Int, var text: String) : HudComponent(width, height) {
    var textColor = CColor(Color.WHITE)
    protected val f = Drawing.FontRendering()

    init {
        showBackground = false
        setLabel(text)
    }

    fun setLabel(text: String) {
        if (text.isEmpty()) return
        val b = f.bounds(text)
        if (width < b.width) {
            val sw = (width / b.width).toFloat()
            f.fontRenderer = TextRenderer(f.font.deriveFont(sw), true)
        }
        if (height < b.height) {
            val sw = (width / b.width).toFloat()
            f.fontRenderer = TextRenderer(f.font.deriveFont(sw), true)
        }
    }

    override fun post() {
//
    }


    override fun draw() {
        super.draw()
        if (text.isEmpty()) return
        val x: Float = (actualX + width / 2 - f.bounds(text).width / 2).toFloat()
        val y: Float = actualY + (height) / 2
        f.drawString(textColor, text, x, y)
    }
}