package me.jikud.tdgame.gui

import com.jogamp.newt.event.MouseEvent
import me.jikud.tdgame.core.Global
import me.jikud.tdgame.gui.hud.event.EventMouseAction
import me.jikud.tdgame.gui.hud.event.GUIEventType
import me.jikud.tdgame.gui.sets.EditorGUI
import me.jikud.tdgame.gui.sets.MenuGUI
import me.jikud.tdgame.gui.sets.PlayerGUI
import me.jikud.tdgame.gui.sets.TowerGUI
import me.jikud.tdgame.helpers.PPoint
import me.jikud.tdgame.world.field.Field
import me.jikud.tdgame.world.field.FieldProcessorQueue
import me.jikud.tdgame.world.obj.NodePoint
import me.jikud.tdgame.world.obj.towers.Tower
import me.jikud.tdgame.world.obj.towers.Tower_Closest
import me.jikud.tdgame.world.obj.towers.Tower_Farthest


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

    fun makeTowerGuiWithTowerAt(pos: PPoint) {
        val t = WorldInteraction.getTowerThroughClick(pos)
        if (t == null) {
            switchToAnotherGuiGroup(playerGUI)
            return
        }
        towerGUI.setTower(t)
        switchToAnotherGuiGroup(towerGUI)
    }

    fun makeNodeGuiWithNodeAt(pos: PPoint) {
        val t = WorldInteraction.getNodeThroughClick(pos)
        if (t == null) {
            switchToAnotherGuiGroup(editorGUI)
            return
        }
        editorGUI.setElement(t)
        switchToAnotherGuiGroup(editorGUI)
    }

    fun switchToAnotherGuiGroup(gui: GUI) {
        if (this.current_gui == gui) return
        this.previous_gui = current_gui
        this.current_gui = gui
//        println(gui::class.java.simpleName)
    }

    fun postProcess() {
        current_gui?.post()
    }

    object WorldInteraction {
        fun processMouseEvent(e: EventMouseAction) {
            if (current_gui is PlayerGUI) processGuiTowerInteraction(e)
            if (current_gui is EditorGUI) processGuiNodeInteraction(e)
        }

        private fun processGuiNodeInteraction(e: EventMouseAction) {
            if (e.type != GUIEventType.CLICKED) return
            if (e !is EventMouseAction.EMAClicker) return
            if (e.button != MouseEvent.BUTTON1) return
            if (Field.hasEmptyTileAt(PPoint(e.pos))) return
        }

        private fun processGuiTowerInteraction(e: EventMouseAction) {
            if (e.type != GUIEventType.CLICKED) return
            if (e !is EventMouseAction.EMAClicker) return
            if (e.button != MouseEvent.BUTTON1) return
            if (Global.waiting_to_add_tower.isNotEmpty()) {
                if (!Field.hasEmptyTileAt(PPoint(e.pos.x, e.pos.y))) return
                FieldProcessorQueue.queueToAdd(
                    when (Global.waiting_to_add_tower) {
                        Tower_Closest::class.java.simpleName -> Tower_Closest(PPoint(e.pos), Tower_Closest::class.java.simpleName)
                        Tower_Farthest::class.java.simpleName -> Tower_Farthest(PPoint(e.pos), Tower_Farthest::class.java.simpleName)
                        else -> return
                    })
                Global.waiting_to_add_tower = ""
            } else {
                makeTowerGuiWithTowerAt(PPoint(e.pos))
            }
        }

        fun getTowerThroughClick(mousePos: PPoint): Tower? {
            return Field.FieldTileSelection.getSelectedObj(mousePos)
        }
        fun getNodeThroughClick(mousePos: PPoint): NodePoint? {
            return Field.FieldTileSelection.getSelectedObj(mousePos)
        }
    }
}