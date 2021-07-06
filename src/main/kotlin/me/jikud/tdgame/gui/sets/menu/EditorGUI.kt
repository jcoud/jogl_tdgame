package me.jikud.tdgame.gui.sets.menu

import me.jikud.engine.core.gui.GUI
import me.jikud.engine.core.gui.event.mouse.AMouseAction
import me.jikud.engine.core.gui.event.mouse.EventMouseAction
import me.jikud.engine.core.gui.hud.HudButton
import me.jikud.engine.core.gui.hud.HudComponent
import me.jikud.engine.core.gui.hud.HudPanel
import me.jikud.engine.core.helpers.CColor
import me.jikud.engine.core.helpers.PPoint
import me.jikud.tdgame.Global
import me.jikud.tdgame.TDMain
import me.jikud.tdgame.gui.sets.editor.EditorGuiGroup
import me.jikud.tdgame.mapping.MapImpl
import me.jikud.tdgame.world.field.Field
import me.jikud.tdgame.world.obj.NodePoint
import me.jikud.tdgame.world.obj.gates.Ender
import me.jikud.tdgame.world.obj.gates.Starter
import java.awt.Color

class EditorGUI : EditorGuiGroup() {
    override fun canvas(): HudPanel = p

    private val p = HudPanel(HudComponent.Alignment.HORIZONTAL)

    init {
        p.pos = PPoint(0, 0)
        p.background = CColor(Color.GRAY)
        val bSave = HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Save").apply {
            textColor = CColor(Color.GREEN)
            mouseAction = object : AMouseAction() {
                override fun mouseClicked(e: EventMouseAction) {
                    MapImpl.save()
                }
            }

        }
        val g1 = HudPanel(HudComponent.Alignment.HORIZONTAL)
//        g1.pos = PPoint(TDMain.def_gui_height, 0)
//        val g2 = HudPanel(HudComponent.Alignment.HORIZONTAL).apply { pos = PPoint(TDMain.def_gui_height, TDMain.def_gui_height / 2)}
        val bddNode = HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Add Node").apply {
            mouseAction = object : AMouseAction() {
                override fun mouseClicked(e: EventMouseAction) {
                    Global.singleQueue_addObjectToWorld = NodePoint::class.java.simpleName
                }
            }
        }
        val bAddStarter = HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Add Starter").apply {
            mouseAction = object : AMouseAction() {
                override fun mouseClicked(e: EventMouseAction) {
                    Global.singleQueue_addObjectToWorld = Starter::class.java.simpleName
                }
            }
        }
        val bAddEnder = HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Add Ender").apply {
            mouseAction = object : AMouseAction() {
                override fun mouseClicked(e: EventMouseAction) {
                    Global.singleQueue_addObjectToWorld = Ender::class.java.simpleName
                }
            }
        }

        val bClearEntities = HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Clear Entities").apply {
            mouseAction = object : AMouseAction() {
                override fun mouseClicked(e: EventMouseAction) {
                    Field.entityListOrder.clear()
                }
            }
        }

        val bClearMap = HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Clear Map").apply {
            mouseAction = object : AMouseAction() {
                override fun mouseClicked(e: EventMouseAction) {
                    Field.clear()
                }
            }
        }

        g1.addComponent(bddNode)
        g1.addComponent(bAddStarter)
        g1.addComponent(bAddEnder)
        g1.addComponent(bClearEntities)
        g1.addComponent(bClearMap)
//        g1.pack()

        p.addComponent(bSave)
        p.addComponent(g1)
        p.pack()
    }
}
