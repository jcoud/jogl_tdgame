package jikud.tdgame.world

import jikud.tdgame.TDMain
import jikud.tdgame.helpers.PPoint
import jikud.tdgame.mapping.MapImpl
import jikud.tdgame.mapping.WorldSaveDataHolder
import jikud.tdgame.world.obj.*
import jikud.tdgame.world.obj.gate.Ender
import jikud.tdgame.world.obj.gate.Starter
import java.awt.Color
import java.util.*

object Field {


    object FieldTileSelection {
        inline fun <reified T : TileObj> getSelectedObj(pos: PPoint): T? {
            return when (T::class) {
                Entity::class -> if (entityListOrder.getByPos(pos) == null) null else entityListOrder.getByPos(pos) as T
                Tower::class -> if (towerListOrder.getByPos(pos) == null) null else towerListOrder.getByPos(pos) as T
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
        NEW_GAME, MOVE, NONE
    }

    lateinit var entityListOrder: LinkedList<Entity>
    lateinit var nodeListOrder: LinkedList<NodePoint>
    lateinit var towerListOrder: LinkedList<Tower>
    const val gridSize = TDMain.bs * 4
    const val gridLength = TDMain.fieldWidth / gridSize
    val grid = Array(gridLength) { x -> Array(gridLength) { y -> FieldTile(x, y) } }


    private lateinit var start: Starter
    private lateinit var end: Ender
    var doMove: Boolean = true

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
        newStart()
    }

    private fun setupRandomMap() {
        start = TileObjUtils.makeWithRandomParams()
        nodeListOrder.add(start)

        nodeListOrder.add(TileObjUtils.makeWithRandomParams())
        nodeListOrder.add(TileObjUtils.makeWithRandomParams())
        nodeListOrder.add(TileObjUtils.makeWithRandomParams())

        end = TileObjUtils.makeWithRandomParams()
        nodeListOrder.add(end)

        towerListOrder.add(TileObjUtils.makeWithRandomParams())
        towerListOrder.add(TileObjUtils.makeWithRandomParams())
        towerListOrder.add(TileObjUtils.makeWithRandomParams())
    }

    fun newStart() {
        entityListOrder.clear()
        nodeListOrder.clear()
        towerListOrder.clear()
        val sdtMap = MapImpl.read()
        if (sdtMap.isEmpty())
            setupRandomMap()
        else
            setupMapFromSDT(sdtMap)
        doMove = true
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

    fun applyTasks() {
        when (task) {
            TaskType.MOVE -> {
                run()
            }
            TaskType.NEW_GAME -> {
                newStart()
            }
            TaskType.NONE -> {
                doMove = false
                return
            }
        }
        task = TaskType.MOVE
    }

    private fun hasNodeAt(posInd: Int): Boolean {
        return if (nodeListOrder.containsFieldIndex(posInd)) {
            val pi = nodeListOrder.getByPosIndex(posInd)
            pi != null && pi.isNotEmpty()
        } else false
    }


    private fun hasTowerAt(posInd: Int): Boolean {
        return if (towerListOrder.containsFieldIndex(posInd)) {
            val pi = towerListOrder.getByPosIndex(posInd)
            pi != null && pi.isNotEmpty()
        } else false

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


