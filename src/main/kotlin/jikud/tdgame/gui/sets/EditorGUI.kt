package jikud.tdgame.gui.sets

import jikud.tdgame.TDMain
import jikud.tdgame.gui.GUI
import jikud.tdgame.gui.hud.components.HudButton
import jikud.tdgame.gui.hud.components.HudComponent
import jikud.tdgame.gui.hud.components.HudPanel
import jikud.tdgame.gui.hud.event.EventMouseAction
import jikud.tdgame.gui.hud.event.MouseActionAbstract
import jikud.tdgame.helpers.CColor
import jikud.tdgame.helpers.PPoint
import jikud.tdgame.mapping.MapImpl
import java.awt.Color

class EditorGUI : GUI() {
    private val p = HudPanel()
    override fun canvas(): HudPanel = p

    init {
        p.pos = PPoint(0, TDMain.fieldHeight)
        p.width = TDMain.guiWidth
        p.height = TDMain.guiHeight
        p.background = CColor(Color.GRAY)
        p.alignment = HudComponent.Alignment.HORIZONTAL
        p.addComponent(HudButton("Save").apply {
            width = TDMain.guiWidth
            height = TDMain.guiWidth
            textColor = CColor(Color.GREEN)
            action = object : MouseActionAbstract(this) {
                override fun mouseAction(e: EventMouseAction) {
                    super.mouseAction(e)
                    MapImpl.save()
                }
            }
        })
        val box1 = HudPanel().apply {
            width = TDMain.guiWidth
            height = TDMain.guiWidth
        }
        box1.addComponent(HudButton("Node").apply {
            width = TDMain.guiWidth / 2
            height = TDMain.guiWidth / 2
        })
        box1.addComponent(HudButton("Starter").apply {
            width = TDMain.guiWidth / 2
            height = TDMain.guiWidth / 2
            pos = PPoint(width.toFloat(), 0f)
        })
        box1.addComponent(HudButton("Ender").apply {
            width = TDMain.guiWidth / 2
            height = TDMain.guiWidth / 2
            pos = PPoint(0f, height.toFloat())
        })
        p.addComponent(box1)
        p.pack()
    }
}