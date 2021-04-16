package me.jikud.tdgame.mapping

data class WorldSaveDataHolder(val id: Int, val x: Int, val y: Int) : Comparable<WorldSaveDataHolder> {
    override fun compareTo(other: WorldSaveDataHolder): Int {
        return id.compareTo(other.id)
    }
}