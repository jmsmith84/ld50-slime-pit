package components

import com.soywiz.kds.iterators.fastForEach
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.seconds
import com.soywiz.korge.component.FixedUpdateComponent
import com.soywiz.korge.view.position
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.XY
import containers.GameEntity
import program.LevelManager
import kotlin.reflect.KClass

class MultipliesAdjacent(
    override val view: GameEntity,
    private val factory: GameEntityFactory,
    private val directions: List<Point>,
    private val levelManager: LevelManager,
    step: TimeSpan = 5.seconds,
    maxAccumulated: Int = 10
) : FixedUpdateComponent(view, step, maxAccumulated) {
    private val viewType: KClass<out GameEntity> = view::class

    override fun update() {
        directions.fastForEach {
            val spawnXY = view.pos.copy().add(it)
            if (isTileEmpty(spawnXY)) {
                cloneSelf(spawnXY)
            }
        }
    }

    private fun isTileEmpty(position: XY): Boolean {
        val tileXY = levelManager.globalXYToTileXY(position.x, position.y)
        val tileId = levelManager.getTileIdAt(tileXY.x.toInt(), tileXY.y.toInt())
        if (tileId !== null && tileId > 0) {
            levelManager.setTileIdAt(tileXY.x.toInt(), tileXY.y.toInt(), 0)
            return false
        }

        view.parent?.fastForEachChild {
            if (it::class == viewType) {
                //Log().debug { "++ $viewType - ${it::class} $position ${it.pos}" }
                if (it.pos.equals(position)) {
                    return false
                }
            }
        }
        return true
    }

    private fun cloneSelf(position: XY) {
        view.parent?.addChild(
            factory.create(position)
        )
    }
}
