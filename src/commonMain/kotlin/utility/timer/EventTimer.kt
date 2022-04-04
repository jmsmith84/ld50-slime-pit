package utility.timer

import com.soywiz.korge.baseview.BaseView
import program.Log
import kotlin.time.Duration

class EventTimer(view: BaseView, length: Duration, var callback: (Timer) -> Unit) : SimpleTimer(view, length) {
    override fun finish() {
        super.finish()
        Log().debug { "heloo/??" }
        callback(this)
    }

    fun newCallback(callback: (Timer) -> Unit) {
        this.callback = callback
    }
}
