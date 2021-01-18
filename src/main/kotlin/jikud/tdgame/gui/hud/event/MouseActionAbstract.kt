package jikud.tdgame.gui.hud.event

import jikud.tdgame.gui.hud.components.HudButton

abstract class MouseActionAbstract(private val source: HudButton) : IMouseAction {
    override fun mouseAction(e: EventMouseAction) {
        source.eventType = e.type
    }
}