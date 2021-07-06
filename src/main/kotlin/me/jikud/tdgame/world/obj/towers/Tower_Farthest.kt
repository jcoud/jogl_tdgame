package me.jikud.tdgame.world.obj.towers

import me.jikud.engine.core.helpers.PPoint
import me.jikud.tdgame.TDMain
import java.awt.Color

class Tower_Farthest(pos: PPoint, name: String) : Tower(pos, name, 20f, Color.GREEN.rgb) {

    companion object {
        var count = 0
    }

    init {
        targetRange = (3 +.5f) * TDMain.bs
        count++
        mode = ShootingMod.FARTHEST
        shootingSpeed = 1.0
        damage = 50

    }
}