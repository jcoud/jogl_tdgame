package me.jikud.tdgame.world.obj

import me.jikud.engine.core.helpers.PPoint
import me.jikud.engine.core.main.GLRenderHelper
import java.awt.Color
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Projectile(
    pos: PPoint,
    private val speed: Float,
    private val target: Entity,
    private val damage: Int
) : TileObj(pos, name = "", size = 1f, color = Color.BLACK.rgb), IMovable {

    init {
        state = TileObjState.WAITING
    }

    private fun applyDamage() {
        target.health -= damage
        state = TileObjState.DEAD
    }

    override fun update() {
        if (state == TileObjState.REACHED) applyDamage()
        super.update()
    }

    override fun move() {
        if (target.isEmpty()) return
        val d = 1 + this.speed
        if (this.center.distanceSq(target.center) <= d) {
            state = TileObjState.REACHED
            return
        }
        state = TileObjState.MOVING
        val angle = atan2((target.center.y - this.center.y), (target.center.x - this.center.x))
        val _x: Float = (cos(angle) * this.speed * 1).toFloat()
        val _y: Float = (sin(angle) * this.speed * 1).toFloat()
        pos.translate(_x, _y)
    }

    override fun draw() {
        val r = 3f
        GLRenderHelper.JGL.glColor3f(.0f, .0f, .0f)
        GLRenderHelper.Circle(center.x, center.y, r, true)
    }
}