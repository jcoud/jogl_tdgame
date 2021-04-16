package me.jikud.tdgame.world.obj

import me.jikud.tdgame.TDMain
import me.jikud.tdgame.core.Drawing
import me.jikud.tdgame.core.Global
import me.jikud.tdgame.core.JOGLEntry
import me.jikud.tdgame.helpers.CColor
import me.jikud.tdgame.helpers.PPoint
import me.jikud.tdgame.world.field.FieldProcessorQueue
import me.jikud.tdgame.world.obj.towers.Tower
import java.awt.Color


abstract class TileObj(
    var pos: PPoint,
    var name: String,
    var size: Float,
    var color: Int
) : IUpdatable {

    open var state = TileObjState.NONE
    var awakened = false

    enum class TileObjState {
        MOVING, REACHED, WAITING, DEAD, NONE
    }

    init {
        this.pos = this.pos.snapToGrid().add((TDMain.bs - size) / 2f)
    }

    fun isEmpty(): Boolean {
        return when (this) {
            is Entity -> this == EmptyEntity
            is Tower -> this == EmptyTower
            is NodePoint -> this == EmptyNode
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

    // == Internal Timer | OPTIONAL ==

    override fun update() {
        if (state == TileObjState.DEAD) FieldProcessorQueue.queueToRemove(this)
        if (Global.TileTimer_isUpdatable)
            if (this is IMovable)
                move()

//        print("delta: $timer\r")
    }
    // ======

    open fun draw() {
        this.draw(showCenter = true, showName = true)
    }

    open fun draw(showCenter: Boolean = true, showName: Boolean = true) {
        if (showName) drawName()
        if (showCenter) drawCenterDot()
    }

    var center: PPoint
        get() = PPoint(this.pos.x + this.size / 2, this.pos.y + this.size / 2)
        set(value) {
            this.pos.x = value.x - this.size / 2
            this.pos.y = value.y - this.size / 2
        }
    private val F = Drawing.FontRendering(JOGLEntry.FONT.deriveFont(.7f))

    open fun drawName(customName: String = this.name) = with(Drawing.GL) {
        F.drawString(
            CColor(.7f, .7f, .5f, .7f),
            customName,
            (center.x - F.bounds(customName).width *.5f).toFloat(),
            center.y - 25
        )
//        glLoadIdentity()
    }

    private fun drawCenterDot() {
        val r = 3f
        Drawing.GL.glColor3f(.0f, .0f, .0f)
        Drawing.Circle(center.x, center.y, r, true)
//        GL11.glLoadIdentity()
    }

    override fun toString(): String {
        return "{pos:$pos, name:$name, size:$size, color:$color | Color: ${Color(color)}}"
    }
}
