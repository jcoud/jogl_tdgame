package me.jikud.tdgame.gui.hud.components

import me.jikud.tdgame.core.Drawing
import me.jikud.tdgame.helpers.CColor
import me.jikud.tdgame.helpers.PPoint
import java.awt.Color

abstract class HudComponent(
    var width: Int,
    var height: Int,
    var alignment: Alignment = Alignment.NONE) {

    enum class Alignment {
        HORIZONTAL, VERTICAL, NONE
    }

    //

    var parent: HudPanel? = null
    var anchor = PPoint.ZERO
    var pos = PPoint.ZERO
    val actualX: Float get() = pos.x + anchor.x
    val actualY: Float get() = pos.y + anchor.y


    var background = CColor(Color.BLACK)
    var foreground = CColor(Color.WHITE)
    var showBackground = true
    var showForeground = true
    var show = true

    var gap = 0
    var strokeThickness = 1f

    open fun draw() = with(Drawing.GL) {
        if (!show) return
        if (showBackground) {
            glColor3f(background.r, background.g, background.b)
            Drawing.Rect(actualX, actualY, width - gap * 2f, height - gap * 2f, true)
//            return
        }
        if (showForeground) {
            glColor3f(foreground.r, foreground.g, foreground.b)
            Drawing.Rect(actualX, actualY, width - gap * 2f, height - gap * 2f, false, strokeThickness)
        }
    }
    open fun post() {
        if (this is HudText) {
            print("")
        }
    }
}