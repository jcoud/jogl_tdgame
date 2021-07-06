package me.jikud.tdgame.world.obj.towers

import me.jikud.engine.core.helpers.CColor
import me.jikud.engine.core.helpers.PPoint
import me.jikud.engine.core.main.GLRenderHelper
import me.jikud.engine.core.main.Timer
import me.jikud.tdgame.Global
import me.jikud.tdgame.TDMain
import me.jikud.tdgame.world.field.Field
import me.jikud.tdgame.world.obj.Entity
import me.jikud.tdgame.world.obj.ITimerUpdatable
import me.jikud.tdgame.world.obj.Projectile
import me.jikud.tdgame.world.obj.TileObj
import java.awt.Color
import java.util.*
import kotlin.collections.ArrayList

abstract class Tower(
    pos: PPoint,
    name: String,
    size: Float,
    color: Int
) : TileObj(pos, name, size, color), ITimerUpdatable {
    var damage = 0
    val sortedTargetList = LinkedList<Entity>()
    val projectiles = ArrayList<Projectile>()

    var targetRange = (1 + .5f) * TDMain.bs // tiles + offset from tower's center
    var shootingSpeed = .0
    var mode = ShootingMod.CLOSEST

    private val searchTiles = ArrayList<PPoint>()
    override var timer = Timer()

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
        //todo: переделать систему поиска тайлов поиска существ
        //todo: пустые тайлы не должны быть в листе

        searchTiles.clear()
        val gridPos = this.center.devide(Field.gridScanTileSize)

        val XY = arrayListOf<PPoint>()

        for (a in 0..360 step 5) {
            val x = (this.targetRange - 1) * Math.cos(a * 1.0) + this.center.x
            val y = (this.targetRange - 1) * Math.sin(a * 1.0) + this.center.y
            val xg: Int = (x / Field.gridScanTileSize).toInt()
            val yg: Int = (y / Field.gridScanTileSize).toInt()

            if (xg >= gridPos.xi) {
                for (xx in gridPos.xi..xg) {
                    if (xx > Field.gridScanLength) continue
                    if (yg >= gridPos.yi) {
                        for (yy in gridPos.yi..yg) {
                            if (yy >= Field.gridScanLength) continue
                            val p = PPoint(xx, yy)
                            if (XY.none { it == p }) XY += p
                        }
                    } else {
                        for (yy in gridPos.yi downTo yg) {
                            if (yy < 0) continue
                            val p = PPoint(xx, yy)
                            if (XY.none { it == p }) XY += p
                        }
                    }
                }
            } else {
                for (xx in gridPos.xi downTo xg) {
                    if (xx < 0) continue
                    if (yg >= gridPos.yi) {
                        for (yy in gridPos.yi..yg) {
                            if (yy >= Field.gridScanLength) continue
                            val p = PPoint(xx, yy)
                            if (XY.none { it == p }) XY += p
                        }
                    } else {
                        for (yy in gridPos.yi downTo yg) {
                            if (yy < 0) continue
                            val p = PPoint(xx, yy)
                            if (XY.none { it == p }) XY += p
                        }
                    }
                }
            }
        }
        for (p in XY) {
            if (searchTiles.none { it == p })
                searchTiles.add(p)
        }
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
            if (st.xi < 0 || st.xi >= Field.gridScanLength) continue
            if (st.yi < 0 || st.yi >= Field.gridScanLength) continue
            val t = Field.gridScan[st.xi][st.yi]
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

    override fun draw() = with(GLRenderHelper.JGL) {

        val c = CColor(this@Tower.color)
        glColor4f(c.r, c.g, c.b, c.a)
        GLRenderHelper.Rect(this@Tower.pos.x, this@Tower.pos.y, this@Tower.size, this@Tower.size, true)

        GLRenderHelper.Circle(this@Tower.center.x, this@Tower.center.y, this@Tower.targetRange, false)
        glColor4f(c.r, c.g, c.b, .15f)
        GLRenderHelper.Circle(this@Tower.center.x, this@Tower.center.y, this@Tower.targetRange, true)

        //projectiles
        this@Tower.projectiles.forEach { it.draw() }

        //target tile range
        if (Global.debugMode) {
            drawTargetLines()
            drawSearchedTiles()
        }

        super.draw()
    }

    private fun drawTargetLines() = with(GLRenderHelper.JGL) {

        for (i in sortedTargetList.indices) {
            if (this@Tower.mode == ShootingMod.CLOSEST) if (i == 0) continue
            if (this@Tower.mode == ShootingMod.FARTHEST) if (i == this@Tower.sortedTargetList.size - 1) continue
            if (this@Tower.mode == ShootingMod.EACH) glColor3f(.0f, 1f, .0f) else glColor3f(1f, .0f, .0f)
            GLRenderHelper.Line(
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
            GLRenderHelper.Line(this@Tower.center.x, this@Tower.center.y, e.center.x, e.center.y)
        }
    }

    private fun drawSearchedTiles() = with(GLRenderHelper.JGL) {
        if (searchTiles.isEmpty()) return

        for (st in searchTiles) {
            val c_ = CColor(Color.BLACK)
            glColor4f(c_.r, c_.g, c_.b, .3f)
            GLRenderHelper.Rect(
                st.xi * Field.gridScanTileSize * 1f,
                st.yi * Field.gridScanTileSize * 1f,
                Field.gridScanTileSize * 1f,
                Field.gridScanTileSize * 1f,
                true,
                2f
            )
        }
//        val sqF = searchTiles.first()
//        val sqL = searchTiles.last()
//
//        val xmin = Math.min(sqF.xi, sqL.xi)
//        val ymin = Math.min(sqF.yi, sqL.yi)
//        val xmax = Math.max(sqF.xi, sqL.xi)
//        val ymax = Math.max(sqF.yi, sqL.yi)
//
//        val w = xmax - xmin + 1
//        val h = ymax - ymin + 1
//
//        val c = CColor(color)
//        glColor4f(c.r, c.g, c.b, c.a)
//        Drawing.Rect(
//            xmin * Field.gridTileSize * 1f,
//            ymin * Field.gridTileSize * 1f,
//            w * Field.gridTileSize * 1f,
//            h * Field.gridTileSize * 1f,
//            false,
//            2f
//        )
    }

    override fun drawName(customName: String) {
        super.drawName("$customName T${this.timer.current} D${this.damage}")
    }
}
