package me.jikud.tdgame.world.field

import me.jikud.tdgame.helpers.PPoint
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
                Entity::class ->
                    if (entityListOrder.none { it.pos.toIndex() == pos.toIndex() }) null
                    else entityListOrder.filter { it.pos.toIndex() == pos.toIndex() }[0] as T
                Tower::class ->
                    if (towerListOrder.none { it.pos.toIndex() == pos.toIndex() }) null
                    else towerListOrder.filter { it.pos.toIndex() == pos.toIndex() }[0] as T
                NodePoint::class ->
                    if (nodeListOrder.none { it.pos.toIndex() == pos.toIndex() }) null
                    else nodeListOrder.filter { it.pos.toIndex() == pos.toIndex() }[0] as T
                else -> null
            }
        }
    }

    fun <E : TileObj> LinkedList<E>.getByPos(pos: PPoint): E? {
//        this.forEach {
//            if (it.center.dist(pos) <= it.size * it.size)
//                return it
//        }
        return this.getByPosIndex(pos.toIndex())
    }

    private fun <E : TileObj> LinkedList<E>.containsFieldIndex(arrInd: Int): Boolean {
        this.forEach {
            if (it.pos.toIndex() == arrInd)
                return true
        }
        return false
    }

    private fun <E : TileObj> LinkedList<E>.getByPosIndex(posInd: Int): E? {
        this.forEach {
            if (it.pos.toIndex() == posInd)
                return it
        }
        return null
    }

    private fun <E : TileObj> LinkedList<E>.removeAtPosIndex(posInd: Int) {
        this.remove(this.getByPosIndex(posInd))
    }

    var task = TaskType.NONE

    enum class TaskType {
        NEW_GAME, RUN, STOP, NONE
    }

    lateinit var entityListOrder: LinkedList<Entity>
    lateinit var nodeListOrder: LinkedList<NodePoint>
    lateinit var towerListOrder: LinkedList<Tower>
    const val gridTileSize = me.jikud.tdgame.TDMain.bs * 4
    const val gridLength = me.jikud.tdgame.TDMain.fieldWidth / gridTileSize
    val grid = Array(gridLength) { x -> Array(gridLength) { y -> FieldTile(x, y) } }


    private lateinit var start: Starter
    private lateinit var end: Ender

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
        start = Starter(PPoint(s.x.toFloat(), s.y.toFloat()), "Start", Color.YELLOW.rgb)
        nodeListOrder.add(start)
//        val ar = ArrayList<NodePoint>(nodeListOrder.values)
//        for (i in 1 until ar.size-1) {
//            nodeListOrder
//        }
//
        for ((k, v) in data) {
            if (k == "Start" || k == "End") continue
            nodeListOrder.add(NodePoint(PPoint(v.x.toFloat(), v.y.toFloat()), k, Color.BLACK.rgb))
        }
        end = Ender(PPoint(e.x.toFloat(), e.y.toFloat()), "End", Color.MAGENTA.rgb)
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
            is NodePoint -> {
                if (nodeListOrder.containsFieldIndex(obj.pos.toIndex()) || nodeListOrder.contains(obj)) return
                val ln = nodeListOrder.last()
                nodeListOrder[nodeListOrder.size - 1] = TileObjUtils.makeWithRandomParams(ln.pos)
                end = obj as Ender
                nodeListOrder.add(end)
            }
            is Tower -> {
                if (towerListOrder.containsFieldIndex(obj.pos.toIndex()) || towerListOrder.contains(obj)) return
                towerListOrder.add(obj)
            }
        }
    }

    private fun run() {
        grid.forEach { it.forEach(FieldTile::clear) }
        updateTiles()
        FieldProcessorQueue.unloadActionQueue()
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
        if (byPosIndex is Ender || byPosIndex is Starter) return
        nodeListOrder.removeAtPosIndex(posInd)
    }


    fun removeTowerAt(posInd: Int) {
        if (hasTowerAt(posInd)) towerListOrder.removeAtPosIndex(posInd)
        else return
    }

    fun hasEmptyTileAt(pos: PPoint): Boolean {
        return !hasTowerAt(pos.toIndex()) && !hasNodeAt(pos.toIndex())
    }
}


