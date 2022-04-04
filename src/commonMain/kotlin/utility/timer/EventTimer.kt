package utility.timer

import com.soywiz.korge.baseview.BaseView
import kotlin.time.Duration

class EventTimer(view: BaseView, length: Duration, var callback: (Timer) -> Unit) : SimpleTimer(view, length) {
    override fun finish() {
        super.finish()
        callback(this)
    }

    fun newCallback(callback: (Timer) -> Unit) {
        this.callback = callback
    }
}
