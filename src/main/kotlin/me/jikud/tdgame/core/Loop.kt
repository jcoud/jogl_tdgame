package me.jikud.tdgame.core

import me.jikud.tdgame.world.field.Field
import kotlin.system.exitProcess

object Loop : Runnable {
    private var fps = 0
    private var ups = 0
    private var running = false
    private lateinit var thread: Thread
    var dt = 0L
    var unprocessed = 0.0
    private const val nsPerTick = 1_000_000_000L / JOGLEntry.FPS

    override fun run() {
        var lastTime = System.nanoTime()
        var shouldRender: Boolean
        fps = 0
        ups = 0
        var lt = System.currentTimeMillis()
        while (running) {
            shouldRender = false
            val currentTime = System.nanoTime()
            unprocessed += (currentTime - lastTime) / nsPerTick
            dt = (currentTime - lastTime) / nsPerTick
            lastTime = currentTime
            while (unprocessed >= 1) {
                ups++
                update()
                unprocessed--
                shouldRender = true
            }
            if (shouldRender) {
                fps++
                render()
            }

            try {
                Thread.sleep((((1 - unprocessed) * 1000).toInt() / JOGLEntry.FPS).toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            if (System.currentTimeMillis() - lt > 1000) {
                lt = System.currentTimeMillis()
//                print("ups: $ups, fps: $fps\r")
                fps = 0
                ups = 0
            }
        }
        exitProcess(-1)

    }

    @Synchronized
    fun start() {
        running = true
        thread = Thread(this, "Thread")
        thread.start()
    }

    @Synchronized
    fun stop() {
        running = false
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private val l = arrayListOf<Double>()

    private fun update() {
        Field.update()
        GameTime.update()
        if (l.none { it == unprocessed }) {
            l.add(unprocessed)
            println(unprocessed)
        }
    }

    private fun render() {
        JOGLEntry.render()
    }

    override fun toString(): String {
        return "{ups: $ups | " +
                "fps: $fps | " +
                "dt: $dt | " +
                "unp: $unprocessed | " +
                "nsp: $nsPerTick}"
    }
}