package jikud.tdgame.world.obj

import jikud.tdgame.core.Drawing
import jikud.tdgame.core.GameTime
import jikud.tdgame.core.Global
import jikud.tdgame.core.Renderer
import jikud.tdgame.helpers.CColor
import jikud.tdgame.helpers.PPoint
import jikud.tdgame.world.FieldProcessorQueue
import jikud.tdgame.world.obj.gate.Ender
import jikud.tdgame.world.obj.gate.Starter
import java.awt.Color


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

    // == Internal Timer Counter ==
    var timer: GameTime.Timer? = null

    fun initTimer() {
        timer = GameTime.Timer()
    }

    override fun update() {
        if (state == TileObjState.DEAD) FieldProcessorQueue.queueRemove(this)
        if (this is IMovable) move()
        if (timer == null) return
        if (!Global.TileTimer_isUpdatable && timer!!.started)
            timer!!.pause()
        else
            timer!!.unpause()
        print("delta: $timer\r")
    }


    fun startInternalTimer() {
        if (timer == null)
            throw NullPointerException("Timer has not been initialized!")
        if (awakened) return
        awakened = true
        timer!!.start()
    }
    // ======

    open fun show() {
        this.show(showCenter = true, showName = true)
    }

    open fun show(showCenter: Boolean = true, showName: Boolean = true) {
        if (showName) showName()
        if (showCenter) showCenterDot()
    }

    var center: PPoint
        get() = PPoint(this.pos.x + this.size / 2, this.pos.y + this.size / 2)
        set(value) {
            this.pos.x = value.x - this.size / 2
            this.pos.y = value.y - this.size / 2
        }
    private val F = Drawing.FontRendering(Renderer.FONT.deriveFont(.7f))

    open fun showName(customName: String = this.name) = with(Drawing.GL) {
        F.drawString(
            CColor(.7f, .7f, .5f, .7f),
            customName,
            center.x,
            center.y - 25
        )
        glLoadIdentity()
    }

    private fun showCenterDot() {
        val r = 3f
        Drawing.GL.glColor3f(.0f, .0f, .0f)
        Drawing.Circle(center.x, center.y, r, true)
//        GL11.glLoadIdentity()
    }

    override fun toString(): String {
        return "{pos:$pos, name:$name, size:$size, color:$color | Color: ${Color(color)}}"
    }
}
