package jikud.tdgame.world.obj

import jikud.tdgame.JOGLEntry
import jikud.tdgame.core.Drawing
import jikud.tdgame.core.GameTime
import jikud.tdgame.helpers.CColor
import jikud.tdgame.helpers.PPoint
import jikud.tdgame.world.FieldProcessorQueue
import jikud.tdgame.world.obj.gate.Ender
import jikud.tdgame.world.obj.gate.Starter
import java.awt.Color
import java.math.BigDecimal
import java.math.RoundingMode


abstract class TileObj(
    open var pos: PPoint,
    open var name: String,
    open var size: Float,
    open var color: Int
) : IUpdatable {

    open var state = TileObjState.NONE
    private var awakened = false

    enum class TileObjState {
        MOVING, REACHED, WAITING, DEAD, NONE
    }

    fun isEmpty(): Boolean {
        return when (this::class) {
            Entity::class -> this == EmptyEntity
            Tower::class -> this == EmptyTower
            NodePoint::class -> this == EmptyNode
            Starter::class -> this as NodePoint == EmptyNode
            Ender::class -> this as NodePoint == EmptyNode
            else -> throw IllegalArgumentException("Class not parent from ${TileObj::class.simpleName}. Found ${this::class.simpleName}")
        }
    }

    fun isNotEmpty(): Boolean {
        return !isEmpty()
    }

    companion object {
        val EmptyEntity = TileObjUtils.getEmptyTile<Entity>(Entity::class.java)
        val EmptyNode = TileObjUtils.getEmptyTile<NodePoint>(NodePoint::class.java)
        val EmptyTower = TileObjUtils.getEmptyTile<Tower>(Tower::class.java)
    }

    override fun update() {
        if (state == TileObjState.DEAD) FieldProcessorQueue.queueRemove(this)
        if (this is IMovable) move()
    }

    // == Internal Timer Counter ==
    private val timer = GameTime.Timer()

    fun startInternalTimer() {
        if (awakened) return
        awakened = true
        timer.start()
    }
    // ======

    open fun show() {
        this.show(showCenter = true, showName = true)
    }

    open fun show(showCenter: Boolean, showName: Boolean) {
        if (showName) showName()
        if (showCenter) showCenterDot()
    }

    var center: PPoint
        get() = PPoint(this.pos.x + this.size / 2, this.pos.y + this.size / 2)
        set(value) {
            this.pos.x = value.x - this.size / 2
            this.pos.y = value.y - this.size / 2
        }
    private val f = Drawing.FontRendering(JOGLEntry.FONT.deriveFont(.7f))

    private fun showName() = with(JOGLEntry.GRF) {
        val s =
            when (this@TileObj) {
                is Entity -> "$name H$health"
                is Tower -> "$name T${BigDecimal(timer.current).setScale(2, RoundingMode.DOWN)} D$damage"
                is Projectile -> return
                else -> name
            }
        f.drawString(
            CColor(.7f, .7f, .5f, .7f),
            s,
            center.x,
            center.y - 25
        )
        glLoadIdentity()
    }

    private fun showCenterDot() {
        val r = 3f
        JOGLEntry.GRF.glColor3f(.0f, .0f, .0f)
        Drawing.fillCircle(center.x, center.y, r)
//        GL11.glLoadIdentity()
    }

    override fun toString(): String {
        return "{pos:$pos, name:$name, size:$size, color:$color | Color: ${Color(color)}}"
    }
}
