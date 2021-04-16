package me.jikud.tdgame.gui.hud.components

import me.jikud.tdgame.gui.hud.event.GUIEventType
import me.jikud.tdgame.gui.hud.event.GUIEventType.*
import me.jikud.tdgame.gui.hud.event.IMouseAction
import me.jikud.tdgame.helpers.CColor
import java.awt.Color

class HudButton(width: Int, height: Int, text: String) : HudText(width, height, text) {
    var active = true
    var eventType: GUIEventType = NONE
    var action: IMouseAction? = null

    private fun drawInactiveState() {
        val b = background
        val f = foreground
        val t = textColor
        background = CColor(.5f, .5f, .5f)
        foreground = CColor(1f, 1f, 1f, .7f)
        textColor = CColor(Color.GRAY)
        super.draw()
        background = b
        foreground = f
        textColor = t
    }

    private fun drawDefaultState() {
        foreground = when (eventType) {
            MOVED -> CColor(1f, 0f, 0f)
            CLICKED -> CColor(0f, 1f, 0f)
            NONE -> CColor(1f, 1f, 0f, .7f)
        }
        background = CColor(0f, 0f, 0f)
        showBackground = true
        super.draw()
    }
    //todo: размер объекта должны определяться перед установлением текста
    override fun draw() {
        if (!active) {
            drawInactiveState()
            return
        }
        drawDefaultState()
    }
}