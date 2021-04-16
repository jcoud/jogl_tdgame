package me.jikud.tdgame.gui.sets

import me.jikud.tdgame.TDMain
import me.jikud.tdgame.core.Global
import me.jikud.tdgame.gui.GUI
import me.jikud.tdgame.gui.hud.components.HudButton
import me.jikud.tdgame.gui.hud.components.HudComponent
import me.jikud.tdgame.gui.hud.components.HudPanel
import me.jikud.tdgame.gui.hud.event.EventMouseAction
import me.jikud.tdgame.gui.hud.event.GUIEventType
import me.jikud.tdgame.gui.hud.event.MouseActionAbstract
import me.jikud.tdgame.helpers.CColor
import me.jikud.tdgame.helpers.PPoint
import me.jikud.tdgame.world.obj.towers.Tower_Closest
import me.jikud.tdgame.world.obj.towers.Tower_Farthest
import java.awt.Color

class PlayerGUI : GUI() {
    private val p = HudPanel(TDMain.def_gui_width, TDMain.def_gui_height)
    private val btns = mapOf(
        Pair("tower_type_1", HudButton(TDMain.def_gui_height, TDMain.def_gui_height,"Tower 1")),
        Pair("tower_type_2", HudButton(TDMain.def_gui_height, TDMain.def_gui_height,"Tower 2")),
        Pair("tower_type_3", HudButton(TDMain.def_gui_height, TDMain.def_gui_height,"Tower 3")),
        Pair("tower_type_4", HudButton(TDMain.def_gui_height, TDMain.def_gui_height,"Tower 4"))
    )

    init {
        p.pos = PPoint(0, TDMain.fieldHeight)
        p.background = CColor(Color.MAGENTA)
        val o = HudPanel()
        o.background = CColor(Color.LIGHT_GRAY)
        o.alignment = HudComponent.Alignment.HORIZONTAL
        btns["tower_type_1"]!!.action = object : MouseActionAbstract(btns["tower_type_1"]!!) {
            override fun mouseAction(e: EventMouseAction) {
                super.mouseAction(e)
                if (e.type == GUIEventType.CLICKED) {
                    Global.waiting_to_add_tower = Tower_Closest::class.java.simpleName
                }
            }
        }
        btns["tower_type_2"]!!.action = object : MouseActionAbstract(btns["tower_type_2"]!!) {
            override fun mouseAction(e: EventMouseAction) {
                super.mouseAction(e)
                if (e.type == GUIEventType.CLICKED) {
                    Global.waiting_to_add_tower = Tower_Farthest::class.java.simpleName
                }
            }
        }
        btns.values.forEach { o.addComponent(it)}
        o.pack()
        p.addComponent(o)
    }

    override fun canvas(): HudPanel = p
}