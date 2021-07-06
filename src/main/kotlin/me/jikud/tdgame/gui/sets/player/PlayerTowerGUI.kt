package me.jikud.tdgame.gui.sets.player

import me.jikud.engine.core.gui.GUI
import me.jikud.engine.core.gui.event.mouse.AMouseAction
import me.jikud.engine.core.gui.event.mouse.EventMouseAction
import me.jikud.engine.core.gui.hud.HudButton
import me.jikud.engine.core.gui.hud.HudComponent
import me.jikud.engine.core.gui.hud.HudPanel
import me.jikud.engine.core.gui.hud.HudText
import me.jikud.engine.core.gui.marks.IInGameGui
import me.jikud.engine.core.helpers.CColor
import me.jikud.engine.core.helpers.PPoint
import me.jikud.tdgame.TDMain
import me.jikud.tdgame.world.obj.TileObj
import me.jikud.tdgame.world.obj.towers.Tower
import java.awt.Color

class PlayerTowerGUI : GUI(), PlayerGroup {
    private var tower: Tower? = null
    private val p = HudPanel(TDMain.def_gui_width, TDMain.def_gui_height)
    private val text_hud =
        HudText(TDMain.def_gui_height, TDMain.def_gui_height, if (tower == null) "" else tower!!.name)

    private val btns = mapOf(
        Pair("destroy", HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Destroy")),
        Pair("shooting_mode", HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Shooting mode")),
        Pair("level_up", HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Level Up")),
    )

    fun setTower(tower: Tower) {
        if (this.tower == tower) return
        this.tower = tower
        text_hud.text = tower.name
        text_hud.textColor = CColor(Color.BLACK)
        text_hud.background = CColor(tower.color)
        btns.values.forEach { it.active = true }
    }

    init {
    p.pos = PPoint(0, 0)
        p.background = CColor(Color.CYAN)
        p.alignment = HudComponent.Alignment.HORIZONTAL

        text_hud.showBackground = true
        text_hud.textColor = CColor(Color.BLACK)

        btns["destroy"]!!.mouseAction = object : AMouseAction() {
            override fun mouseClicked(e: EventMouseAction) {
                tower?.state = TileObj.TileObjState.DEAD
                btns.values.forEach { it.active = false }
            }
        }

        btns["shooting_mode"]!!.mouseAction = object : AMouseAction() {
            override fun mouseClicked(e: EventMouseAction) {
                tower!!.mode = tower!!.mode.next()
            }
        }
        btns["level_up"]!!.mouseAction = object : AMouseAction() {
            override fun mouseClicked(e: EventMouseAction) {}
        }

        p.addComponent(text_hud)
        btns.values.forEach { p.addComponent(it) }
//        p.pack()
    }

    override fun canvas(): HudPanel = p
}