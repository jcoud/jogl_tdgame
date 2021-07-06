package me.jikud.tdgame.world.field

import me.jikud.engine.core.helpers.PPoint
import me.jikud.tdgame.TDMain
import me.jikud.tdgame.mapping.MapImpl
import me.jikud.tdgame.mapping.WorldSaveDataHolder
import me.jikud.tdgame.world.obj.*
import me.jikud.tdgame.world.obj.gates.Ender
import me.jikud.tdgame.world.obj.gates.Starter
import me.jikud.tdgame.world.obj.towers.Tower
import java.awt.Color
import java.util.*

object Field {


    object FieldTileSelection {
        inline fun <reified T : TileObj> getSelectedObj(pos: PPoint): T? {
            return when (T::class) {
                Entity::class -> entityListOrder.getByPos(pos) as T?
                Tower::class -> towerListOrder.getByPos(pos) as T?
                NodePoint::class -> nodeListOrder.getByPos(pos) as T?
                else -> null
            }
        }
    }

    fun <E : TileObj> LinkedList<E>.getByPos(pos: PPoint): E? {
        return this.getByPosIndex(pos.toIndex(TDMain.bs, TDMain.n))
    }

    private fun <E : TileObj> LinkedList<E>.getByPosIndex(posInd: Int): E? {
        this.forEach {
            if (it.pos.toIndex(TDMain.bs, TDMain.n) == posInd)
                return it
        }
        return null
    }

    private fun <E : TileObj> LinkedList<E>.containsFieldIndex(arrInd: Int): Boolean {
        this.forEach {
            if (it.pos.toIndex(TDMain.bs, TDMain.n) == arrInd)
                return true
        }
        return false
    }


    private fun <E : TileObj> LinkedList<E>.removeAtPosIndex(posInd: Int) {
        this.remove(this.getByPosIndex(posInd))
    }

    var task = TaskType.NONE

    enum class TaskType {
        NEW_GAME, RUN, STOP, NONE
    }

    var entityListOrder: LinkedList<Entity> = LinkedList()
    var nodeListOrder: LinkedList<NodePoint> = LinkedList()
    var towerListOrder: LinkedList<Tower> = LinkedList()
    const val gridScanTileSize = TDMain.bs * 4
    const val gridScanLength = TDMain.fieldWidth / gridScanTileSize
    val gridScan = Array(gridScanLength) { x -> Array(gridScanLength) { y -> FieldTile(x, y) } }


    lateinit var start: Starter
    lateinit var end: Ender

    fun saveField(): HashMap<String, WorldSaveDataHolder> {
        val a = LinkedHashMap<String, WorldSaveDataHolder>()
        for (i in 0 until nodeListOrder.size) {
            val n = nodeListOrder[i]
            a[n.name] = WorldSaveDataHolder(i, n.pos.xi, n.pos.yi)
        }
        return a
    }

    private fun setupMapFromSDT(data: HashMap<String, WorldSaveDataHolder>) {
        nodeListOrder = LinkedList()
        val s = data["Start"]!!
        val e = data["End"]!!
        start = Starter(PPoint(s.x, s.y), "Start", Color.YELLOW.rgb)
        nodeListOrder.add(start)
//        val ar = ArrayList<NodePoint>(nodeListOrder.values)
//        for (i in 1 until ar.size-1) {
//            nodeListOrder
//        }
//
        for ((k, v) in data) {
            if (k == "Start" || k == "End") continue
            nodeListOrder.add(NodePoint(PPoint(v.x.toFloat(), v.y.toFloat()), k, Color.WHITE.rgb))
        }
        end = Ender(PPoint(e.x, e.y), "End", Color.MAGENTA.rgb)
        nodeListOrder.add(end)
    }

    fun init() {
        entityListOrder = LinkedList()
        nodeListOrder = LinkedList()
        towerListOrder = LinkedList()
        restart()
    }

    private fun setupRandomMap() {
        start = TileObjUtils.makeWithRandomParams()
        nodeListOrder.add(start)

        nodeListOrder.add(TileObjUtils.makeWithRandomParams())
        nodeListOrder.add(TileObjUtils.makeWithRandomParams())
        nodeListOrder.add(TileObjUtils.makeWithRandomParams())

        end = TileObjUtils.makeWithRandomParams()
        nodeListOrder.add(end)

    }

    private fun restart() {
        entityListOrder.clear()
        nodeListOrder.clear()
        towerListOrder.clear()
        val sdtMap = MapImpl.read()
        if (sdtMap.isEmpty())
            setupRandomMap()
        else
            setupMapFromSDT(sdtMap)
    }

    fun add(obj: TileObj) {
        when (obj) {
            is Entity -> {
                if (entityListOrder.contains(obj)) return
                entityListOrder.add(obj)
            }
            is Starter -> {
                if (nodeListOrder.isEmpty()) {
                    nodeListOrder.add(obj)
                    return
                }
                val node = nodeListOrder.first()
                nodeListOrder[0] = TileObjUtils.makeWithRandomParams(node.pos)
                start = obj
                nodeListOrder.addFirst(obj)
                return
            }
            is Ender -> {
                if (nodeListOrder.isEmpty()) {
                    nodeListOrder.add(obj)
                    return
                }
                val node = nodeListOrder.last()
                if (node !is Starter) {
                    nodeListOrder[nodeListOrder.size - 1] = TileObjUtils.makeWithRandomParams(node.pos)
                }
                end = obj
                nodeListOrder.add(obj)
                return
            }
            is NodePoint -> {
                if (nodeListOrder.isEmpty()) {
                    nodeListOrder.add(obj)
                    return
                }
                if (!nodeListOrder.none { it.pos.toIndex(TDMain.bs, TDMain.n) == obj.pos.toIndex(TDMain.bs, TDMain.n) }) return
                nodeListOrder.add(nodeListOrder.size - 1, obj)
            }
            is Tower -> {
                if (towerListOrder.containsFieldIndex(obj.pos.toIndex(TDMain.bs, TDMain.n)) || towerListOrder.contains(obj)) return
                towerListOrder.add(obj)
            }
        }
    }

    private fun run() {
        gridScan.forEach { it.forEach(FieldTile::clear) }
        updateTiles()
    }

    private fun updateTiles() {
        entityListOrder.forEach(IUpdatable::update)
        nodeListOrder.forEach(IUpdatable::update)
        towerListOrder.forEach(IUpdatable::update)
    }

    fun update() {
        when (task) {
            TaskType.RUN -> {
                run()
            }
            TaskType.NEW_GAME -> {
                restart()
            }
            TaskType.STOP,
            TaskType.NONE -> return
        }
        task = TaskType.RUN
    }

    private fun hasNodeAt(posInd: Int): Boolean {
        val pi = nodeListOrder.getByPosIndex(posInd)
        return pi != null && pi.isNotEmpty()
    }


    private fun hasTowerAt(posInd: Int): Boolean {
        val pi = towerListOrder.getByPosIndex(posInd)
        return pi != null && pi.isNotEmpty()
    }

    fun removeNodeAt(posInd: Int) {
        if (!hasNodeAt(posInd)) return
        val byPosIndex = nodeListOrder.getByPosIndex(posInd)
        when (byPosIndex) {
            is Starter -> {
                val i = if (nodeListOrder.size > 0) 1 else 0
                val n = nodeListOrder[i]
                start = Starter(n.pos, start.name, start.color)
                nodeListOrder[i] = start
            }
            is Ender -> {
                val n = nodeListOrder[nodeListOrder.size - 2]
                end = Ender(n.pos, end.name, end.color)
                nodeListOrder[nodeListOrder.size - 2] = end
            }
        }
        nodeListOrder.removeAtPosIndex(posInd)
    }


    fun removeTowerAt(posInd: Int) {
        if (hasTowerAt(posInd)) towerListOrder.removeAtPosIndex(posInd)
        else return
    }

    fun hasEmptyTileAt(pos: PPoint): Boolean {
        val b1 = hasTowerAt(pos.toIndex(TDMain.bs, TDMain.n))
        val b2 = hasNodeAt(pos.toIndex(TDMain.bs, TDMain.n))
        return !b1 && !b2
    }

    fun clear() {
        nodeListOrder.clear()
        towerListOrder.clear()
        entityListOrder.clear()
    }
}


