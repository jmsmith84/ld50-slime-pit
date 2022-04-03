package components.collision

import com.soywiz.korge.component.Component
import com.soywiz.korge.view.onCollision
import containers.bullet.PlayerBullet
import containers.enemy.SpriteEnemy

class DamagedByPlayerBullets(override val view: SpriteEnemy)
    : Component {
    init {
        view.onCollision(filter = { it != view && it is PlayerBullet }) {
            it.removeFromParent()
            view.damage((it as PlayerBullet).damageValue)
        }
    }
}
