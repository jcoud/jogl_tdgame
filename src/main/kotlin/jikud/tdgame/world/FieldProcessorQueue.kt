package jikud.tdgame.world

import jikud.tdgame.world.obj.*
import java.util.*

object FieldProcessorQueue {
    private lateinit var queueAddList: LinkedList<TileObj>
    private lateinit var queueRemoveList: LinkedList<TileObj>

    fun unloadActionQueue() {
        queueAddList.forEach(Field::add)
        queueRemoveList.forEach {
            when (it) {
                is Tower -> Field.removeTowerAt(it.pos.toIndex())
                is NodePoint -> Field.removeNodeAt(it.pos.toIndex())
                is Projectile -> Field.towerListOrder.forEach { t -> t.projectiles.remove(it) }
                is Entity -> {
                    Field.entityListOrder.remove(it)
                    Field.towerListOrder.forEach { t ->
                        if (t.sortedTargetList.contains(it))
                            t.sortedTargetList.remove(it)
                    }
                }
            }
        }

        queueAddList.clear()
        queueRemoveList.clear()
    }

    fun queueAdd(obj: TileObj) {
        val b1 = queueAddList.stream().anyMatch { it.name == obj.name }
        val b2 = queueAddList.contains(obj)
        if (b1 || b2) return
        queueAddList.add(obj)
    }

    fun queueRemove(tileObj: TileObj) {
        queueRemoveList.add(tileObj)
    }

    fun queueRemoveByEntName(entityName: String) {
        Field.entityListOrder.forEach {
            queueRemove(if (it.name == entityName) it else return)
        }
    }

    fun init() {
        queueAddList = LinkedList()
        queueRemoveList = LinkedList()
    }
}