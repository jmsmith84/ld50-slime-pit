package utility.timer

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.baseview.BaseView
import com.soywiz.korge.component.UpdateComponent
import com.soywiz.korge.component.attach
import program.Log
import kotlin.time.Duration
import kotlin.time.DurationUnit

open class SimpleTimer(override val view: BaseView, val length: Duration) : Timer, UpdateComponent {
    private var secondsLeft: Double = 0.0
    private var isRunning: Boolean = false

    init {
        reset()
    }

    override fun restart(): Timer {
        return reset().start()
    }

    override fun start(): Timer {
        isRunning = true
        return this
    }

    override fun stop(): Timer {
        isRunning = false
        return this
    }

    override fun isFinished(): Boolean {
        return secondsLeft <= 0.0
    }

    override fun isRunning(): Boolean {
        return isRunning
    }

    final override fun reset(): Timer {
        secondsLeft = length.toDouble(DurationUnit.SECONDS)
        return this
    }

    protected open fun finish() {
        secondsLeft = 0.0
        stop()
    }

    override fun update(dt: TimeSpan) {
        if (isRunning && !isFinished()) {
            Log().debug { "timer running $secondsLeft"}
            secondsLeft -= dt.seconds
            if (isFinished()) finish()
        }
    }
}

