package jikud.tdgame.gui

import com.jogamp.newt.event.KeyEvent
import com.jogamp.newt.event.KeyListener
import com.jogamp.newt.event.MouseEvent
import com.jogamp.newt.event.MouseListener
import com.jogamp.newt.opengl.GLWindow
import jikud.tdgame.core.Drawing
import jikud.tdgame.gui.hud.event.EventMouseAction
import jikud.tdgame.gui.hud.event.GUIEventType
import jikud.tdgame.gui.sets.EditorGUI
import jikud.tdgame.gui.sets.MenuGUI
import jikud.tdgame.gui.sets.PlayerGUI
import jikud.tdgame.gui.sets.TowerGUI
import jikud.tdgame.helpers.PPoint
import jikud.tdgame.world.Field
import jikud.tdgame.world.FieldProcessorQueue
import jikud.tdgame.world.obj.TileObjUtils
import jikud.tdgame.world.obj.Tower
import java.awt.Point


object GuiCore {
    var current_gui: GUI? = null
    var previous_gui: GUI? = null

    private val towerGUI = TowerGUI()
    val playerGUI = PlayerGUI()
    val menuGUI = MenuGUI()
    val editorGUI = EditorGUI()

    fun repaintProcess() {
        current_gui?.drawGui()
    }

    fun makeTowerGuiWithTowerAt(pos: Point) {
        val t = WorldInteraction.getTowerThroughClick(pos)
        if (t == null) {
            switchToAnotherGuiGroup(playerGUI)
            return
        }
        towerGUI.setTower(t)
        switchToAnotherGuiGroup(towerGUI)
    }

    fun switchToAnotherGuiGroup(gui: GUI) {
        if (this.current_gui == gui) return
        this.previous_gui = current_gui
        this.current_gui = gui
//        println(gui::class.java.simpleName)
    }

    class InputTransfer(c: GLWindow) : MouseListener, KeyListener {

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
            current_gui?.processMouseEvent(EventMouseAction(Point(e.x, e.y), GUIEventType.NONE))
            b = 0
        }

        override fun mousePressed(e: MouseEvent) {
            current_gui?.processMouseEvent(EventMouseAction.EMAClicker(Point(e.x, e.y), GUIEventType.CLICKED, e.button))
            x = e.x
            y = e.y
            b = e.button
        }

        override fun mouseMoved(e: MouseEvent) {
            current_gui?.processMouseEvent(EventMouseAction(Point(e.x, e.y), GUIEventType.MOVED))
            x = e.x
            y = e.y
        }

        override fun mouseWheelMoved(p0: MouseEvent) {}
        override fun mouseEntered(p0: MouseEvent) {}
        override fun mouseDragged(p0: MouseEvent) {}
        override fun mouseClicked(p0: MouseEvent) {}
        override fun mouseExited(p0: MouseEvent) {}
        override fun keyPressed(p0: KeyEvent) {
            if (current_gui is MenuGUI) return
            if (p0.keyCode != KeyEvent.VK_ESCAPE) return
            Drawing.drawWorld = false
            Field.task = Field.TaskType.NONE
            switchToAnotherGuiGroup(menuGUI)
        }
        override fun keyReleased(p0: KeyEvent) {}
    }

    object WorldInteraction {
        fun processMouseEvent(e: EventMouseAction) {
            if (e.type == GUIEventType.CLICKED) {
                if (e is EventMouseAction.EMAClicker)
                    if (e.button == MouseEvent.BUTTON2) {
                        if (Field.hasEmptyTileAt(PPoint(e.pos.x, e.pos.y)))
                            FieldProcessorQueue.queueAdd(
                                TileObjUtils.makeWithRandomParams<Tower>(PPoint(e.pos.x, e.pos.y))
                            )
                    } else if (e.button == MouseEvent.BUTTON1) {
                        makeTowerGuiWithTowerAt(e.pos)
                    }
            }
        }

        fun getTowerThroughClick(mousePos: Point): Tower? {
            return Field.FieldTileSelection.getSelectedObj(PPoint(mousePos))
        }
    }
}