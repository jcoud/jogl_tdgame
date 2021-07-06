package me.jikud.tdgame.world.obj

import me.jikud.engine.core.helpers.CColor
import me.jikud.engine.core.helpers.PPoint
import me.jikud.engine.core.main.GLRenderHelper
import me.jikud.tdgame.TDMain
import me.jikud.tdgame.Global
import me.jikud.tdgame.world.field.FieldProcessorQueue
import me.jikud.tdgame.world.obj.towers.Tower
import java.awt.Color


abstract class TileObj(
    var pos: PPoint,
    var name: String,
    var size: Float,
    var color: Int
) : IUpdatable {

    var state = TileObjState.NONE
    var awakened = false

    enum class TileObjState {
        MOVING, REACHED, WAITING, DEAD, NONE
    }

    init {
        this.pos = PPoint.snapToGrid(this.pos, TDMain.bs * 1f).translate((TDMain.bs - size) / 2f).apply {
            y += TDMain.fieldHeightGap
        }
    }

    override fun equals(other: Any?): Boolean {
        val b1 = if (other is TileObj) true else return false
        val b2 = this.pos == other.pos
        val b3 = this.name == other.name
        val b4 = this.hashCode() == other.hashCode()
        return b1 && b2 && b3 && b4

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

    private fun updateIds() {
        when (this) {
            is NodePoint -> {

            }
            is Tower -> {
            }
            is Entity -> {
            }
        }
    }

    override fun update() {
        if (state == TileObjState.DEAD) FieldProcessorQueue.queueToRemove(this)
        if (Global.TileTimer_isUpdatable && this is IMovable) move()

        updateIds()
//        print("delta: $timer\r")
    }

    open fun draw() {
        this.draw(showCenter = true, showName = true)
    }

    open fun draw(showCenter: Boolean = true, showName: Boolean = true) {

        if (Global.debugMode || Global.editorMode) {
            if (showCenter) drawCenterDot()
            if (showName) drawName()
        }
    }

    var center: PPoint
        get() = PPoint(this.pos.x + this.size / 2, this.pos.y + this.size / 2)
        set(value) {
            this.pos.x = value.x
            this.pos.y = value.y
        }

    open fun drawName(customName: String = this.name) {
        GLRenderHelper.FontRendering.drawString(
            CColor(.7f, .7f, .5f, .7f),
            customName,
            (center.x - GLRenderHelper.FontRendering.bounds(customName).width * .5f).toFloat(),
            center.y - 25
        )
    }

    private fun drawCenterDot() {
        val r = 3f
        GLRenderHelper.JGL.glColor3f(.0f, .0f, .0f)
        GLRenderHelper.Circle(center.x, center.y, r, true)
    }

    override fun toString(): String {
        return "{pos:$pos, name:$name, size:$size, color:$color | Color: ${Color(color)}}"
    }

    override fun hashCode(): Int {
        var result = pos.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + color
        result = 31 * result + state.hashCode()
        result = 31 * result + awakened.hashCode()
        return result
    }
}
