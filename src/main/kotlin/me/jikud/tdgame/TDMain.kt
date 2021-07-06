package me.jikud.tdgame

import me.jikud.engine.core.Engine
import me.jikud.engine.core.EngineSettings
import me.jikud.engine.core.helpers.FFont
import me.jikud.tdgame.gui.sets.GameGuiReg
import me.jikud.tdgame.mapping.MapImpl
import me.jikud.tdgame.world.WorldInteraction
import me.jikud.tdgame.world.field.Field
import me.jikud.tdgame.world.field.FieldProcessorQueue
import java.awt.Font

/**
 * Код полностью написан одник разработчиком. Начало создания игры: ~18.11.2020
 * Автор: Andrew jikud (jcoud) Swan
 * Github: https://github.com/jcoud
 *
 * Концепция классического Tower Defense.
 * Особенности:
 * - башни повышают уровеь в зависимости от количества убитых существ
 * - башни могут улучшаться за монеты
 * - существа имеют разные харрактеристики в группе нападения в текущем раунде
 * - существа повышают уровень с каждым новым раундом
 * - игровое поле можно изменять в редакторе карт
 * - перед началом игры можно выбрать игровое поле
 * - игру можно ставить на паузу (клав. 'P' англ.)
 * - игрок имеет счёт за игровую сессию, итория счётов отображается в меню
 */


class TDMain : Engine() {
    companion object {
        const val n = 20
        const val bs = 30
        const val fieldWidth = bs * n
        const val fieldHeight = bs * n
        const val fieldHeightGap = 4
        const val def_gui_width = bs * n
        const val def_gui_height = bs * 2
        const val guiWithGapHeight = def_gui_height + fieldHeightGap
        const val TITLE = "JOGL TD Game" // window's title

        @JvmStatic
        fun main(args: Array<String>) {
            TDMain()
        }
    }


    init {
        val s = EngineSettings("settings.toml")
        s.windowWidth = fieldWidth
        s.windowHeight = fieldHeight + fieldHeightGap + def_gui_height
        s.title = TITLE
        setSettings(s)
        setEventProcessor(WorldInteraction())
        setGuiRegister(GameGuiReg())
        loadStartMenu()
        start()
    }

    override fun init() {
        MapImpl.init()
        Field.init()
    }

    override fun render() {
        GameDraw.visualize()
    }

    override fun update() {
        Field.update()
        FieldProcessorQueue.unloadActionQueue()
    }
}