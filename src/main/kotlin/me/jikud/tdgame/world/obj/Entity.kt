package me.jikud.tdgame.world.obj

import me.jikud.engine.core.helpers.CColor
import me.jikud.engine.core.helpers.PPoint
import me.jikud.engine.core.main.GLRenderHelper
import me.jikud.tdgame.TDMain
import me.jikud.tdgame.world.field.Field
import me.jikud.tdgame.world.obj.gates.Ender
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


class Entity(
    pos: PPoint, name: String, size: Float, color: Int, private var speed: Float
) : TileObj(pos, name, size, color), IMovable {

    private var errorState = 0
    var health = 0
    var target = EmptyNode
    private var angle = .0f

    companion object {
        var count = 0
    }

    init {
        state = TileObjState.MOVING
        count++
    }

    override fun update() {
        findEntityTarget()
        if (health <= 0) state = TileObjState.DEAD
        updateFieldGridPos()
        super.update()
        checkIfEntityReachedTheEnd()
    }

    private fun updateFieldGridPos() {
        val g = center.devide(Field.gridScanTileSize)
//        if (g.xi >= Field.grid.size) println("_x (${g.xi}) >= ${Field.grid.size}")
//        if (g.yi >= Field.grid.size) println("_y (${g.yi}) >= ${Field.grid[0].size}")
        val x = Math.min(g.xi, Field.gridScan.size - 1)
        val y = Math.min(g.yi, Field.gridScan[0].size - 1)
        Field.gridScan[x][y].add(this)
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

    private fun checkIfEntityReachedTheEnd() {
        if (errorState == 1) {
            findEntityTarget()
            return
        }

        if (state == TileObjState.REACHED) {
            if (pos.toIndex(TDMain.bs, TDMain.n) != Field.end.pos.toIndex(TDMain.bs, TDMain.n)) {
                errorState = 1
                state = TileObjState.WAITING
            }
        }
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
//        val x: Float = (cos(angle) * this.speed * Loop.unprocessed).toFloat()
//        val y: Float = (sin(angle) * this.speed * Loop.unprocessed).toFloat()
        val x: Float = cos(angle) * this.speed
        val y: Float = sin(angle) * this.speed
        pos.translate(x, y)

    }

    //triangle with pointing to next target
    //default direction - right
    //for sake of be able to change angle in triangle shape - { a } parameter
    private val x = size
    private val y = size / 2
    private val a = Math.toRadians(25.0)
    private val l1X = (x - size * cos(0 + a)).toFloat()
    private val l1Y = (y - size * sin(0 + a)).toFloat()
    private val l2X = (x - size / 1.5).toFloat()
    private val l2Y = y
    private val l3X = (x - size * cos(0 - a)).toFloat()
    private val l3Y = (y - size * sin(0 - a)).toFloat()

    private val vx = floatArrayOf(x, l1X, l2X, l3X)
    private val vy = floatArrayOf(y, l1Y, l2Y, l3Y)

//        simple triangle shape:
//        val poly = Polygon(
//            intArrayOf(this.pos.xi, this.pos.xi, this.pos.xi + this.size),
//            intArrayOf(this.pos.yi, this.pos.yi + this.size, this.pos.yi + this.size / 2),
//            3
//        )

    override fun draw() = with(GLRenderHelper.JGL) {
        glPushMatrix()
        glTranslatef(center.x, center.y, 0f)
        glRotatef(angle * 57.29577951308232f, 0f, 0f, 1f)
        val c = CColor(color)
        if (errorState == 1) {
            glColor4f(1f, 0f, 0f, 1f)
            GLRenderHelper.Polygon(vx, vy, 4, true)
            glColor4f(1f, 1f, 1f, 1f)
            GLRenderHelper.Polygon(vx, vy, 4, false, 3f)
        } else {
            glColor4f(c.r, c.g, c.b, .6f)
            GLRenderHelper.Polygon(vx, vy, 4, true)
            glColor4f(0f, 0f, 0f, 1f)
            GLRenderHelper.Polygon(vx, vy, 4, false, 2.5f)
        }
        glPopMatrix()
        glLoadIdentity()
        super.draw(showCenter = false, showName = true)
    }

    override fun drawName(customName: String) {
        super.drawName("$customName $health")
    }
}