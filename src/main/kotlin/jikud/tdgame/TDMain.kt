package jikud.tdgame

import jikud.tdgame.gui.GuiCore
import jikud.tdgame.mapping.MapImpl
import jikud.tdgame.world.Field
import jikud.tdgame.world.FieldProcessorQueue
class TDMain {
    companion object {
        const val n = 20
        const val bs = 30
        const val fieldWidth = bs * n
        const val fieldHeight = bs * n
        const val guiWidth = bs * n
        const val guiHeight = bs * 2
    }

    init {
//        Window.init()
        MapImpl.init()
        Field.init()
        FieldProcessorQueue.init()
//        Engine.start()

        GuiCore.switchToAnotherGuiGroup(GuiCore.menuGUI)
    }
}