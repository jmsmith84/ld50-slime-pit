package components.collision

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.component.UpdateComponent
import containers.GameEntity
import utility.*

class MovesWithoutTilemapCollision(
    override val view: GameEntity
) : UpdateComponent {
    override fun update(dt: TimeSpan) {
        val delta = getDeltaScale(dt)

        if (view.isMovingLeft() || view.isMovingRight()) {
            view.x += view.move.x * delta
        }
        if (view.isMovingUp() || view.isMovingDown()) {
            view.y += view.move.y * delta
        }
    }
}
