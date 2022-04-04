package components.movement

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.component.UpdateComponent
import containers.GameEntity

@Suppress("MemberVisibilityCanBePrivate")
class HasFacing(
    override val view: GameEntity,
    initialDirection: MoveDirection = MoveDirection.DOWN
) : UpdateComponent {
    private var facing: MoveDirection = initialDirection

    override fun update(dt: TimeSpan) {
        val directions = view.getMoveDirections()

        // Priority order: l+r > u+d
        if (directions.contains(MoveDirection.RIGHT)) {
            facing = MoveDirection.RIGHT
        } else if (directions.contains(MoveDirection.LEFT)) {
            facing = MoveDirection.LEFT
        } else if (directions.contains(MoveDirection.UP)) {
            facing = MoveDirection.UP
        } else if (directions.contains(MoveDirection.DOWN)) {
            facing = MoveDirection.DOWN
        }
    }

    fun getFacing(): MoveDirection {
        return facing
    }
}
