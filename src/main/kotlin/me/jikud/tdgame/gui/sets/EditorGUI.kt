package me.jikud.tdgame.gui.sets

import me.jikud.tdgame.TDMain
import me.jikud.tdgame.gui.GUI
import me.jikud.tdgame.gui.hud.components.HudButton
import me.jikud.tdgame.gui.hud.components.HudComponent
import me.jikud.tdgame.gui.hud.components.HudPanel
import me.jikud.tdgame.gui.hud.components.HudText
import me.jikud.tdgame.gui.hud.event.EventMouseAction
import me.jikud.tdgame.gui.hud.event.MouseActionAbstract
import me.jikud.tdgame.helpers.CColor
import me.jikud.tdgame.helpers.PPoint
import me.jikud.tdgame.mapping.MapImpl
import me.jikud.tdgame.world.obj.NodePoint
import me.jikud.tdgame.world.obj.towers.Tower
import java.awt.Color

class EditorGUI : GUI() {
    private val p = HudPanel(HudComponent.Alignment.HORIZONTAL)
    private var tower: Tower? = null
    private var node: NodePoint? = null
    private val text_hud = HudText(
        TDMain.def_gui_height, TDMain.def_gui_height,
        if (tower != null) tower!!.name
        else if (node != null) node!!.name
        else ""
    )

    fun setElement(node: NodePoint) {
        if (this.node == node) return
        this.node = node
        text_hud.background = CColor(node.color)
    }

    fun setElement(tower: Tower) {
        if (this.tower == tower) return
        this.tower = tower
        text_hud.background = CColor(tower.color)
//        btns.values.forEach { it.active = true }
    }

    init {
        p.pos = PPoint(0, TDMain.fieldHeight)
        p.background = CColor(Color.GRAY)
        p.addComponent(HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Save").apply {
            textColor = CColor(Color.GREEN)
            action = object : MouseActionAbstract(this) {
                override fun mouseAction(e: EventMouseAction) {
                    super.mouseAction(e)
                    MapImpl.save()
                }
            }
        })
        //todo: при паковке панели в панеле, его компоненты не перемещаются в соответсвтии с координатами этой панели
        val box = HudPanel(HudComponent.Alignment.HORIZONTAL)
        box.pos = PPoint(TDMain.def_gui_height, TDMain.def_gui_height)
        box.addComponent(HudButton(TDMain.def_gui_height / 2, TDMain.def_gui_height / 2, "Node"))
        box.addComponent(HudButton(TDMain.def_gui_height / 2, TDMain.def_gui_height / 2,"Starter"))
        box.addComponent(HudButton(TDMain.def_gui_height / 2, TDMain.def_gui_height / 2, "Ender"))
        box.pack()
        p.addComponent(box)
        p.pack()
    }

    override fun canvas(): HudPanel = p
}
