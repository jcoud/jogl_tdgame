package me.jikud.tdgame.world.obj.gates

import me.jikud.tdgame.core.Drawing
import me.jikud.tdgame.core.Global
import me.jikud.tdgame.core.Timer
import me.jikud.tdgame.helpers.CColor
import me.jikud.tdgame.helpers.PPoint
import me.jikud.tdgame.world.field.Field
import me.jikud.tdgame.world.field.FieldProcessorQueue
import me.jikud.tdgame.world.obj.Entity
import me.jikud.tdgame.world.obj.NodePoint
import me.jikud.tdgame.world.obj.TileObjUtils


class Starter(pos: PPoint, name: String, color: Int) : NodePoint(pos, name, color) {
    //    private var prevInstanceOfObj: Entity? = null
    private var spawnTimer = .0

    private var timer = Timer()


     private fun startTimer() {
        if (awakened) return
        awakened = true
        timer.start()
    }

    override fun update() {
        spawnTimer = 1.0
        spawn()
        super.update()
    }

    private fun spawn() {
        if (!Global.TileTimer_isUpdatable) return
        startTimer()
        if (!timer.trigger(spawnTimer)) return
        val e = TileObjUtils.makeWithRandomParams<Entity>(PPoint(this.center))
        e.target = Field.nodeListOrder[1]
        FieldProcessorQueue.queueToAdd(e)

    }

    override fun draw() {
        val c = CColor(this.color)
        Drawing.GL.glColor3f(c.r, c.g, c.b)
        Drawing.Rect(
            this.pos.x - me.jikud.tdgame.TDMain.bs / 2,
            this.pos.y - me.jikud.tdgame.TDMain.bs / 2,
            me.jikud.tdgame.TDMain.bs * 1f,
            me.jikud.tdgame.TDMain.bs * 1f,
            false,
            3f
        )
        super.draw()
    }

    override fun drawName(customName: String) {
        super.drawName("$customName TA${timer.actual} TU${timer.current} SP: $spawnTimer")
    }
}