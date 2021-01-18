package jikud.tdgame.core

import java.math.BigDecimal
import java.math.RoundingMode

object GameTime {

    private val timer = Timer().apply { start() }

    enum class TimerNames {
        MLS, SEC, MIN, HRS
    }

    fun mls(t: Double = timer.current): Int {
        return (t % 1000).toInt()
    }

    fun sec(t: Double = timer.current): Double {
        return BigDecimal(t / 1000 % 60).setScale(3, RoundingMode.DOWN).toDouble()
    }

    fun min(t: Double = timer.current): Double {
        return BigDecimal(t / 1000 / 60 % 60).setScale(3, RoundingMode.DOWN).toDouble()
    }

    fun hrs(t: Double = timer.current): Double {
        return BigDecimal(t / 1000 / 3600).setScale(3, RoundingMode.DOWN).toDouble()
    }

    override fun toString(): String {
        return "{now: ${timer.current} | " +
                "mls: ${mls()} | " +
                "sec: ${sec()} | " +
                "min: ${min()} | " +
                "hrs: ${hrs()}}"
    }

    class Timer {
        private var timeSinceStarted = 0L
        val current get() = if (started) (System.currentTimeMillis() - timeSinceStarted).toDouble() else 0.0
        private var started = false

        fun start() {
            if (started) return
            started = true
            timeSinceStarted = System.currentTimeMillis()
        }

        fun trigger(time: Double, timerName: TimerNames = TimerNames.SEC): Boolean {
            start()
            if (current == 0.0) return false
            val t: Double = when (timerName) {
                TimerNames.MLS -> mls(current).toDouble()
                TimerNames.SEC -> sec(current)
                TimerNames.MIN -> min(current)
                TimerNames.HRS -> hrs(current)
            }
            if (t >= time) {
                timeSinceStarted = System.currentTimeMillis()
                return true
            }
            return false
        }
    }
}