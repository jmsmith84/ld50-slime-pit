package components.ai

import com.soywiz.kds.iterators.fastForEach
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.seconds
import com.soywiz.korge.component.FixedUpdateComponent
import com.soywiz.korma.geom.IPoint
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.XY
import containers.GameEntity
import factories.GameEntityFactory
import program.LevelManager
import program.Log
import kotlin.reflect.KClass

enum class WallTileStatus(val id: Int) {
    GONE(0),
    WOOD_NORMAL(2),
    WOOD_DAMAGED(3),
    WOOD_ALMOST_DESTROYED(4),
    STONE_NORMAL(12),
    STONE_SLIGHTLY_DAMAGED(13),
    STONE_DAMAGED(14),
    STONE_VERY_DAMAGED(15),
    STONE_ALMOST_DESTROYED(16);
}

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

    private fun mapIdToType(id: Int): WallTileStatus? {
        WallTileStatus.values().fastForEach {
            if (it.id == id) return it
        }
        return null
    }

    private fun damageTile(tileXY: IPoint, tileId: Int) {
        Log().debug { "$tileXY - tile damaged $tileId"}
        val newId = when (mapIdToType(tileId)) {
            WallTileStatus.WOOD_NORMAL -> WallTileStatus.WOOD_DAMAGED.id
            WallTileStatus.WOOD_DAMAGED -> WallTileStatus.WOOD_ALMOST_DESTROYED.id
            WallTileStatus.WOOD_ALMOST_DESTROYED -> WallTileStatus.GONE.id
            WallTileStatus.STONE_NORMAL -> WallTileStatus.STONE_SLIGHTLY_DAMAGED.id
            WallTileStatus.STONE_SLIGHTLY_DAMAGED -> WallTileStatus.STONE_DAMAGED.id
            WallTileStatus.STONE_DAMAGED -> WallTileStatus.STONE_VERY_DAMAGED.id
            WallTileStatus.STONE_VERY_DAMAGED -> WallTileStatus.STONE_ALMOST_DESTROYED.id
            WallTileStatus.STONE_ALMOST_DESTROYED -> WallTileStatus.GONE.id
            else -> WallTileStatus.GONE.id
        }
        levelManager.setTileIdAt(tileXY.x.toInt(), tileXY.y.toInt(), newId)
    }

    private fun isTileEmpty(position: XY): Boolean {
        val tileXY = levelManager.globalXYToTileXY(position.x, position.y)
        val tileId = levelManager.getTileIdAt(tileXY.x.toInt(), tileXY.y.toInt())
        if (tileId === null) return false
        if (tileId > 0) {
            damageTile(tileXY, tileId)
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
