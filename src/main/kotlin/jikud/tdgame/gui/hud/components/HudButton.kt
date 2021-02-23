package jikud.tdgame.gui.hud.components

import jikud.tdgame.gui.hud.event.GUIEventType
import jikud.tdgame.gui.hud.event.GUIEventType.*
import jikud.tdgame.gui.hud.event.IMouseAction
import jikud.tdgame.helpers.CColor
import java.awt.Color

class HudButton(text: String = "") : HudText(text) {
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

    override fun draw() {
        if (!active) {
            drawInactiveState()
            return
        }
        drawDefaultState()
    }
}