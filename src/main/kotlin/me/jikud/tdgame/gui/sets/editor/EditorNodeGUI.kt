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
import me.jikud.tdgame.world.obj.NodePoint
import me.jikud.tdgame.world.obj.TileObj

class EditorNodeGUI : EditorGuiGroup() {

    override fun canvas(): HudPanel = p

    private val p = HudPanel(HudComponent.Alignment.HORIZONTAL)

    private var node: NodePoint? = null
    private val hudText = HudText(TDMain.def_gui_height, TDMain.def_gui_height, if (node != null) node!!.name else "")

    fun setElement(node: NodePoint) {
        if (this.node == node) return
        this.node = node
        hudText.text = node.name
        hudText.textColor = CColor(node.color)
        btns.values.forEach { it.active = true }
    }

    private val btns = mapOf(
        Pair("move", HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "Move")),
        Pair("delete", HudButton(TDMain.def_gui_height, TDMain.def_gui_height, "delete"))
    )


    init {
        p.pos = PPoint(0, 0)
        btns["move"]?.apply {
            mouseAction = object : AMouseAction() {
                override fun mouseClicked(e: EventMouseAction) {
                }
            }
        }
        btns["delete"]?.apply {
            mouseAction = object : AMouseAction() {
                override fun mouseClicked(e: EventMouseAction) {
                    node?.state = TileObj.TileObjState.DEAD
                    btns.values.forEach { it.active = false }

                }
            }

            p.addComponent(hudText)
            btns.values.forEach { p.addComponent(it) }
            p.pack()
        }
    }

}