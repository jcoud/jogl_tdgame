package me.jikud.tdgame.world.obj.towers

import me.jikud.tdgame.TDMain
import me.jikud.tdgame.core.Drawing
import me.jikud.tdgame.core.Timer
import me.jikud.tdgame.helpers.CColor
import me.jikud.tdgame.helpers.PPoint
import me.jikud.tdgame.world.field.Field
import me.jikud.tdgame.world.obj.Entity
import me.jikud.tdgame.world.obj.Projectile
import me.jikud.tdgame.world.obj.TileObj
import java.util.*
import kotlin.collections.ArrayList

abstract class Tower(
        pos: PPoint,
        name: String,
        size: Float,
        color: Int
) : TileObj(pos, name, size, color) {
    var damage = 0
    val sortedTargetList = LinkedList<Entity>()
    val projectiles = ArrayList<Projectile>()
    val targetRange = (2 + .5f) * TDMain.bs // tiles + offset from tower's center
    var shootingSpeed = .0
    var mode = ShootingMod.CLOSEST

    private val searchTiles = ArrayList<PPoint>()
    private val timer = Timer()

    companion object {
        var count = 0
    }

    init {
        count++
    }

    enum class ShootingMod {
        CLOSEST, FARTHEST, EACH;

        fun next(): ShootingMod {
            return values()[if (ordinal == values().size - 1) 0 else ordinal + 1]
        }
    }


    private fun prepareSearchField() {
        val gridPos = this.center.devide(Field.gridTileSize)
        val diffX =
                when {
                    this.center.xi + this.targetRange > gridPos.xi * Field.gridTileSize + Field.gridTileSize -> 1
                    this.center.xi - this.targetRange < gridPos.xi * Field.gridTileSize -> -1
                    else -> 0
                }
        val diffY =
                when {
                    this.center.yi + this.targetRange > gridPos.yi * Field.gridTileSize + Field.gridTileSize -> 1
                    this.center.yi - this.targetRange < gridPos.yi * Field.gridTileSize -> -1
                    else -> 0
                }

        val p1 = PPoint(gridPos.x + diffX, gridPos.y)
        val p2 = PPoint(gridPos.x + diffX, gridPos.y + diffY)
        val p3 = PPoint(gridPos.x, gridPos.y + diffY)
        if (searchTiles.none { it == gridPos }) searchTiles.add(gridPos)
        if (searchTiles.none { it == p1 }) searchTiles.add(p1)
        if (searchTiles.none { it == p2 }) searchTiles.add(p2)
        if (searchTiles.none { it == p3 }) searchTiles.add(p3)

    }

    override fun update() {
//        updateProjectiles()
        prepareSearchField()
        findTowerTarget()
        if (this.sortedTargetList.isNotEmpty()) shoot()
        if (this.projectiles.isNotEmpty()) this.projectiles.forEach(Projectile::update)
        super.update()
    }

    private fun findTowerTarget() {
        for (st in searchTiles) {
            if (st.xi < 0 || st.xi >= Field.gridLength) continue
            if (st.yi < 0 || st.yi >= Field.gridLength) continue
            val t = Field.grid[st.xi][st.yi]
            if (t.isEmpty()) continue
            for (e in t.entities) {
                if (e.center.dist(this.center) <= this.targetRange * this.targetRange) {
                    if (this.sortedTargetList.none { it.name == e.name }) this.sortedTargetList.add(e)
                    this.sortedTargetList.sortWith { e1, e2 ->
                        val e1d = e1.center.dist(this.center)
                        val e2d = e2.center.dist(this.center)
                        return@sortWith if (e1d > e2d) 1 else -1
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
        timer.start()
        if (!this.timer.trigger(this.shootingSpeed)) return
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
        this.projectiles.add(Projectile(PPoint(this.center), 7f, entity, damage))
    }

    override fun draw() = with(Drawing.GL) {
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
        val e = when (mode) {
            ShootingMod.CLOSEST -> this@Tower.sortedTargetList.firstOrNull()
            ShootingMod.FARTHEST -> this@Tower.sortedTargetList.lastOrNull()
            else -> null
        }
        if (e != null) {
            glColor3f(.0f, 1f, .0f)
            Drawing.Line(this@Tower.center.x, this@Tower.center.y, e.center.x, e.center.y)
        }
        val c = CColor(color)
        glColor4f(c.r, c.g, c.b, c.a)
        Drawing.Circle(this@Tower.center.x, this@Tower.center.y, this@Tower.targetRange, false)
        glColor4f(c.r, c.g, c.b, .15f)
        Drawing.Circle(this@Tower.center.x, this@Tower.center.y, this@Tower.targetRange, true)

        //projectiles
        this@Tower.projectiles.forEach { it.draw() }

        //target tile range
//        print("$gridPos, $gridRad")
//        exitProcess(0)
        drawSearchedTiles()
//        for (st in searchTiles) {
//            if (st.xi < 0 || st.xi >= Field.gridLength) continue
//            if (st.yi < 0 || st.yi >= Field.gridLength) continue
//            val c_ = CColor(this@Tower.color)
//            glColor4f(c_.r, c_.g, c_.b, c_.a)
//            Drawing.Rect(
//                st.xi * Field.gridTileSize * 1f,
//                st.yi * Field.gridTileSize * 1f,
//                Field.gridTileSize * 1f,
//                Field.gridTileSize * 1f,
//                false,
//                2f
//            )
//        }

        super.draw()
    }

    private fun drawSearchedTiles() = with(Drawing.GL) {
        if (searchTiles.isEmpty()) return
        val sq1 = searchTiles.first()
        val sq2 = searchTiles.last()
        val w = sq2.xi - sq1.xi
        val h = sq2.yi - sq2.yi
        val c = CColor(color)
        glColor4f(c.r, c.g, c.b, c.a)
        Drawing.Rect(
                sq1.xi * Field.gridTileSize * 1f,
                sq1.yi * Field.gridTileSize * 1f,
                w * 1f, h * 1f,
                false,
                2f
        )
    }

    override fun drawName(customName: String) {
        super.drawName("$customName T${this.timer.current} D${this.damage}")
    }
}
