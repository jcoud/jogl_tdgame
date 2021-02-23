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
        private var timeSinceStarted = 0.0
        private var timeSinceStartedActual = 0.0
        val actual get() = System.currentTimeMillis() - timeSinceStartedActual
        val current get() =
            if (started)
                if (paused)
                    internalLastUpdated
                else
                    System.currentTimeMillis() - timeSinceStarted - deltaPaused
            else 0.0

        //TODO: доделать возможность останавливать время на паузу
        var started = false
        private var internalLastUpdated = 0.0
        private var paused = false
        private var timeSincePaused = 0.0
        private val deltaPaused get() = if (paused) System.currentTimeMillis() - timeSincePaused else 0.0

        fun start() {
            if (started) return
            started = true
            timeSinceStarted = System.currentTimeMillis().toDouble()
            timeSinceStartedActual = System.currentTimeMillis().toDouble()
        }

        fun pause() {
            paused = true
            internalLastUpdated = current
            timeSincePaused = System.currentTimeMillis().toDouble()
        }

        fun unpause() {
            paused = false
        }

        fun trigger(time: Double, timerName: TimerNames = TimerNames.SEC): Boolean {
//            start()
            if (current == 0.0) return false
            val t: Double = when (timerName) {
                TimerNames.MLS -> mls(current).toDouble()
                TimerNames.SEC -> sec(current)
                TimerNames.MIN -> min(current)
                TimerNames.HRS -> hrs(current)
            }
            if (t >= time) {
                timeSinceStarted = System.currentTimeMillis().toDouble()
                return true
            }
            return false
        }

        override fun toString(): String {
            return "{ac: $actual | " +
                    "cu: $current | " +
                    "lu: $internalLastUpdated | " +
                    "pa: $paused | " +
                    "tp: $timeSincePaused | " +
                    "dp: $deltaPaused}"
        }
    }
}