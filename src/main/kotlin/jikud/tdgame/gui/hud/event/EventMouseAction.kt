package jikud.tdgame.gui.hud.event

import java.awt.Point

open class EventMouseAction(open val pos: Point, open val type: GUIEventType) {
    class EMAClicker(pos: Point, type: GUIEventType, var button: Short) : EventMouseAction(pos, type)
}