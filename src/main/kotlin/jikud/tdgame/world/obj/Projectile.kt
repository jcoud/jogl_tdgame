package jikud.tdgame.world.obj

import jikud.tdgame.helpers.PPoint
import java.awt.Color
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

data class Projectile(
    override var pos: PPoint,
    var target: Entity,
    var speed: Float
) : TileObj(pos, name = "", size = 1f, color = Color.BLACK.rgb), IMovable {

    private var damage: Int = 20
    override var state = TileObjState.WAITING
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
        val cos = cos(angle) * this.speed
        val sin = sin(angle) * this.speed
        this.pos.translate(cos, sin)
    }
}