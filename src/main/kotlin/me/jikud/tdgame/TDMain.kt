package me.jikud.tdgame

import me.jikud.tdgame.core.JOGLEntry
import me.jikud.tdgame.core.Loop
import me.jikud.tdgame.gui.GuiCore
import me.jikud.tdgame.mapping.MapImpl
import me.jikud.tdgame.world.field.Field

class TDMain {
    companion object {
        const val n = 20
        const val bs = 30
        const val fieldWidth = bs * n
        const val fieldHeight = bs * n
        const val def_gui_width = bs * n
        const val def_gui_height = bs * 2
        const val TITLE = "JOGL TD Game" // window's title

        @JvmStatic
        fun main(args: Array<String>) {
            TDMain()
        }
    }

    init {
        JOGLEntry.init()
        MapImpl.init()
        Field.init()
        Loop.start()
        GuiCore.switchToAnotherGuiGroup(GuiCore.menuGUI)
        GuiCore.postProcess()
    }
}