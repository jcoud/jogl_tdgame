package me.jikud.tdgame.gui

import com.jogamp.newt.event.KeyEvent
import com.jogamp.newt.event.KeyListener
import com.jogamp.newt.event.MouseEvent
import com.jogamp.newt.event.MouseListener
import com.jogamp.newt.opengl.GLWindow
import me.jikud.tdgame.core.Drawing
import me.jikud.tdgame.core.Global
import me.jikud.tdgame.gui.hud.event.EventMouseAction
import me.jikud.tdgame.gui.hud.event.GUIEventType
import me.jikud.tdgame.gui.sets.MenuGUI
import me.jikud.tdgame.world.field.Field
import java.awt.Point

class GuiIOTransfer(c: GLWindow) : MouseListener, KeyListener {

    companion object {
        var x = 0
        var y = 0
        var b: Short = 0
    }

    init {
        c.addKeyListener(this)
        c.addMouseListener(this)
    }

    override fun mouseReleased(e: MouseEvent) {
        GuiCore.current_gui?.processMouseEvent(EventMouseAction(Point(e.x, e.y), GUIEventType.NONE))
        b = 0
    }

    override fun mousePressed(e: MouseEvent) {
        GuiCore.current_gui?.processMouseEvent(EventMouseAction.EMAClicker(Point(e.x, e.y), GUIEventType.CLICKED, e.button))
        x = e.x
        y = e.y
        b = e.button
    }

    override fun mouseMoved(e: MouseEvent) {
        GuiCore.current_gui?.processMouseEvent(EventMouseAction(Point(e.x, e.y), GUIEventType.MOVED))
        x = e.x
        y = e.y
    }

    override fun mouseWheelMoved(p0: MouseEvent) {}
    override fun mouseEntered(p0: MouseEvent) {}
    override fun mouseDragged(p0: MouseEvent) {}
    override fun mouseClicked(p0: MouseEvent) {}
    override fun mouseExited(p0: MouseEvent) {}
    override fun keyPressed(p0: KeyEvent) {
        if (GuiCore.current_gui is MenuGUI) return
        when (p0.keyCode) {
            KeyEvent.VK_ESCAPE -> {
                Drawing.drawWorld = false
                performPause()
                GuiCore.switchToAnotherGuiGroup(GuiCore.menuGUI)
            }
            KeyEvent.VK_P -> performPause()
        }

    }

    override fun keyReleased(p0: KeyEvent) {}

    private fun performPause() {
        Global.TileTimer_isUpdatable = !Global.TileTimer_isUpdatable
    }
}