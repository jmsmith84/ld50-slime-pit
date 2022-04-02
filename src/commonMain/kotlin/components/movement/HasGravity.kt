package components.movement

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.component.UpdateComponent
import containers.GameEntity
import utility.getDeltaScale


class HasGravity(override val view: GameEntity, var gravity: Double)
    : UpdateComponent {
    override fun update(dt: TimeSpan) {
        val delta = getDeltaScale(dt)

        view.move.y += gravity * delta
    }
}
