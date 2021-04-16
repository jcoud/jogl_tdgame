package me.jikud.tdgame.gui.sets

import me.jikud.tdgame.TDMain
import me.jikud.tdgame.gui.GUI
import me.jikud.tdgame.gui.hud.components.HudButton
import me.jikud.tdgame.gui.hud.components.HudComponent
import me.jikud.tdgame.gui.hud.components.HudPanel
import me.jikud.tdgame.gui.hud.components.HudText
import me.jikud.tdgame.gui.hud.event.EventMouseAction
import me.jikud.tdgame.gui.hud.event.GUIEventType
import me.jikud.tdgame.gui.hud.event.MouseActionAbstract
import me.jikud.tdgame.helpers.CColor
import me.jikud.tdgame.helpers.PPoint
import me.jikud.tdgame.world.obj.TileObj
import me.jikud.tdgame.world.obj.towers.Tower
import java.awt.Color

class TowerGUI : GUI() {

    private var tower: Tower? = null
    private val p = HudPanel(TDMain.def_gui_width, TDMain.def_gui_height)
    private val text_hud = HudText(TDMain.def_gui_height, TDMain.def_gui_height, if (tower == null) "" else tower!!.name)

    private val btns = mapOf(
        Pair("destroy", HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Destroy")),
        Pair("shooting_mode", HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Shooting mode")),
        Pair("level_up", HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Level Up")),
    )

    fun setTower(tower: Tower) {
        if (this.tower == tower) return
        this.tower = tower
        text_hud.background = CColor(tower.color)
        btns.values.forEach { it.active = true }
    }

    init {
        p.pos = PPoint(0, TDMain.fieldHeight)
        p.background = CColor(Color.CYAN)
        p.alignment = HudComponent.Alignment.HORIZONTAL

        text_hud.showBackground = true
        text_hud.textColor = CColor(Color.BLACK)

        btns["destroy"]!!.action = object : MouseActionAbstract(btns["destroy"]!!) {
            override fun mouseAction(e: EventMouseAction) {
                //                if (tower == null) return
                super.mouseAction(e)
                if (e.type == GUIEventType.CLICKED) {
                    tower?.state = TileObj.TileObjState.DEAD
                    btns.values.forEach { it.active = false }
                }

            }
        }

        btns["shooting_mode"]!!.action = object : MouseActionAbstract(btns["shooting_mode"]!!) {
            override fun mouseAction(e: EventMouseAction) {
                super.mouseAction(e)
                if (e.type == GUIEventType.CLICKED) {
                    tower!!.mode = tower!!.mode.next()
//                    btns["shooting_mode].active = false
                }

            }
        }
        btns["level_up"]!!.action = object : MouseActionAbstract(btns["level_up"]!!) {
            override fun mouseAction(e: EventMouseAction) {
                super.mouseAction(e)
                if (e.type == GUIEventType.CLICKED) {
                    println("level up ${tower?.name}")
//                    tower?.state = TileObj.TileObjState.DEAD
//                    btns["level_up].active = false
                }

            }
        }

        p.addComponent(text_hud)
        btns.values.forEach { p.addComponent(it) }
        p.pack()
    }

    override fun canvas(): HudPanel = p
}