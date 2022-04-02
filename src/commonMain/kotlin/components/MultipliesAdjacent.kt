package components

import com.soywiz.klock.TimeSpan
import com.soywiz.klock.seconds
import com.soywiz.korge.component.FixedUpdateComponent
import com.soywiz.korge.view.position
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.XY
import containers.GameEntity
import program.LevelManager
import program.Log

class MultipliesAdjacent(
    override val view: GameEntity,
    private val levelManager: LevelManager,
    step: TimeSpan = 5.seconds,
    maxAccumulated: Int = 10
) : FixedUpdateComponent(view, step, maxAccumulated) {
    override fun update() {
        var spawnXY = Point(view.x - 16, view.y)
        if (isTileEmpty(spawnXY)) {
            Log().info { "empty left" }
            cloneSelf(spawnXY)
        }
        spawnXY = Point(view.x + 16, view.y)
        if (isTileEmpty(spawnXY)) {
            Log().info { "empty right" }
            cloneSelf(spawnXY)
        }
        spawnXY = Point(view.x, view.y + 16)
        if (isTileEmpty(spawnXY)) {
            Log().info { "empty down" }
            cloneSelf(spawnXY)
        }
    }

    private fun isTileEmpty(position: XY): Boolean {
        val tileXY = levelManager.globalXYToTileXY(position.x, position.y)
        return levelManager.isTileEmpty(tileXY.x.toInt(), tileXY.y.toInt())
    }

    private fun cloneSelf(position: XY) {
        view.parent?.addChild(view.clone()
            .position(position))
    }
}
