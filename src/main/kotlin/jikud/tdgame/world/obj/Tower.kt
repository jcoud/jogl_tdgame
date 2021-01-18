package jikud.tdgame.world.obj

import jikud.tdgame.JOGLEntry
import jikud.tdgame.TDMain
import jikud.tdgame.core.Drawing
import jikud.tdgame.core.GameTime
import jikud.tdgame.helpers.CColor
import jikud.tdgame.helpers.PPoint
import jikud.tdgame.world.Field
import java.util.*
import kotlin.collections.ArrayList

open class Tower(
    override var pos: PPoint,
    override var name: String,
    override var size: Float,
    override var color: Int
) : TileObj(pos, name, size, color) {
    var damage = 0
    val sortedTargetList = LinkedList<Entity>()
    val projectiles = ArrayList<Projectile>()
    val targetRange = (2 + .5f) * TDMain.bs // tiles + offset from tower's center
    var shootingSpeed = .0
    var mode = ShootingMod.CLOSEST

    private fun <E : Entity> LinkedList<E>.containsName(e: E): Boolean {
        for (i in this) if (i.name == e.name) return true else continue
        return false
    }

    companion object {
        var count = 0
    }

    enum class ShootingMod {
        CLOSEST, FARTHEST, EACH;

        fun next(): ShootingMod {
            return values()[if (ordinal == values().size - 1) 0 else ordinal + 1]
        }
    }

    override fun update() {
//        updateProjectiles()
        findTowerTarget()
        shootingSpeed = .5
        if (sortedTargetList.isNotEmpty()) shoot()
        if (projectiles.isNotEmpty()) projectiles.forEach { it.update() }
        super.update()
    }

    private fun findTowerTarget() {
        val pos = center.devide(Field.gridSize)
        var rt = (targetRange / Field.gridSize).toInt()
        rt = if (rt == 0) 1 else rt
        for (i in pos.xi - rt..pos.xi + rt) {
            if (i < 0 || i >= Field.gridLength) continue
            for (j in pos.yi - rt..pos.yi + rt) {
                if (j < 0 || j >= Field.gridLength) continue
                val t = Field.grid[i][j]
                if (t.isEmpty()) continue
                for (e in t.entities) {
                    if (e.center.dist(center) <= targetRange * targetRange) {
                        if (!sortedTargetList.containsName(e)) sortedTargetList.add(e)
                        sortedTargetList.sortWith { e1, e2 ->
                            val e1d = e1.center.dist(center)
                            val e2d = e2.center.dist(center)
                            return@sortWith if (e1d > e2d) 1 else -1
                        }
                    }
                }
            }
        }
        val c = sortedTargetList.clone() as LinkedList<*>
        for (i in c) {
            i as Entity
            if (i.center.dist(center) > targetRange * targetRange) sortedTargetList.remove(i)
        }
    }

    private val timer = GameTime.Timer()

    private fun shoot() {
        startInternalTimer()
        if (!timer.trigger(shootingSpeed)) return
        when (mode) {
            ShootingMod.CLOSEST -> {
                for (e in sortedTargetList) {
                    makeAndAddProjectile(e)
                    break
                }
            }
            ShootingMod.FARTHEST -> {
                for (e in sortedTargetList.reversed()) {
                    makeAndAddProjectile(e)
                    break
                }
            }
            ShootingMod.EACH -> {
                for (e in sortedTargetList) {
                    makeAndAddProjectile(e)
                }
            }
        }
    }

    private fun makeAndAddProjectile(entity: Entity) {
        projectiles.add(Projectile(PPoint(this.center), entity, 7f))
    }

    override fun show() = with(JOGLEntry.GRF) {
        //rectangle
        val q = CColor(color)
        glColor4f(q.r, q.g, q.b, q.a)
        Drawing.fillRect(pos.x, pos.y, size, size)
        gl.glLineWidth(1f)
        for (i in sortedTargetList.indices) {
            if (mode == ShootingMod.CLOSEST) if (i == 0) continue
            if (mode == ShootingMod.FARTHEST) if (i == sortedTargetList.size - 1) continue
            if (mode == ShootingMod.EACH) glColor3f(.0f, 1f, .0f) else glColor3f(1f, .0f, .0f)
            Drawing.drawLine(
                center.x,
                center.y,
                sortedTargetList[i].center.x,
                sortedTargetList[i].center.y
            )
        }
        val e_ = when (mode) {
            ShootingMod.CLOSEST -> sortedTargetList.firstOrNull()
            ShootingMod.FARTHEST -> sortedTargetList.lastOrNull()
            else -> null
        }
        if (e_ != null) {
            glColor3f(.0f, 1f, .0f)
            Drawing.drawLine(center.x, center.y, e_.center.x, e_.center.y)
        }
        var c = CColor(color)
        glColor4f(c.r, c.g, c.b, c.a)
//        val off = targetRange.toFloat()
//        gl.glTranslatef(-off, -off, 0f)
//        gl.glLoadIdentity()
        Drawing.drawLineCircle(center.x, center.y, targetRange)
        c = CColor(color)
        glColor4f(c.r, c.g, c.b, .15f)
        Drawing.fillCircle(center.x, center.y, targetRange)

        //projectiles
        projectiles.forEach { it.show() }

        //target tile range
        val pos = pos.devide(Field.gridSize)
        val rt = (targetRange / Field.gridSize).toInt()
        glLineWidth(2f)
        for (i in pos.xi - rt..pos.xi + rt) {
            if (i < 0 || i >= Field.gridLength) continue
            for (j in pos.yi - rt..pos.yi + rt) {
                if (j < 0 || j >= Field.gridLength) continue
                glColor4i(255, 30, 30, 90)
                Drawing.drawLineRect(
                    i * Field.gridSize * 1f,
                    j * Field.gridSize * 1f,
                    Field.gridSize * 1f,
                    Field.gridSize * 1f
                )
            }
        }

        super.show()
    }
}
