package components

import com.soywiz.korma.geom.IPoint
import com.soywiz.korma.geom.Point
import containers.GameEntity

interface GameEntityFactory {
    fun create(spawnPosition: IPoint = Point.Zero): GameEntity
}
