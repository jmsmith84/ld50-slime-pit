package utility

import com.soywiz.kds.iterators.fastForEachWithIndex
import com.soywiz.klock.TimeSpan
import com.soywiz.korge.tiled.TiledMap
import com.soywiz.korge.tiled.TiledMapView
import com.soywiz.korge.view.*
import com.soywiz.korge.view.tiles.TileMap
import com.soywiz.korge.view.tiles.tileMap
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.Rectangle
import com.soywiz.korma.geom.Size
import com.soywiz.korma.geom.SizeInt
import com.soywiz.korma.geom.shape.Shape2d
import components.movement.MoveDirection
import program.Log

operator fun Shape2d.Rectangle.times(scale: Double): Shape2d.Rectangle {
    return Shape2d.Rectangle(this.x, this.y, this.width * scale, this.height * scale)
}

fun SizeInt(shape: Shape2d.Rectangle): SizeInt {
    return SizeInt(shape.width.toInt(), shape.height.toInt())
}

fun getDeltaScale(dt: TimeSpan): Double {
    return if (dt.milliseconds > 0) dt.milliseconds / 16.6666 else 0.0
}

fun ByteArray.toUInt(): UInt {
    var result = 0u
    for (i in this.indices) {
        result = result or this[i].toUInt().shl(Byte.SIZE_BITS * i)
    }
    return result
}

fun Point.isMovingLeft(): Boolean {
    return x < 0.0
}

fun Point.isMovingRight(): Boolean {
    return x > 0.0
}

fun Point.isMovingUp(): Boolean {
    return y < 0.0
}

fun Point.isMovingDown(): Boolean {
    return y > 0.0
}

fun Point.getDirections(): Set<MoveDirection> {
    val set = mutableSetOf<MoveDirection>()

    if (isMovingLeft()) {
        set.add(MoveDirection.LEFT)
    } else if (isMovingRight()) {
        set.add(MoveDirection.RIGHT)
    }
    if (isMovingDown()) {
        set.add(MoveDirection.DOWN)
    } else if (isMovingUp()) {
        set.add(MoveDirection.UP)
    }
    return set
}

fun TiledMapView.viewHitTest(view: View, direction: HitTestDirection = HitTestDirection.ANY): View? {
    //return this.hitTestView(view, direction)
    return rectHitTest(view.getGlobalBounds(), direction)
}

fun TiledMapView.rectHitTest(rect: Rectangle, direction: HitTestDirection = HitTestDirection.ANY): View? {
    var hit: View?
    when (direction) {
        HitTestDirection.DOWN -> {
            for (x: Int in rect.left.toInt() until rect.right.toInt()) {
                Log().debug { "DOWN ${x},${rect.bottom.toInt()}" }
                hit = pixelHitTest(x, rect.bottom.toInt() - 1, direction)
                if (hit !== null) return hit
            }
        }
        HitTestDirection.UP -> {
            for (x: Int in rect.left.toInt() until rect.right.toInt()) {
                Log().debug { "UP ${x},${rect.top.toInt()}" }
                hit = pixelHitTest(x, rect.top.toInt(), direction)
                if (hit !== null) return hit
            }
        }
        HitTestDirection.LEFT -> {
            for (y: Int in rect.top.toInt() until rect.bottom.toInt()) {
                Log().debug { "LEFT ${rect.left.toInt()},${y}" }
                hit = pixelHitTest(rect.left.toInt(), y, direction)
                if (hit !== null) return hit
            }
        }
        HitTestDirection.RIGHT -> {
            for (y: Int in rect.top.toInt() until rect.bottom.toInt()) {
                Log().debug { "RIGHT ${rect.right.toInt()},${y}" }
                hit = pixelHitTest(rect.right.toInt() - 1, y, direction)
                if (hit !== null) return hit
            }
        }
        else -> {
            for (y: Int in rect.top.toInt() .. rect.bottom.toInt()) {
                for (x: Int in rect.left.toInt() .. rect.right.toInt()) {
                    Log().debug { "ANY ${x},${rect.bottom.toInt()}" }
                    hit = pixelHitTest(x, y, direction)
                    if (hit !== null) return hit
                }
            }
        }
    }
    return null
}

fun TiledMapView.recreateTileLayers(smoothing: Boolean) {
    fastForEachChild {
        if (it is TileMap) {
            it.removeFromParent()
        }
    }
    tiledMap.allLayers.fastForEachWithIndex { _, layer ->
        if (layer is TiledMap.Layer.Tiles) {
            val view: View = tileMap(
                map = layer.map,
                tileset = tileset,
                smoothing = smoothing,
                orientation = tiledMap.data.orientation,
                staggerAxis = tiledMap.data.staggerAxis,
                staggerIndex = tiledMap.data.staggerIndex,
                tileSize = Size(tiledMap.tilewidth.toDouble(), tiledMap.tileheight.toDouble()),
            )
            view.visible(layer.visible)
                .name(layer.name.takeIf { it.isNotEmpty() })
                .xy(layer.offsetx, layer.offsety)
                .alpha(layer.opacity)
                .also { it.addProps(layer.properties) }
        }
    }
}
