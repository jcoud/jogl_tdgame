package me.jikud.tdgame.gui.sets.editor

import me.jikud.engine.core.gui.event.mouse.AMouseAction
import me.jikud.engine.core.gui.event.mouse.EventMouseAction
import me.jikud.engine.core.gui.hud.HudButton
import me.jikud.engine.core.gui.hud.HudComponent
import me.jikud.engine.core.gui.hud.HudPanel
import me.jikud.engine.core.gui.hud.HudText
import me.jikud.engine.core.helpers.CColor
import me.jikud.engine.core.helpers.PPoint
import me.jikud.tdgame.TDMain
import me.jikud.tdgame.world.obj.TileObj
import me.jikud.tdgame.world.obj.towers.Tower

class EditorTowerGUI : EditorGuiGroup() {
    private val p = HudPanel(HudComponent.Alignment.HORIZONTAL)

    private var tower: Tower? = null
    private val hudText = HudText(TDMain.def_gui_height, TDMain.def_gui_height, if (tower != null) tower!!.name else "")

    fun setElement(tower: Tower) {
        if (this.tower == tower) return
        this.tower = tower
        hudText.text = tower.name
        hudText.textColor = CColor(tower.color)
        btns.values.forEach { it.active = true }
    }

    private val btns = mapOf(
        Pair("move", HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Move")),
        Pair("delete", HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Delete")),
        Pair("upgrade", HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Upgrade"))
    )

    init {
        p.pos = PPoint(0, 0)
        btns["move"]?.apply {
            mouseAction = object : AMouseAction() {
                override fun mouseClicked(e: EventMouseAction) {}
            }
        }
        btns["delete"]?.apply {
            mouseAction = object : AMouseAction() {
                override fun mouseClicked(e: EventMouseAction) {
                    tower?.state = TileObj.TileObjState.DEAD
                    btns.values.forEach { it.active = false }
                }
            }
        }
        btns["upgrade"]?.apply {
            mouseAction = object : AMouseAction() {
                override fun mouseClicked(e: EventMouseAction) {}
            }
        }
        p.addComponent(hudText)
        btns.values.forEach { p.addComponent(it) }
        p.pack()

    }

    override fun canvas() = p
}