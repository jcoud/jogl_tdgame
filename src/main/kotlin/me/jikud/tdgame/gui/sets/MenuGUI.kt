package me.jikud.tdgame.gui.sets

import me.jikud.tdgame.TDMain
import me.jikud.tdgame.core.Drawing
import me.jikud.tdgame.core.Global
import me.jikud.tdgame.core.JOGLEntry
import me.jikud.tdgame.gui.GUI
import me.jikud.tdgame.gui.GuiCore
import me.jikud.tdgame.gui.hud.components.HudButton
import me.jikud.tdgame.gui.hud.components.HudComponent
import me.jikud.tdgame.gui.hud.components.HudPanel
import me.jikud.tdgame.gui.hud.event.EventMouseAction
import me.jikud.tdgame.gui.hud.event.GUIEventType
import me.jikud.tdgame.gui.hud.event.MouseActionAbstract
import me.jikud.tdgame.helpers.CColor
import me.jikud.tdgame.helpers.PPoint
import me.jikud.tdgame.world.field.Field
import java.awt.Color
import kotlin.system.exitProcess

class MenuGUI : GUI() {

    private val p = HudPanel(TDMain.fieldWidth / 2, TDMain.fieldHeight / 2)

    init {
        p.pos = PPoint((JOGLEntry.INIT_WIDTH - p.width) / 2, (JOGLEntry.INIT_HEIGHT - p.height) / 2)
        p.background = CColor(Color.PINK)
        val o = HudPanel(HudComponent.Alignment.VERTICAL)
        val playButton = HudButton(80, 30,"Play")
        val editorButton = HudButton(80, 30,"Map Editor")
        val exitButton = HudButton(80, 30,"Exit")
        playButton.action = object : MouseActionAbstract(playButton) {
            override fun mouseAction(e: EventMouseAction) {
                super.mouseAction(e)
                if (e.type == GUIEventType.CLICKED) {
                    playButtonAction()
                }
            }
        }
        editorButton.action = object : MouseActionAbstract(editorButton) {
            override fun mouseAction(e: EventMouseAction) {
                super.mouseAction(e)
                if (e.type == GUIEventType.CLICKED) {
                    editorButtonAction()
                }
            }
        }
        exitButton.action = object : MouseActionAbstract(exitButton) {
            override fun mouseAction(e: EventMouseAction) {
                super.mouseAction(e)
                if (e.type == GUIEventType.CLICKED) {
                    exitButtonAction()
                }
            }
        }
        o.addComponent(playButton)
        o.addComponent(editorButton)
        o.addComponent(exitButton)
        o.pack()
        o.pos = PPoint((p.width - o.width) / 2, (p.height - o.height) / 2)
        p.addComponent(o)
    }

    private fun exitButtonAction() {
        exitProcess(0)
    }

    private fun editorButtonAction() {
        Global.TileTimer_isUpdatable = false
        Drawing.drawWorld = true
        Field.task = Field.TaskType.RUN
        GuiCore.switchToAnotherGuiGroup(GuiCore.editorGUI)
    }

    private fun playButtonAction() {
        Global.TileTimer_isUpdatable = true
        Drawing.drawWorld = true
        Field.task = Field.TaskType.RUN
        GuiCore.switchToAnotherGuiGroup(if (GuiCore.previous_gui == null) GuiCore.playerGUI else GuiCore.previous_gui!!)
    }

    override fun canvas(): HudPanel = p
}