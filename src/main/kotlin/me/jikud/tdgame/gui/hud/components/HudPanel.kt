package me.jikud.tdgame.gui.hud.components

import me.jikud.tdgame.helpers.PPoint
import kotlin.math.max

class HudPanel(width: Int = 0, height: Int = 0, alignment: Alignment = Alignment.NONE) : HudComponent(width, height, alignment) {
    constructor(alignment: Alignment) : this(width = 0, height = 0, alignment)

    val components = ArrayList<HudComponent>()

    init {
        showBackground = true
    }

    override fun draw() {
        super.draw()
        components.forEach { it.draw() }
    }

    override fun post() {
        super.post()
        components.forEach { it.post() }
    }

    fun pack() {
        when (alignment) {
            Alignment.HORIZONTAL -> {
                width = 0
                for (c in components.indices) {
                    val _c = components[c]
                    if (c > 0) _c.pos = PPoint(_c.pos).translate(width.toFloat(), 0f)
                    width += _c.width
                    height = max(height, _c.height)
//                    if (_c is HudPanel) _c.pack()
                }
            }
            Alignment.VERTICAL -> {
                height = 0
                for (c in components.indices) {
                    val _c = components[c]
                    if (c > 0) _c.pos = PPoint(_c.pos).translate(0f, height.toFloat())
                    height += _c.height
                    width = max(width, _c.width)
//                    if (_c is HudPanel) _c.pack()

                }
            }
            else -> return
        }
    }

    //    fun addComponent(c: HudComponent) = addComponent(c, false)
    fun addComponent(c: HudComponent) {
        try {
            if (this == c) throw Error("wtf chel, you adding yourself to component list")
            c.anchor = PPoint(this.pos)
//            c.parent = this
            components.add(c)
            applyParentSettings(this)
        } catch (e: Error) {
            e.printStackTrace()
        }
    }

    private fun applyParentSettings(c: HudPanel) {
        for (co in c.components) {
            co.anchor = c.anchor.add(c.pos)
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