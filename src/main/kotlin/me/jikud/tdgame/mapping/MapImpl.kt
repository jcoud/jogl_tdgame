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
        prepareFile()
    }

    private fun prepareFile() {
        mapFile = File("map-1.toml")
        mapFile.createNewFile()
    }

    fun save() {
        val data = Field.saveField()
        val b = save(data)
        if (b) println("Map saved!")
        else throw Error("Map wasn't written to file. File not writable.")
    }

    private fun save(data: HashMap<String, WorldSaveDataHolder>): Boolean {
        val tw = TomlWriter
            .Builder()
            .indentValuesBy(2)
            .indentTablesBy(2)
            .padArrayDelimitersBy(2)
            .build()
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
        if (!mapFile.exists()) throw Error("Map file does not exists!")
        val ma = Toml().read(mapFile).toMap()
        val mm = HashMap<String, WorldSaveDataHolder>()
        for ((k, v) in ma) {
            v as HashMap<String, Long>
            mm[k as String] = WorldSaveDataHolder(v["id"]!!.toInt(), v["x"]!!.toInt(), v["y"]!!.toInt())
        }
//        ma = HashMap<String, WorldSaveDataHolder>()
//        val a = ArrayList(mm.values)
//        a.sort()
//        a.forEach {
//            val i = mm.values.indexOf(it)
//            val w = mm.keys.elementAt(i)
//            ma[w] = mm[w]!!
//        }
//        return map
//        val mm = LinkedHashMap<String, WorldSaveDataHolder>()
//        val ma = LinkedHashMap<String, WorldSaveDataHolder>()
//        val retMap = Toml().read(mapFile)
//        retMap.toMap().keys.forEach { k ->
//            val a_ = retMap.getTable(k)
//            val x = a_.getLong("x").toInt()
//            val y = a_.getLong("y").toInt()
//            val id = a_.getLong("id").toInt()
//            mm[k] = WorldSaveDataHolder(id, x ,y)
////            a.add(WorldSaveDataHolder(id, x ,y))
//        }

        return mm
    }
}