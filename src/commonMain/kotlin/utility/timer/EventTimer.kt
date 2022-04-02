package utility.timer

import com.soywiz.korge.baseview.BaseView
import kotlin.time.Duration

class EventTimer(view: BaseView, length: Duration, val callback: (Timer) -> Unit) : SimpleTimer(view, length) {
    override fun finish() {
        super.finish()
        callback(this)
    }
}
