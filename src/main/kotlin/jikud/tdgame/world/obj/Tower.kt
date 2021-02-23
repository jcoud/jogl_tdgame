package jikud.tdgame.world.obj

import jikud.tdgame.TDMain
import jikud.tdgame.core.Drawing
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

    private fun LinkedList<Entity>.containsName(e: Entity): Boolean {
        for (i in this) if (i.name == e.name) return true else continue
        return false
    }

    companion object {
        var count = 0
    }

    init {
        initTimer()
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
        this.shootingSpeed = .5
        if (this.sortedTargetList.isNotEmpty()) shoot()
        if (this.projectiles.isNotEmpty()) this.projectiles.forEach { it.update() }
        super.update()
    }
    private val gridPos get() = this.center.devide(Field.gridSize)
    private val gridRad get() = (this.targetRange * 2 / Field.gridSize).toInt()
    private fun findTowerTarget() {

//        val diffXp = if (this.center.x + this.targetRange > gridPos.xi * Field.gridSize + Field.gridSize) 1 else 0
//        val diffYp = if (this.center.y + this.targetRange > gridPos.yi * Field.gridSize + Field.gridSize) 1 else 0
//        val diffXn = if (this.center.x - this.targetRange < gridPos.xi * Field.gridSize) -1 else 0
//        val diffYn = if (this.center.y - this.targetRange < gridPos.yi * Field.gridSize) -1 else 0
        for (i in gridPos.xi - gridRad..gridPos.xi + gridRad) {
            if (i < 0 || i >= Field.gridLength) continue
            for (j in gridPos.yi - gridRad..gridPos.yi + gridRad) {
                if (j < 0 || j >= Field.gridLength) continue
                val t = Field.grid[i][j]
                if (t.isEmpty()) continue
                for (e in t.entities) {
                    if (e.center.dist(this.center) <= this.targetRange * this.targetRange) {
                        if (!this.sortedTargetList.containsName(e))
                            this.sortedTargetList.add(e)
                        this.sortedTargetList.sortWith { e1, e2 ->
                            val e1d = e1.center.dist(this.center)
                            val e2d = e2.center.dist(this.center)
                            return@sortWith if (e1d > e2d) 1 else -1
                        }
                    }
                }
            }
        }
        val c = this.sortedTargetList.clone() as LinkedList<*>
        for (i in c) {
            i as Entity
            if (i.center.dist(this.center) > this.targetRange * this.targetRange)
                this.sortedTargetList.remove(i)
        }
    }

    private fun shoot() {
        startInternalTimer()
        if (!this.timer!!.trigger(this.shootingSpeed)) return
        when (this.mode) {
            ShootingMod.CLOSEST -> {
                for (e in this.sortedTargetList) {
                    makeAndAddProjectile(e)
                    break
                }
            }
            ShootingMod.FARTHEST -> {
                for (e in this.sortedTargetList.reversed()) {
                    makeAndAddProjectile(e)
                    break
                }
            }
            ShootingMod.EACH -> {
                for (e in this.sortedTargetList) {
                    makeAndAddProjectile(e)
                }
            }
        }
    }

    private fun makeAndAddProjectile(entity: Entity) {
        this.projectiles.add(Projectile(PPoint(this.center), entity, 7f))
    }

    override fun show() = with(Drawing.GL) {
        //rectangle
        val q = CColor(this@Tower.color)
        glColor4f(q.r, q.g, q.b, q.a)
        Drawing.Rect(this@Tower.pos.x, this@Tower.pos.y, this@Tower.size, this@Tower.size, true)
        for (i in sortedTargetList.indices) {
            if (this@Tower.mode == ShootingMod.CLOSEST) if (i == 0) continue
            if (this@Tower.mode == ShootingMod.FARTHEST) if (i == this@Tower.sortedTargetList.size - 1) continue
            if (this@Tower.mode == ShootingMod.EACH) glColor3f(.0f, 1f, .0f) else glColor3f(1f, .0f, .0f)
            Drawing.Line(
                this@Tower.center.x,
                this@Tower.center.y,
                this@Tower.sortedTargetList[i].center.x,
                this@Tower.sortedTargetList[i].center.y
            )
        }
        val e_ = when (mode) {
            ShootingMod.CLOSEST -> this@Tower.sortedTargetList.firstOrNull()
            ShootingMod.FARTHEST -> this@Tower.sortedTargetList.lastOrNull()
            else -> null
        }
        if (e_ != null) {
            glColor3f(.0f, 1f, .0f)
            Drawing.Line(this@Tower.center.x, this@Tower.center.y, e_.center.x, e_.center.y)
        }
        val c = CColor(color)
        glColor4f(c.r, c.g, c.b, c.a)
        Drawing.Circle(this@Tower.center.x, this@Tower.center.y, this@Tower.targetRange, false)
        glColor4f(c.r, c.g, c.b, .15f)
        Drawing.Circle(this@Tower.center.x, this@Tower.center.y, this@Tower.targetRange, true)

        //projectiles
        this@Tower.projectiles.forEach { it.show() }

        //target tile range
        for (i in gridPos.xi - gridRad..gridPos.xi + gridRad) {
            if (i < 0 || i >= Field.gridLength) continue
            for (j in gridPos.yi - gridRad..gridPos.yi + gridRad) {
                if (j < 0 || j >= Field.gridLength) continue
                glColor4i(255, 30, 30, 90)
                Drawing.Rect(
                    i * Field.gridSize * 1f,
                    j * Field.gridSize * 1f,
                    Field.gridSize * 1f,
                    Field.gridSize * 1f,
                    false
                )
            }
        }

        super.show()
    }

    override fun showName(customName: String) {
        super.showName("$customName T${this.timer!!.current} D${this.damage}")
    }
}
