package jikud.tdgame.world.obj

import jikud.tdgame.TDMain
import jikud.tdgame.helpers.PPoint
import jikud.tdgame.world.Field
import jikud.tdgame.world.obj.gate.Ender
import jikud.tdgame.world.obj.gate.Starter
import java.awt.Color
import java.lang.reflect.Type
import kotlin.random.Random

object TileObjUtils {

    @Suppress("UNCHECKED_CAST")
    fun <T : TileObj> getEmptyTile(w: Type): T {
        return when (w.typeName) {
            Entity::class.java.name ->
                Entity(PPoint.ZERO, "EMPTY", 0f, Color.BLACK.rgb, 0f) as T
            Tower::class.java.name ->
                Tower(PPoint.ZERO, "EMPTY", 0f, Color.BLACK.rgb) as T
            NodePoint::class.java.name ->
                NodePoint(PPoint.ZERO, "EMPTY", Color.BLACK.rgb) as T
            else -> throw Error("dwqw $w")
        }
    }

    inline fun <reified T : TileObj> makeWithRandomParams(pos: PPoint): T {
        return when (T::class) {
            Entity::class -> {
//                val s = 15 + Random.nextInt(10)
                val s = 20
                Entity.count++
                Entity(
                    pos.snapToGrid().mult(TDMain.bs.minus(s) / 2),
                    "E${Entity.count + 1}",
                    s * 1f,
                    Color.WHITE.rgb,
//                    Random.nextInt(0xffffff),
                    (Random.nextInt(10) + 4f) / 5f
                ).apply {
                    health = (Random.nextInt(300) + 2000)
                } as T
            }
            NodePoint::class -> {
                NodePoint.count++
                NodePoint(
                    pos.snapToGrid().mult(TDMain.bs / 2),
                    "N${Field.nodeListOrder.size + 1}",
                    Color.BLACK.rgb
                ) as T
            }
            Tower::class -> {
                val s = 20
                Tower.count++
                Tower(
                    pos.snapToGrid().mult((TDMain.bs - s) / 2),
                    "T${Field.towerListOrder.size + 1}",
                    s * 1f,
                    Random.nextInt(0xffffff)
//                    Color.BLUE.rgb
                ).apply { damage = (Random.nextInt(45) + 5) } as T
            }
            Starter::class -> {
                NodePoint.count++
                Starter(
                    pos.snapToGrid().mult(TDMain.bs / 2),
                    "Start",
                    Color.YELLOW.rgb
                ) as T
            }
            Ender::class -> {
                NodePoint.count++
                Ender(
                    pos.snapToGrid().mult(TDMain.bs / 2),
                    "End",
                    Color.MAGENTA.rgb
                ) as T
            }
            else ->
                throw IllegalArgumentException("Class not parent from ${TileObj::class.simpleName}. Found ${T::class.simpleName}")
        }
    }

    inline fun <reified T : TileObj> makeWithRandomParams(): T {
        return makeWithRandomParams(
            PPoint(
                (TDMain.bs + Random.nextInt(TDMain.fieldWidth - TDMain.bs)).toFloat(),
                (TDMain.bs + Random.nextInt(TDMain.fieldHeight - TDMain.bs)).toFloat()
            )
        )
    }
}