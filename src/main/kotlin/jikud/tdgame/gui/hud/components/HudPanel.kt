package jikud.tdgame.gui.hud.components

import jikud.tdgame.helpers.PPoint
import kotlin.math.max

class HudPanel : HudComponent() {

    val components = ArrayList<HudComponent>()

    init {
        showBackground = true
    }

    override fun draw() {
        super.draw()
        components.forEach {it.draw() }
    }

    fun pack() {
        when (alignment) {
            Alignment.HORIZONTAL -> {
                width = 0
                for (c in components.indices) {
                    if (c != 0) components[c].pos = PPoint(components[c].pos).translate(width.toFloat(), 0f)
                    width += components[c].width
                    height = max(height, components[c].height)
                }
            }
            Alignment.VERTICAL -> {
                height = 0
                for (c in components.indices) {
                    if (c != 0) components[c].pos = PPoint(components[c].pos).translate(0f, height.toFloat())
                    height += components[c].height
                    width = max(width, components[c].width)
                }
            }
            else -> return
        }
    }

    //    fun addComponent(c: HudComponent) = addComponent(c, false)
    fun addComponent(c: HudComponent) {
        try {
            if (this == c) throw Error("wtf chel, you adding yourself to component list")
            c.anchor = this.pos
            components.add(c)
            applyParentSettings(this)
        } catch (e: Error) {
            e.printStackTrace()
        }
    }

    private fun applyParentSettings(c: HudPanel) {
        for (co in c.components) {
            co.anchor = c.anchor.concat(c.pos)
            if (co is HudPanel) {
                applyParentSettings(co)
            }
        }
    }

    fun removeComponent(c: HudComponent) {
        if (components.contains(c))
            components.remove(c)
    }
}