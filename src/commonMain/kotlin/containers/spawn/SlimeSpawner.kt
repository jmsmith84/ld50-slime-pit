package containers.spawn

import com.soywiz.korge.view.position
import com.soywiz.korma.geom.XY
import components.GameEntityFactory
import containers.GameEntity
import program.AssetManager
import program.LevelManager
import program.SoundManager

class SlimeSpawner(
    assets: AssetManager,
    soundManager: SoundManager,
    levelManager: LevelManager,
    factory: GameEntityFactory,
    position: XY
) : GameEntity(assets, soundManager, levelManager) {

    init {
        position(position)

        val slime = factory.create(pos.copy())
        levelManager.getCurrentMapView().addChild(slime)
    }
}
