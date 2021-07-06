package me.jikud.tdgame.world

import me.jikud.engine.core.gui.GuiCore
import me.jikud.engine.core.gui.GuiCore.switchToAnotherGuiGroup
import me.jikud.engine.core.gui.event.IOEventProcessor
import me.jikud.engine.core.gui.event.key.EventKeyAction
import me.jikud.engine.core.gui.event.mouse.EventMouseAction
import me.jikud.engine.core.gui.event.mouse.IOMouseEventType
import me.jikud.engine.core.helpers.PPoint
import me.jikud.tdgame.Global
import me.jikud.tdgame.gui.sets.editor.EditorGuiGroup
import me.jikud.tdgame.gui.sets.editor.EditorNodeGUI
import me.jikud.tdgame.gui.sets.editor.EditorTowerGUI
import me.jikud.tdgame.gui.sets.menu.EditorGUI
import me.jikud.tdgame.gui.sets.player.PlayerGroup
import me.jikud.tdgame.gui.sets.player.PlayerTowerGUI
import me.jikud.tdgame.world.field.Field
import me.jikud.tdgame.world.field.FieldProcessorQueue
import me.jikud.tdgame.world.obj.NodePoint
import me.jikud.tdgame.world.obj.TileObjUtils
import me.jikud.tdgame.world.obj.gates.Ender
import me.jikud.tdgame.world.obj.gates.Starter
import me.jikud.tdgame.world.obj.towers.Tower
import me.jikud.tdgame.world.obj.towers.Tower_Closest
import me.jikud.tdgame.world.obj.towers.Tower_Farthest

class WorldInteraction : IOEventProcessor {
    private fun makeTowerGuiWithTowerAt(pos: PPoint) {
        val t = getTowerThroughClick(pos)
        if (t == null) {
            switchToAnotherGuiGroup(GuiCore.mainMenuGui!!)
            return
        }
        val gui = GuiCore.getGui<PlayerTowerGUI>()
        gui.setTower(t)
        switchToAnotherGuiGroup(gui)
    }

    private fun makeEditorNodeOrTowerGui(pos: PPoint) {
        val n = getNodeThroughClick(pos)
        if (n != null) {
            val gui = GuiCore.getGui<EditorNodeGUI>()
            gui.setElement(n)
            switchToAnotherGuiGroup(gui)
            return
        }
        val t = getTowerThroughClick(pos)
        if (t != null) {
            val gui = GuiCore.getGui<EditorTowerGUI>()
            gui.setElement(t)
            switchToAnotherGuiGroup(gui)
            return
        }
        switchToAnotherGuiGroup(GuiCore.getGui<EditorGUI>())
    }

    override fun processKeyEvent(e: EventKeyAction): Boolean {
        return false
    }

    override fun processMouseEvent(e: EventMouseAction): Boolean {
        when (GuiCore.currentGui) {
            is PlayerGroup -> processTowerGuiInteraction(e)
            is EditorGuiGroup -> processEditorGuiInteraction(e)
            else -> return false
        }
        return false
    }

    private fun processEditorGuiInteraction(e: EventMouseAction) {
        if (e.type != IOMouseEventType.CLICKED) return
        if (Global.singleQueue_addObjectToWorld.isNotEmpty()) {
            if (!Field.hasEmptyTileAt(e.pos)) return
            FieldProcessorQueue.queueToAdd(
                when (Global.singleQueue_addObjectToWorld) {
                    NodePoint::class.java.simpleName -> TileObjUtils.makeWithRandomParams<NodePoint>(e.pos)
                    Starter::class.java.simpleName -> TileObjUtils.makeWithRandomParams<Starter>(e.pos)
                    Ender::class.java.simpleName -> TileObjUtils.makeWithRandomParams<Ender>(e.pos)
                    else -> return
                }
            )
            Global.singleQueue_addObjectToWorld = ""
            return
        }
        makeEditorNodeOrTowerGui(e.pos)
    }

    private fun processTowerGuiInteraction(e: EventMouseAction) {
        if (e.type != IOMouseEventType.CLICKED) return
        if (Global.singleQueue_addObjectToWorld.isNotEmpty()) {
            if (!Field.hasEmptyTileAt(e.pos)) return
            FieldProcessorQueue.queueToAdd(
                when (Global.singleQueue_addObjectToWorld) {
                    Tower_Closest::class.java.simpleName -> Tower_Closest(
                        e.pos,
                        "${Tower_Closest::class.java.simpleName}${Tower_Closest.count}"
                    )
                    Tower_Farthest::class.java.simpleName -> Tower_Farthest(
                        e.pos,
                        "${Tower_Farthest::class.java.simpleName}${Tower_Farthest.count}"
                    )
                    else -> return
                }
            )
            Global.singleQueue_addObjectToWorld = ""
            return

        }
        makeTowerGuiWithTowerAt(e.pos)
    }

    private fun getTowerThroughClick(mousePos: PPoint): Tower? {
        return Field.FieldTileSelection.getSelectedObj(mousePos)
    }

    private fun getNodeThroughClick(mousePos: PPoint): NodePoint? {
        return Field.FieldTileSelection.getSelectedObj(mousePos)
    }
}