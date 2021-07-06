package me.jikud.tdgame.world.obj.towers

import me.jikud.engine.core.helpers.PPoint
import me.jikud.tdgame.TDMain
import java.awt.Color

class Tower_Closest(pos: PPoint, name: String) : Tower(pos, name, 20f, Color.ORANGE.rgb) {

    companion object {
        var count = 0
    }

    init {
        targetRange = (10 +.5f) * TDMain.bs
        count++
        mode = ShootingMod.CLOSEST
        shootingSpeed = .3
        damage = 20
    }
}