package components.movement

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.component.UpdateComponent
import com.soywiz.korma.geom.XY
import com.soywiz.korma.math.clamp
import containers.GameEntity

class ClampMovement(
    override val view: GameEntity,
    val maxSpeedXY: XY
) : UpdateComponent {
    override fun update(dt: TimeSpan) {
        view.move.x = view.move.x.clamp(-maxSpeedXY.x, maxSpeedXY.x)
        view.move.y = view.move.y.clamp(-maxSpeedXY.y, maxSpeedXY.y)
    }
}
