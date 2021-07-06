package me.jikud.tdgame.world.field

import me.jikud.engine.core.main.Timer
import me.jikud.tdgame.TDMain
import me.jikud.tdgame.world.obj.*
import me.jikud.tdgame.world.obj.towers.Tower

object FieldProcessorQueue {
    private var queueToAddList = ArrayList<TileObj>()
    private var queueToRemoveList = ArrayList<TileObj>()

    fun unloadActionQueue() {
        queueToAddList.forEach(Field::add)
        queueToRemoveList.forEach {
            if (it is ITimerUpdatable) Timer.timers.remove(it.timer)
            when (it) {
                is Tower -> Field.removeTowerAt(it.pos.toIndex(TDMain.bs, TDMain.n))
                is NodePoint -> Field.removeNodeAt(it.pos.toIndex(TDMain.bs, TDMain.n))
                is Projectile -> Field.towerListOrder.forEach { t -> t.projectiles.remove(it) }
                is Entity -> {
                    Field.entityListOrder.remove(it)
                    Field.towerListOrder.forEach { t -> t.sortedTargetList.remove(it) }
                }
            }
        }
        queueToAddList.clear()
        queueToRemoveList.clear()

    }

    fun queueToAdd(tileObj: TileObj) {
        val b1 = !queueToAddList.none { it.name == tileObj.name }
        val b2 = queueToAddList.contains(tileObj)
        if (b1 || b2) return
        queueToAddList.add(tileObj)
    }

    fun queueToRemove(tileObj: TileObj) {
//        val b1 = queueToRemoveList.stream().anyMatch { it.name == tileObj.name }
        if (!queueToRemoveList.none { it.name == tileObj.name }) return
        queueToRemoveList.add(tileObj)
    }

    fun queueRemoveByEntName(entityName: String) {
        Field.entityListOrder.forEach {
            queueToRemove(if (it.name == entityName) it else return)
        }
    }
}