package jikud.tdgame.world.obj

import jikud.tdgame.JOGLEntry
import jikud.tdgame.core.Drawing
import jikud.tdgame.helpers.CColor
import jikud.tdgame.helpers.PPoint
import jikud.tdgame.world.Field
import jikud.tdgame.world.obj.gate.Ender
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


open class Entity(
    override var pos: PPoint,
    override var name: String,
    override var size: Float,
    override var color: Int,
    var speed: Float
) : TileObj(pos, name, size, color), IMovable {

    var health = 0
    var target = EmptyNode
    private var angle = .0f
    override var state = TileObjState.MOVING

    companion object {
        var count = 0
    }

    override fun update() {
        findEntityTarget()
        if (health <= 0) state = TileObjState.DEAD
        val g = center.devide(Field.gridSize)
        Field.grid[g.xi][g.yi].add(this)
        super.update()
    }

    private fun findEntityTarget() {
        if (state != TileObjState.WAITING) return
        val v = Field.nodeListOrder
        var index = v.indexOf(target)
        val currentNode = if (index == -1) v.last() else {
            index++
            if (index > v.size - 1) {
                index = v.size - 1
                state = TileObjState.REACHED
            }
            v[index]
        }
        target = currentNode
    }

    override fun move() {
        if (state == TileObjState.DEAD) return
        if (state == TileObjState.REACHED) return
        if (target.isEmpty()) return
        if (target.visitorsList.contains(this.name)) return
        if (center.dist(target.center) <= this.speed * this.speed) {
            this.center = PPoint(target.center)
            if (target is Ender) {
                this.state = TileObjState.DEAD
                return
            }
            target.visitorsList.add(this.name)
            this.state = TileObjState.WAITING
            return
        }
        state = TileObjState.MOVING
        angle = atan2((target.center.y - center.y), (target.center.x - center.x))
        val cos = cos(angle) * this.speed
        val sin = sin(angle) * this.speed
        pos.translate(cos, sin)

    }

    override fun show() = with(JOGLEntry.GRF) {
        //triangle with pointing to next target
        //default direction - right

        //for sake of be able to change angle in triangle shape - { a } parameter
        val x = 0 + size
        val y = 0 + size / 2
        val a = Math.toRadians(25.0)
        val l1X = (x - size * cos(0 + a)).toFloat()
        val l1Y = (y - size * sin(0 + a)).toFloat()
        val l2X = (x - size / 1.5).toFloat()
        val l2Y = y
        val l3X = (x - size * cos(0 - a)).toFloat()
        val l3Y = (y - size * sin(0 - a)).toFloat()

        val vx = floatArrayOf(x, l1X, l2X, l3X)
        val vy = floatArrayOf(y, l1Y, l2Y, l3Y)
//        simple triangle shape:
//        val poly = Polygon(
//            intArrayOf(this.pos.xi, this.pos.xi, this.pos.xi + this.size),
//            intArrayOf(this.pos.yi, this.pos.yi + this.size, this.pos.yi + this.size / 2),
//            3
//        )
        //TODO: fix rotation
        glPushMatrix()
        glTranslatef(center.x, center.y, 0f)
        glRotatef(angle * 57.29577951308232f, 0f, 0f, 1f)
        val c = CColor(color)
        glColor4f(c.r, c.g, c.b, 1f)
        Drawing.drawLinePolygon(vx, vy, 4, 2.5f)
        glLoadIdentity()
        glColor4f(0f, 0f, 0f, .8f)
        Drawing.fillPolygon(vx, vy, 4)
        glPopMatrix()
        glLoadIdentity()
        super.show(showCenter = false, showName = true)
    }
}