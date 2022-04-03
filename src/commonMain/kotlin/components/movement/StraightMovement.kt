package components.movement

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.component.UpdateComponent
import com.soywiz.korge.view.View
import com.soywiz.korma.geom.XY
import com.soywiz.korma.geom.add
import com.soywiz.korma.geom.times
import utility.getDeltaScale

class StraightMovement(
    override val view: View,
    private var speed: XY
) : UpdateComponent {
    override fun update(dt: TimeSpan) {
        val delta = getDeltaScale(dt)
        view.pos = view.pos.add(speed * delta)
    }
}
