package containers.enemy

import com.soywiz.klock.seconds
import com.soywiz.korge.view.Sprite
import com.soywiz.korma.geom.IPoint
import com.soywiz.korma.geom.Point
import components.ai.MultipliesAdjacent
import factories.SlimeFactory
import program.AssetManager
import program.LevelManager
import program.SoundManager

class AcidSlime(assets: AssetManager, soundManager: SoundManager, levelManager: LevelManager, position: IPoint) :
    SpriteEnemy(Sprite(assets.slimeBitmap), assets, soundManager, levelManager, position) {

    init {
        addComponent(
            MultipliesAdjacent(
                this,
                SlimeFactory(assets, soundManager, levelManager),
                listOf(
                    Point(-16, 0),
                    Point(16, 0),
                    Point(0, -16),
                    Point(0, 16)
                ),
                levelManager,
                3.seconds
            )
        )
    }
}
