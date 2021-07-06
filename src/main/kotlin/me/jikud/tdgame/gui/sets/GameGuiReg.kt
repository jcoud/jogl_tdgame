package me.jikud.tdgame.gui.sets

import me.jikud.engine.core.gui.GuiCore
import me.jikud.engine.core.gui.GuiRegister
import me.jikud.tdgame.gui.sets.editor.EditorNodeGUI
import me.jikud.tdgame.gui.sets.editor.EditorTowerGUI
import me.jikud.tdgame.gui.sets.menu.EditorGUI
import me.jikud.tdgame.gui.sets.menu.PlayerGUI
import me.jikud.tdgame.gui.sets.player.PlayerTowerGUI

class GameGuiReg: GuiRegister {
    override fun registrateGui(registrator: GuiCore.GuiRegistration) {
        registrator.register<EditorGUI>()
        registrator.register<EditorTowerGUI>()
        registrator.register<EditorNodeGUI>()
        registrator.register<MenuGUI>()
        registrator.register<PlayerGUI>()
        registrator.register<PlayerTowerGUI>()
    }
}