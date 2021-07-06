package me.jikud.tdgame.gui.sets.menu

import me.jikud.engine.core.gui.event.mouse.AMouseAction
import me.jikud.engine.core.gui.event.mouse.EventMouseAction
import me.jikud.engine.core.gui.hud.HudButton
import me.jikud.engine.core.gui.hud.HudComponent
import me.jikud.engine.core.gui.hud.HudPanel
import me.jikud.engine.core.gui.marks.IInGameGui
import me.jikud.engine.core.gui.marks.InGameGui
import me.jikud.engine.core.helpers.CColor
import me.jikud.engine.core.helpers.PPoint
import me.jikud.tdgame.Global
import me.jikud.tdgame.TDMain
import me.jikud.tdgame.gui.sets.player.PlayerGroup
import me.jikud.tdgame.world.obj.towers.Tower_Closest
import me.jikud.tdgame.world.obj.towers.Tower_Farthest
import java.awt.Color

class PlayerGUI : InGameGui(), PlayerGroup {
    private val p = HudPanel(TDMain.def_gui_width, TDMain.def_gui_height)
    private val btns = mapOf(
        Pair("tower_type_c", HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Tower Closest")),
        Pair("tower_type_f", HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Tower Farthest")),
        Pair("tower_type_3", HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Tower 3")),
        Pair("tower_type_4", HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Tower 4"))
    )

    init {
        p.pos = PPoint(0, 0)
        p.background = CColor(Color.MAGENTA)
        val o = HudPanel()
        o.background = CColor(Color.LIGHT_GRAY)
        o.alignment = HudComponent.Alignment.HORIZONTAL
        btns["tower_type_c"]!!.mouseAction = object : AMouseAction() {
            override fun mouseClicked(e: EventMouseAction) {
                Global.singleQueue_addObjectToWorld = Tower_Closest::class.java.simpleName

            }
        }
        btns["tower_type_f"]!!.mouseAction = object : AMouseAction() {
            override fun mouseClicked(e: EventMouseAction) {
                Global.singleQueue_addObjectToWorld = Tower_Farthest::class.java.simpleName

            }
        }
        btns.values.forEach { o.addComponent(it) }
        p.addComponent(o)
        p.pack()
    }

    override fun canvas(): HudPanel = p
}