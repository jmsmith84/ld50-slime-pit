package containers

import com.soywiz.korge.view.BaseImage
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.HitTestDirection
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.Rectangle
import components.movement.MoveDirection
import program.AssetManager
import program.LevelManager
import program.SoundManager
import utility.rectHitTest

open class GameEntity(
    protected val assets: AssetManager,
    protected val soundManager: SoundManager,
    protected val levelManager: LevelManager,
    protected var hp: UInt = 1u
) : Container() {
    val move = Point(0, 0)
    var canMove = true
    var speedModifier: Double = 1.0
    lateinit var image: BaseImage

    fun getHP(): UInt {
        return hp
    }

    open fun damage(amount: UInt = 1u) {
        if (amount > hp) hp = 0u else hp -= amount
        if (hp == 0u) kill()
    }

    open fun kill() {}

    fun getCurrentBounds(): Rectangle {
        return getGlobalBounds()
    }

    fun isTouchingGround(): Boolean {
        val clone = getCurrentBounds().clone()
        clone.y++
        return levelManager.getMapView().rectHitTest(clone, HitTestDirection.DOWN) !== null
    }

    fun isMovingLeft(): Boolean {
        return move.x < 0.0
    }

    fun isMovingRight(): Boolean {
        return move.x > 0.0
    }

    fun isMovingUp(): Boolean {
        return move.y < 0.0
    }

    fun isMovingDown(): Boolean {
        return move.y > 0.0
    }

    fun isMoving(): Boolean {
        return !getMoveDirections().contains(MoveDirection.NONE)
    }

    fun getMoveDirections(): Set<MoveDirection> {
        val set = mutableSetOf<MoveDirection>()

        if (!isMovingLeft() && !isMovingRight() && !isMovingUp() && !isMovingDown()) {
            set.add(MoveDirection.NONE)
            return set
        }

        if (isMovingLeft()) {
            set.add(MoveDirection.LEFT)
        } else if (isMovingRight()) {
            set.add(MoveDirection.RIGHT)
        }
        if (isMovingDown()) {
            set.add(MoveDirection.DOWN)
        } else if (isMovingUp()) {
            set.add(MoveDirection.UP)
        }
        return set
    }
}
