package jikud.tdgame.world.obj.gate

import jikud.tdgame.JOGLEntry
import jikud.tdgame.TDMain
import jikud.tdgame.core.Drawing
import jikud.tdgame.helpers.CColor
import jikud.tdgame.helpers.PPoint
import jikud.tdgame.world.obj.NodePoint

class Ender(pos: PPoint, name: String, color: Int) : NodePoint(pos, name, color) {

    override fun show() {
        val c = CColor(this.color)
        JOGLEntry.GRF.glColor3f(c.r, c.g, c.b)
        Drawing.drawLineRect(
            this.pos.xi - TDMain.bs / 2f,
            this.pos.yi - TDMain.bs / 2f,
            TDMain.bs * 1f,
            TDMain.bs * 1f,
            3f
        )
        super.show()
    }
}