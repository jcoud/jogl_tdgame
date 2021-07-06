package me.jikud.tdgame.world.obj.gates

import me.jikud.engine.core.helpers.CColor
import me.jikud.engine.core.helpers.PPoint
import me.jikud.engine.core.main.GLRenderHelper
import me.jikud.tdgame.world.obj.NodePoint

class Ender(pos: PPoint, name: String, color: Int) : NodePoint(pos, name, color) {

    override fun draw() {
        val c = CColor(this.color)
        GLRenderHelper.JGL.glColor3f(c.r, c.g, c.b)
        GLRenderHelper.Rect(
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