package jikud.tdgame.world.obj.gate

import jikud.tdgame.JOGLEntry
import jikud.tdgame.TDMain
import jikud.tdgame.core.Drawing
import jikud.tdgame.core.GameTime
import jikud.tdgame.helpers.CColor
import jikud.tdgame.helpers.PPoint
import jikud.tdgame.world.Field
import jikud.tdgame.world.FieldProcessorQueue
import jikud.tdgame.world.obj.Entity
import jikud.tdgame.world.obj.NodePoint
import jikud.tdgame.world.obj.TileObjUtils


class Starter(pos: PPoint, name: String, color: Int) : NodePoint(pos, name, color) {
    //    private var prevInstanceOfObj: Entity? = null
    private var spawnTimer = .0

    override fun update() {
        spawnTimer = 1.0
        spawn()
        super.update()
    }

    private val timer = GameTime.Timer()

    private fun spawn() {
        if (!timer.trigger(spawnTimer)) return
        val e = TileObjUtils.makeWithRandomParams<Entity>(PPoint(this.center))
        e.target = Field.nodeListOrder[1]
        FieldProcessorQueue.queueAdd(e)

    }

    override fun show() {
        val c = CColor(this.color)
        JOGLEntry.GRF.glColor3f(c.r, c.g, c.b)
        Drawing.drawLineRect(
            this.pos.x - TDMain.bs / 2,
            this.pos.y - TDMain.bs / 2,
            TDMain.bs * 1f,
            TDMain.bs * 1f,
            3f
        )
        super.show()
    }
}