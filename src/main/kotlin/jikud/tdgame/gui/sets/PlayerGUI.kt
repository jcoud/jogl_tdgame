package jikud.tdgame.gui.sets

import jikud.tdgame.TDMain
import jikud.tdgame.gui.GUI
import jikud.tdgame.gui.hud.components.HudButton
import jikud.tdgame.gui.hud.components.HudComponent
import jikud.tdgame.gui.hud.components.HudPanel
import jikud.tdgame.gui.hud.components.HudText
import jikud.tdgame.gui.hud.event.EventMouseAction
import jikud.tdgame.gui.hud.event.IMouseAction
import jikud.tdgame.helpers.CColor
import jikud.tdgame.helpers.PPoint
import java.awt.Color

class PlayerGUI: GUI() {
    private val p = HudPanel()
    init {
        p.pos = PPoint(0, TDMain.fieldHeight)
        p.width = TDMain.guiWidth
        p.height = TDMain.guiHeight
        p.background = CColor(Color.MAGENTA)
        val o = HudPanel()
        o.background = CColor(Color.LIGHT_GRAY)
        o.alignment = HudComponent.Alignment.HORIZONTAL
        o.addComponent(HudText("12").apply { width = TDMain.guiHeight; height = TDMain.guiHeight })
        o.addComponent(HudText("12").apply { width = TDMain.guiHeight; height = TDMain.guiHeight })
        o.addComponent(HudText("12").apply { width = TDMain.guiHeight; height = TDMain.guiHeight })
        o.addComponent(HudText("12").apply { width = TDMain.guiHeight; height = TDMain.guiHeight })
        o.addComponent(HudButton().apply { width = TDMain.guiHeight; height = TDMain.guiHeight
            action = object : IMouseAction {
                override fun mouseAction(e: EventMouseAction) {
                    eventType = e.type
                }
            }
        })
        o.pack()
        p.addComponent(o)
    }

    override fun canvas(): HudPanel = p
}