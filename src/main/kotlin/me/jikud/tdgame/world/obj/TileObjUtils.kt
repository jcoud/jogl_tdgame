package me.jikud.tdgame.world.obj

import me.jikud.engine.core.helpers.PPoint
import me.jikud.tdgame.TDMain
import me.jikud.tdgame.world.field.Field
import me.jikud.tdgame.world.obj.gates.Ender
import me.jikud.tdgame.world.obj.gates.Starter
import me.jikud.tdgame.world.obj.towers.Tower
import java.awt.Color
import java.lang.reflect.Type
import kotlin.random.Random

object TileObjUtils {

    @Suppress("UNCHECKED_CAST")
    fun <T : TileObj> getEmptyTile(w: Type): T {
        return when (w.typeName) {
            Entity::class.java.name ->
                Entity(PPoint.ZERO, "EMPTY", 0f, Color.BLACK.rgb, 0f) as T
            Tower::class.java.name -> object :
                Tower(PPoint.ZERO, "EMPTY", 0f, Color.BLACK.rgb) {} as T
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
                Entity(
                    PPoint.snapToGrid(pos, TDMain.bs * 1f).translate(TDMain.bs.minus(s) / 2f),
                    "E${Entity.count + 1}",
                    s * 1f,
//                        Color.WHITE.rgb,
                    Random.nextInt(0xffffff),
                    (Random.nextInt(10) + 4f) / 5f
                ).apply {
                    health = Random.nextInt(300) + 2000
                } as T
            }
            NodePoint::class -> {
                NodePoint(
                    PPoint.snapToGrid(pos, TDMain.bs * 1f).translate(TDMain.bs / 2f),
                    "N${Field.nodeListOrder.size + 1}",
                    Color.WHITE.rgb
                ) as T
            }
//            Tower::class -> {
//                val s = 20
//                Tower.count++
//                Tower(
//                    pos.snapToGrid().mult((TDMain.bs - s) / 2),
//                    "T${Field.towerListOrder.size + 1}",
//                    s * 1f,
//                    Random.nextInt(0xffffff)
////                    Color.BLUE.rgb
//                ).apply { damage = (Random.nextInt(45) + 5) } as T
//            }
            Starter::class -> {
                Starter(
                    PPoint.snapToGrid(pos, TDMain.bs * 1f).translate(TDMain.bs / 2f),
                    "Start",
                    Color.YELLOW.rgb
                ) as T
            }
            Ender::class -> {
                Ender(
                    PPoint.snapToGrid(pos, TDMain.bs * 1f).translate(TDMain.bs / 2f),
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