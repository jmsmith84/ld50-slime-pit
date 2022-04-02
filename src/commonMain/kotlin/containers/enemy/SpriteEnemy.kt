package containers.enemy

import com.soywiz.korge.view.Sprite
import com.soywiz.korge.view.onCollision
import com.soywiz.korge.view.position
import com.soywiz.korma.geom.XY
import containers.SpriteEntity
import containers.bullet.PlayerBullet
import program.*

@Suppress("MemberVisibilityCanBePrivate")
abstract class SpriteEnemy(
    sprite: Sprite,
    assets: AssetManager,
    soundManager: SoundManager,
    levelManager: LevelManager,
    position: XY
) : SpriteEntity(sprite, assets, soundManager, levelManager), Enemy {
    protected open val value = 100

    override fun kill() {
        this.removeFromParent()
        GameState.score += value
    }

    init {
        position(position)
        onCollision {
            if (it is PlayerBullet) {
                it.removeFromParent()
                hp--
                if (hp == 0u) {
                    this@SpriteEnemy.kill()
                }
            }
        }

        Log().debug { "Enemy created @ $position" }
    }
}
