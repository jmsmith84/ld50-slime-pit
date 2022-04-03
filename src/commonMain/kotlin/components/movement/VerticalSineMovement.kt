package components.movement

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.component.UpdateComponent
import containers.GameEntity
import kotlin.math.sin

class VerticalSineMovement(
    override val view: GameEntity,
    private var baseX: Double,
    private var radius: Int = 50,
    private var speed: Double = 3.0,
    private var frequency: Double = 0.03
) : UpdateComponent {
    override fun update(dt: TimeSpan) {
        view.move.y = speed
        view.move.x = (baseX + radius * sin(view.y * frequency)) - view.x
    }
}
