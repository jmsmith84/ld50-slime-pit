package containers.enemy

import com.soywiz.klock.seconds
import com.soywiz.korge.component.attach
import com.soywiz.korge.view.Sprite
import com.soywiz.korma.geom.XY
import components.MultipliesAdjacent
import program.AssetManager
import program.LevelManager
import program.SoundManager

class AcidSlime(assets: AssetManager, soundManager: SoundManager, levelManager: LevelManager, position: XY)
    : SpriteEnemy(Sprite(assets.slimeBitmap), assets, soundManager, levelManager, position) {

        init {
            MultipliesAdjacent(this, levelManager, 3.seconds).attach()
        }
}
