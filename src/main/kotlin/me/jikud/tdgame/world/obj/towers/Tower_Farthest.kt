package me.jikud.tdgame.world.obj.towers

import me.jikud.tdgame.helpers.PPoint
import java.awt.Color

class Tower_Farthest(pos: PPoint, name: String) : Tower(pos, name, 20f, Color.GREEN.rgb) {
    init {
        mode = ShootingMod.FARTHEST
        shootingSpeed = 1.0
        damage = 50

    }
}