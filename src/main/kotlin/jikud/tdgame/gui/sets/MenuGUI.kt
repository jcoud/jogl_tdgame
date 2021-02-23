package jikud.tdgame.gui.sets

import jikud.tdgame.TDMain
import jikud.tdgame.core.Drawing
import jikud.tdgame.core.Global
import jikud.tdgame.gui.GUI
import jikud.tdgame.gui.GuiCore
import jikud.tdgame.gui.hud.components.HudButton
import jikud.tdgame.gui.hud.components.HudComponent
import jikud.tdgame.gui.hud.components.HudPanel
import jikud.tdgame.gui.hud.event.EventMouseAction
import jikud.tdgame.gui.hud.event.GUIEventType
import jikud.tdgame.gui.hud.event.MouseActionAbstract
import jikud.tdgame.helpers.CColor
import jikud.tdgame.world.Field
import java.awt.Color
import kotlin.system.exitProcess

class MenuGUI : GUI() {

    private val p = HudPanel()

    init {
        p.width = TDMain.fieldWidth / 2
        p.height = TDMain.fieldHeight / 2
        p.background = CColor(Color.PINK)
        val o = HudPanel()
        o.alignment = HudComponent.Alignment.VERTICAL
        val playButton = HudButton("Play").apply { width = 80; height = 30 }
        val editorButton = HudButton("Editor").apply { width = 80; height = 30 }
        val exitButton = HudButton("Exit").apply { width = 80; height = 30 }
        playButton.action = object : MouseActionAbstract(playButton) {
            override fun mouseAction(e: EventMouseAction) {
                super.mouseAction(e)
                if (e.type == GUIEventType.CLICKED) {
                    menuPlay()
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
        p.addComponent(o)
    }

    private fun exitButtonAction() {
        exitProcess(0)
    }

    private fun editorButtonAction() {
        Global.TileTimer_isUpdatable = false
        Drawing.drawWorld = true
        Field.task = Field.TaskType.NONE
        GuiCore.switchToAnotherGuiGroup(GuiCore.editorGUI)
    }

    private fun menuPlay() {
        Global.TileTimer_isUpdatable = true
        Drawing.drawWorld = true
        Field.task = Field.TaskType.RUN
        GuiCore.switchToAnotherGuiGroup(if (GuiCore.previous_gui == null) GuiCore.playerGUI else GuiCore.previous_gui!!)
    }

    override fun canvas(): HudPanel = p
}