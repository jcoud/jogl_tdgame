package me.jikud.tdgame.gui.sets

import me.jikud.engine.core.EngineStates
import me.jikud.engine.core.gui.GuiCore
import me.jikud.tdgame.TDMain
import me.jikud.tdgame.Global
import me.jikud.engine.core.main.JOGLEntry
import me.jikud.engine.core.gui.marks.MainMenuGui
import me.jikud.engine.core.gui.event.mouse.AMouseAction
import me.jikud.engine.core.gui.hud.HudButton
import me.jikud.engine.core.gui.hud.HudComponent
import me.jikud.engine.core.gui.hud.HudPanel
import me.jikud.engine.core.gui.event.mouse.EventMouseAction
import me.jikud.tdgame.gui.sets.editor.EditorNodeGUI
import me.jikud.tdgame.gui.sets.editor.EditorTowerGUI
import me.jikud.tdgame.gui.sets.menu.EditorGUI
import me.jikud.engine.core.helpers.CColor
import me.jikud.engine.core.helpers.PPoint
import me.jikud.tdgame.GameDraw
import me.jikud.tdgame.gui.sets.editor.EditorGuiGroup
import me.jikud.tdgame.world.field.Field
import java.awt.Color
import kotlin.system.exitProcess

class MenuGUI : MainMenuGui() {
    private val p = HudPanel(TDMain.fieldWidth / 2, TDMain.fieldHeight / 2)

    init {
        EngineStates.draw = false
        p.pos = PPoint((JOGLEntry.INIT_WIDTH - p.width) / 2, (JOGLEntry.INIT_HEIGHT - p.height) / 2)
        p.background = CColor(Color.PINK)
        val o = HudPanel(HudComponent.Alignment.VERTICAL)
        val playButton = HudButton(80, 30, "Play")
        val editorButton = HudButton(80, 30, "Map Editor")
        val exitButton = HudButton(80, 30, "Exit")
        playButton.mouseAction = object : AMouseAction() {
            override fun mouseClicked(e: EventMouseAction) {
                playButtonAction()
            }
        }
        editorButton.mouseAction = object : AMouseAction() {
            override fun mouseClicked(e: EventMouseAction) {
                editorButtonAction()
            }
        }
        exitButton.mouseAction = object : AMouseAction() {
            override fun mouseClicked(e: EventMouseAction) {
                exitButtonAction()
            }
        }
        o.addComponent(playButton)
        o.addComponent(editorButton)
        o.addComponent(exitButton)
        o.pos = PPoint((p.width - o.width) / 2, (p.height - o.height) / 2)
        p.addComponent(o)
        p.pack()
    }

    private fun exitButtonAction() {
        exitProcess(0)
    }

    private fun editorButtonAction() {
        Global.TileTimer_isUpdatable = false
        EngineStates.draw = true
        Field.task = Field.TaskType.RUN
        GuiCore.switchToAnotherGuiGroup(GuiCore.getGui<EditorGUI>())
    }

    private fun playButtonAction() {
        Global.TileTimer_isUpdatable = true
        EngineStates.draw = true
        Field.task = Field.TaskType.RUN
        GuiCore.switchToAnotherGuiGroup(
            when (GuiCore.previousGui) {
                null, is EditorGuiGroup -> GuiCore.inGameGui!!
                else -> GuiCore.previousGui!!
            }
        )
    }

    override fun canvas(): HudPanel = p
}