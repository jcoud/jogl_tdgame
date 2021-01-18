package jikud.tdgame.gui.hud.components

import jikud.tdgame.JOGLEntry
import jikud.tdgame.core.Drawing
import jikud.tdgame.helpers.CColor
import jikud.tdgame.helpers.PPoint
import java.awt.Color

abstract class HudComponent {
    enum class Alignment {
        HORIZONTAL, VERTICAL, NONE
    }

    //

    var parent: HudPanel? = null
    var anchor = PPoint.ZERO
    var pos = PPoint.ZERO
    val actualX: Float get() = pos.x + anchor.x
    val actualY: Float get() = pos.y + anchor.y
    var width = 0
    var height = 0
    var background = CColor(Color.BLACK)
    var foreground = CColor(Color.WHITE)
    var showBackground = true
    var showForeground = true
    var show = true
    var alignment = Alignment.NONE
    var gap = 0
    var strokeThickness = 1f

    open fun draw() = with(JOGLEntry.GRF) {
        if (!show) return
        if (showBackground) {
            glColor3f(background.r, background.g, background.b)
            Drawing.fillRect(actualX, actualY, width - gap * 2f, height - gap * 2f)
//            return
        }
        if (showForeground) {
            glColor3f(foreground.r, foreground.g, foreground.b)
            glLineWidth(strokeThickness)
            Drawing.drawLineRect(actualX, actualY, width - gap * 2f, height - gap * 2f)
        }
    }
}