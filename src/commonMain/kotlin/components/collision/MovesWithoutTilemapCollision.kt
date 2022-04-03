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

        if (view.move.isMovingLeft() || view.move.isMovingRight()) {
            view.x += view.move.x * delta
        }
        if (view.move.isMovingUp() || view.move.isMovingDown()) {
            view.y += view.move.y * delta
        }
    }
}
