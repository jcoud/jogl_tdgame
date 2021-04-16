package me.jikud.tdgame

import java.awt.Canvas
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D
import javax.swing.JFrame
import javax.swing.WindowConstants

class Test2 {

    class Player {
        var U_ID = 0
        init {
            ID++
            U_ID = ID
            if (test.none{it.U_ID == this.U_ID})
                test.add(this)
        }

        fun str() {
            println("id: $U_ID | ${this.hashCode()}")
        }
    }
    companion object {
        var ID = 0
        val test = ArrayList<Player>()

        @JvmStatic
        fun main(args: Array<String>) {

            Player()
            Player()
            Player()
            Player()
            Player()
            Player()
            Player()
            Player()


            test.forEach(Player::str)
        }
    }
}

class Test : Canvas() {
    init {
        JFrame("Test").apply {
            defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            isVisible = true
            preferredSize = Dimension(400, 400)
            add(this@Test)
            pack()
            setLocationRelativeTo(null)
        }
        var timeDiff: Long
        var sleepTime: Long
        var afterTime: Long
        var beforeTime: Long
        var overSleepTime = 0L

        val fps = 24 // the desire jikud.tdgame.core.FPS

        val period = 1000000000L / fps

        while (true) {
            beforeTime = System.nanoTime()

            // your game logic
            update()
            afterTime = System.nanoTime()

            // timeDiff is time needed by the update process
            timeDiff = afterTime - beforeTime
            sleepTime = period - timeDiff - overSleepTime
            overSleepTime = if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1000000L) // sleeptTime is in nano secs and sleep expects millis
                } catch (e: InterruptedException) {
                }

                // checks if the thread has slept more than desired
                System.nanoTime() - afterTime - sleepTime
            } else {
                // negative sleeptTime means that the system can't be update at the desired rate
                0L
            }
        }
    }

    private fun update() {
        a()
    }

    private fun a() {
        val bs = bufferStrategy
        if (bs == null) {
            createBufferStrategy(3)
            requestFocus()
            return
        }
        val g = bs.drawGraphics as Graphics2D
        g.clearRect(0, 0, 400, 400)
        g.translate(40, 40)
        g.background = Color(134,30,50,60)
        g.color = Color.BLACK
        g.drawString("${System.currentTimeMillis() % 1000}", 0, 0)


        bs.dispose()
        bs.show()
    }
}