package containers.spawn

import com.soywiz.korge.view.position
import com.soywiz.korma.geom.XY
import containers.GameEntity
import containers.enemy.AcidSlime
import program.AssetManager
import program.LevelManager
import program.SoundManager

class SlimeSpawner(
    assets: AssetManager, soundManager: SoundManager, levelManager: LevelManager,
    position: XY
) : GameEntity(assets, soundManager, levelManager) {

    init {
        position(position)
        //addUpdater {
        val slime = AcidSlime(assets, soundManager, levelManager, pos.copy())
        levelManager.getCurrentMapView().addChild(slime)
        //}
    }
}
