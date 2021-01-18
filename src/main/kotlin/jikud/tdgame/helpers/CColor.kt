package jikud.tdgame.helpers

import java.awt.Color

class CColor(var r: Float, var g: Float, var b: Float, var a: Float) {
    val color: Color get() = Color(r, g, b, a)
    constructor(r: Float, b: Float, g: Float) : this(r, g, b, 1.0f)
    constructor(c: Color) : this(c.red / 255f, c.green / 255f, c.blue / 255f, c.alpha / 255f)
    constructor(rgb: Int) : this(Color(rgb))
}