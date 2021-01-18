package jikud.tdgame.gui.sets

import jikud.tdgame.TDMain
import jikud.tdgame.gui.GUI
import jikud.tdgame.gui.hud.components.HudButton
import jikud.tdgame.gui.hud.components.HudComponent
import jikud.tdgame.gui.hud.components.HudPanel
import jikud.tdgame.gui.hud.components.HudText
import jikud.tdgame.gui.hud.event.EventMouseAction
import jikud.tdgame.gui.hud.event.GUIEventType
import jikud.tdgame.gui.hud.event.MouseActionAbstract
import jikud.tdgame.helpers.CColor
import jikud.tdgame.helpers.PPoint
import jikud.tdgame.world.obj.TileObj
import jikud.tdgame.world.obj.Tower
import java.awt.Color

class TowerGUI : GUI() {

    private var tower: Tower? = null
    private val p = HudPanel()

    private val destroy = HudButton("Destroy")
    private val shootingMod = HudButton("Shooting Mod")
    private val levelUp = HudButton("Level Up")

    private val text = HudText()

    fun setTower(tower: Tower) {
        if (this.tower == tower) return
        this.tower = tower
        text.background = CColor(tower.color)
        destroy.active = true
    }

    init {
        p.pos = PPoint(0, TDMain.fieldHeight)
        p.width = TDMain.guiWidth
        p.height = TDMain.guiHeight
        p.background = CColor(Color.CYAN)
        p.alignment = HudComponent.Alignment.HORIZONTAL

        text.showBackground = true
        text.textColor = CColor(Color.BLACK)
        text.text = if (tower == null) "" else tower!!.name
        text.width = TDMain.guiHeight
        text.height = TDMain.guiHeight

        destroy.width = TDMain.guiHeight
        destroy.height = TDMain.guiHeight
        destroy.action = object : MouseActionAbstract(destroy) {
            override fun mouseAction(e: EventMouseAction) {
                //                if (tower == null) return
                super.mouseAction(e)
                if (e.type == GUIEventType.CLICKED) {
                    tower?.state = TileObj.TileObjState.DEAD
                    destroy.active = false
                }

            }
        }

        shootingMod.width = TDMain.guiHeight
        shootingMod.height = TDMain.guiHeight
        shootingMod.action = object : MouseActionAbstract(shootingMod) {
            override fun mouseAction(e: EventMouseAction) {
                super.mouseAction(e)
                if (e.type == GUIEventType.CLICKED) {
                    tower!!.mode = tower!!.mode.next()
//                    shootingMod.active = false
                }

            }
        }
        levelUp.width = TDMain.guiHeight
        levelUp.height = TDMain.guiHeight
        levelUp.action = object : MouseActionAbstract(levelUp) {
            override fun mouseAction(e: EventMouseAction) {
                super.mouseAction(e)
                if (e.type == GUIEventType.CLICKED) {
                    println("level up ${tower?.name}")
//                    tower?.state = TileObj.TileObjState.DEAD
//                    levelUp.active = false
                }

            }
        }

        p.addComponent(text)
        p.addComponent(destroy)
        p.addComponent(shootingMod)
        p.addComponent(levelUp)
        p.pack()
    }

    override fun canvas(): HudPanel = p
}