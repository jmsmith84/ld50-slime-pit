package components.movement

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.component.UpdateComponent
import com.soywiz.korma.geom.XY
import containers.GameEntity
import utility.getDeltaScale
import kotlin.math.cos
import kotlin.math.sin

class CircleMovement(
    override val view: GameEntity,
    private val basePos: XY,
    private val radius: Int = 50,
    private val speed: Double = 0.05
) : UpdateComponent {
    private var angle = 0.0

    init {
        view.x += radius
    }

    override fun update(dt: TimeSpan) {
        val delta = getDeltaScale(dt)
        angle += speed * delta

        view.move.x = (basePos.x + cos(angle) * radius) - view.x
        view.move.y = (basePos.y + sin(angle) * radius) - view.y
    }
}
