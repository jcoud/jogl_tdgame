package me.jikud.tdgame.world.obj.towers

import me.jikud.tdgame.helpers.PPoint
import java.awt.Color

class Tower_Closest(pos: PPoint, name: String) : Tower(pos, name, size = 20f, color = Color.ORANGE.rgb) {
    init {
        mode = ShootingMod.CLOSEST
        shootingSpeed = .3
        damage = 20
    }
}