package jikud.tdgame.world.obj.gate

import jikud.tdgame.TDMain
import jikud.tdgame.core.Drawing
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

    init {
        initTimer()
    }

    override fun update() {
        spawnTimer = 1.0
        spawn()
        super.update()
    }

    private fun spawn() {
        startInternalTimer()
        if (!timer!!.trigger(spawnTimer)) return
        val e = TileObjUtils.makeWithRandomParams<Entity>(PPoint(this.center))
        e.target = Field.nodeListOrder[1]
        FieldProcessorQueue.queueAdd(e)

    }

    override fun show() {
        val c = CColor(this.color)
        Drawing.GL.glColor3f(c.r, c.g, c.b)
        Drawing.Rect(
            this.pos.x - TDMain.bs / 2,
            this.pos.y - TDMain.bs / 2,
            TDMain.bs * 1f,
            TDMain.bs * 1f,
            false,
            3f
        )
        super.show()
    }

    override fun showName(customName: String) {
        super.showName("$customName T${timer!!.current} SP: $spawnTimer")
    }
}