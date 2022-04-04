package components.collision

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.component.UpdateComponent
import com.soywiz.korge.view.HitTestDirection
import containers.GameEntity
import program.LevelManager
import program.Log
import utility.*
import kotlin.math.round

class MovesWithTilemapCollision(
    override val view: GameEntity,
    private val levelManager: LevelManager
) : UpdateComponent {
    override fun update(dt: TimeSpan) {
        val delta = getDeltaScale(dt)
        val mapView = levelManager.getCurrentMapView()

        if (view.isMovingLeft() || view.isMovingRight()) {
            val oldX = view.x
            view.x += round(view.move.x * delta)

            if (mapView.viewHitTest(view, HitTestDirection.LEFT) !== null
                || mapView.viewHitTest(view, HitTestDirection.RIGHT) !== null
            ) {
                Log().debug { "map hit X " + view.pos }

                if (view.isMovingLeft()) {
                    while (mapView.viewHitTest(view, HitTestDirection.LEFT) !== null
                        && view.x < oldX
                    ) {
                        view.x += 1.0
                    }
                } else if (view.isMovingRight()) {
                    while (mapView.viewHitTest(view, HitTestDirection.RIGHT) !== null
                        && view.x > oldX
                    ) {
                        view.x -= 1.0
                    }
                }
                view.move.x = 0.0
            }
        }

        if (view.isMovingUp() || view.isMovingDown()) {
            val oldY = view.y
            view.y += round(view.move.y * delta)

            if (mapView.viewHitTest(view, HitTestDirection.UP) !== null
                || mapView.viewHitTest(view, HitTestDirection.DOWN) !== null
            ) {
                Log().debug { "map hit Y " + view.pos }

                if (view.isMovingUp()) {
                    while (mapView.viewHitTest(view, HitTestDirection.UP) !== null
                        && view.y < oldY
                    ) {
                        view.y += 1.0
                    }
                } else if (view.isMovingDown()) {
                    while (mapView.viewHitTest(view, HitTestDirection.DOWN) !== null
                        && view.y > oldY
                    ) {
                        view.y -= 1.0
                    }
                }
                view.move.y = 0.0
            }
        }
    }
}
