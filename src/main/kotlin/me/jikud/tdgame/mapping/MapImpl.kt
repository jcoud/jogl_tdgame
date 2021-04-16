package me.jikud.tdgame.mapping

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import me.jikud.tdgame.world.field.Field
import java.io.File

object MapImpl {


    /**
     * mapping;
     * E - entity
     * N - node
     * S - start node
     * X - end node
     * T - tower
     * ? - other
     */

    private lateinit var mapFile: File

    fun init() {
        mapFile = File("map-1.sdt")
    }

    fun save() {
        val data = Field.saveField()
        val b = save(data)
        if (!b) throw Error("Map wasn't written to file. File not writable.")
    }

    private fun save(data: HashMap<String, WorldSaveDataHolder>): Boolean {
        val tw = TomlWriter
            .Builder()
            .indentValuesBy(2)
            .indentTablesBy(2)
            .padArrayDelimitersBy(2)
            .build();
        val s = tw.write(data)
//        val gs = GsonBuilder()
//            .setPrettyPrinting()
//            .create()
//        val s = gs.toJson(data)
        if (mapFile.canWrite()) {
            mapFile.writeText(s)
            return true
        }
        return false
    }

    fun read(): HashMap<String, WorldSaveDataHolder> {
        if (!mapFile.exists()) return HashMap()
        val retMap = Toml().read(mapFile)
        val mm = LinkedHashMap<String, WorldSaveDataHolder>()
        val ma = LinkedHashMap<String, WorldSaveDataHolder>()
        retMap.toMap().keys.forEach { k ->
            val a_ = retMap.getTable(k)
            val x = a_.getLong("x").toInt()
            val y = a_.getLong("y").toInt()
            val id = a_.getLong("id").toInt()
            mm[k] = WorldSaveDataHolder(id, x ,y)
//            a.add(WorldSaveDataHolder(id, x ,y))
        }
        val a = ArrayList(mm.values)
        a.sort()
        a.forEach { o ->
            val i = mm.values.indexOf(o)
            val w = mm.keys.elementAt(i)
            ma[w] = mm[w]!!
        }
        return ma
    }
}