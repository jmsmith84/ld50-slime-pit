package components.attack

import com.soywiz.klock.TimeSpan
import com.soywiz.klock.seconds
import com.soywiz.korge.component.FixedUpdateComponent
import containers.GameEntity
import containers.enemy.SpriteEnemy
import containers.player.PLAYER_NAME

class ShootsBulletsAtPlayer(
    override val view: SpriteEnemy,
    private val bullet: GameEntity,
    step: TimeSpan = 2.seconds,
    maxAccumulated: Int = 10
) : FixedUpdateComponent(view, step, maxAccumulated) {
    override fun update() {
        val player = view.parent?.findViewByName(PLAYER_NAME)
        if (player != null) {
            val bulletOrigin = view.localXY()
            bulletOrigin.x += view.getBounds().width / 2
            bulletOrigin.y += view.getBounds().height

            view.parent?.addChild(bullet.clone())
        }
    }
}
