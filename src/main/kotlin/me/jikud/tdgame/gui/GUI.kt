package me.jikud.tdgame.gui

import me.jikud.tdgame.core.Drawing
import me.jikud.tdgame.gui.hud.components.HudButton
import me.jikud.tdgame.gui.hud.components.HudPanel
import me.jikud.tdgame.gui.hud.event.EventMouseAction
import me.jikud.tdgame.gui.hud.event.GUIEventType
import me.jikud.tdgame.gui.sets.MenuGUI

abstract class GUI {
    //    private var canvas: HudPanel? = null
    abstract fun canvas(): HudPanel


    fun drawGui() {
        val c = canvas()
        Drawing.GL.glColor3f(0f, 0f, 0f)
        Drawing.Rect(c.pos.x, c.pos.y, c.width.toFloat(), c.height.toFloat(), true)
        c.draw()
    }

    private fun processMouseEvent(c: HudPanel, e: EventMouseAction) {
        var foundButtonHud = false
        var mouseInButtonHud = false
        for (cc in c.components) {
            if (cc is HudPanel) {
                processMouseEvent(cc, e)
                return
            }
            if (cc !is HudButton) {
                foundButtonHud = false
                continue
            }
            foundButtonHud = true
            if (e.pos.x.toFloat() in cc.actualX..cc.actualX + cc.width &&
                e.pos.y.toFloat() in cc.actualY..cc.actualY + cc.height) {
                mouseInButtonHud = true
                cc.action?.mouseAction(e)
            } else {
                cc.action?.mouseAction(EventMouseAction(e.pos, GUIEventType.NONE))
//                e.source = null
            }
        }
        if (GuiCore.current_gui is MenuGUI) return
        //checking if event was in GUI or in the world
        if (!foundButtonHud || !mouseInButtonHud) {
            GuiCore.WorldInteraction.processMouseEvent(e)
        }
    }

    fun processMouseEvent(e: EventMouseAction) {
        processMouseEvent(canvas(), e)
    }

    fun post() {
        canvas().post()
    }
}

