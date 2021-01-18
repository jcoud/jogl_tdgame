package jikud.tdgame.helpers

import jikud.tdgame.TDMain
import java.awt.Point
import kotlin.math.sqrt

@Suppress("SpellCheckingInspection")
class PPoint(var x: Float, var y: Float) {
    constructor(xi: Int, yi: Int) : this(xi.toFloat(), yi.toFloat())
    constructor(pPoint: PPoint) : this(pPoint.x, pPoint.y)
    constructor(point: Point) : this(point.getX().toFloat(), point.getY().toFloat())

    companion object {
        val ZERO = PPoint(.0f, .0f)
        val NONE = PPoint(-1f, -1f)
    }

    val xi get() = this.x.toInt()
    val yi get() = this.y.toInt()


    fun translate(x: Float, y: Float): PPoint {
        this.x += x
        this.y += y
        return this
    }

    fun sub(point: PPoint): PPoint {
        return PPoint(this.x - point.x, this.y - point.y)
    }

    fun mult(value: Int): PPoint {
        return this.translate(value.toFloat(), value.toFloat())
    }

    fun setLocation(x: Float, y: Float): PPoint {
        this.x = x
        this.y = y
        return this
    }

    fun distanceSq(pos: PPoint): Float {
        return sqrt(dist(pos))
    }

    fun concat(pos: PPoint): PPoint {
        return PPoint(x + pos.x, y + pos.y)
    }

    fun dist(pos: PPoint): Float {
        val xx = this.x - pos.x
        val yy = this.y - pos.y
        return xx * xx + yy * yy
    }

    fun scale(value: Int): PPoint {
        return PPoint(this.x * value, this.y * value)
    }

    fun devide(value: Int): PPoint {
        return PPoint(this.x / value, this.y / value)
    }

//    fun descendInt(value: Int): PPoint {
//        return PPoint(this.xi.div(value), this.yi.div(value))
//    }

    fun snapToGrid(): PPoint {
        val xi = (this.x / TDMain.bs).toInt()
        val yi = (this.y / TDMain.bs).toInt()
        val xd = (xi * TDMain.bs).toFloat()
        val yd = (yi * TDMain.bs).toFloat()
        return PPoint(xd, yd)
    }

    fun toIndex(size: Int = TDMain.bs): Int {
        val d = devide(size)
        return d.xi * TDMain.n + d.yi
    }

    override fun toString(): String {
        return "{x: $x, y: $y}"
    }

    override fun equals(other: Any?): Boolean {
        return other is PPoint && other.x == this.x && other.y == this.y
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}