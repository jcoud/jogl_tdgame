package me.jikud.tdgame.world.obj.gates

import me.jikud.tdgame.core.Drawing
import me.jikud.tdgame.helpers.CColor
import me.jikud.tdgame.helpers.PPoint
import me.jikud.tdgame.world.obj.NodePoint

class Ender(pos: PPoint, name: String, color: Int) : NodePoint(pos, name, color) {

    override fun draw() {
        val c = CColor(this.color)
        Drawing.GL.glColor3f(c.r, c.g, c.b)
        Drawing.Rect(
            this.pos.xi - me.jikud.tdgame.TDMain.bs / 2f,
            this.pos.yi - me.jikud.tdgame.TDMain.bs / 2f,
            me.jikud.tdgame.TDMain.bs * 1f,
            me.jikud.tdgame.TDMain.bs * 1f,
            false,
            3f
        )
        super.draw()
    }
}