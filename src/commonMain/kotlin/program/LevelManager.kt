package program

import com.soywiz.kmem.toIntFloor
import com.soywiz.korge.tiled.TiledMap
import com.soywiz.korge.tiled.TiledMapView
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.collidesWith
import com.soywiz.korge.view.collidesWithShape
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.Rectangle
import com.soywiz.korma.geom.XY
import com.soywiz.korma.geom.shape.Shape2d
import containers.enemy.AcidSlime

class LevelManager(private val assets: AssetManager) {
    var mapView: TiledMapView? = null
    var smoothing = false
    private var currentLevel: UShort = 0u
    private var currentMap: TiledMap? = null

    fun setNewMap(level: UShort, container: Container, callback: TiledMapView.() -> Unit = {}): TiledMapView {
        currentLevel = level
        currentMap = assets.levels[level]
        mapView?.removeFromParent()
        return createMapView(container, callback)
    }

    fun getLevel(): UShort {
        return currentLevel
    }

    fun getCurrentMap(): TiledMap {
        if (currentMap === null) throw RuntimeException("Trying to get current level map when there isn't one.")
        return currentMap!!
    }

    fun getCurrentMapView(): TiledMapView {
        if (currentMap === null) throw RuntimeException("Trying to get current map view when there isn't one.")
        return mapView!!
    }

    private fun createMapView(
        container: Container,
        callback: TiledMapView.() -> Unit = {}
    ): TiledMapView {
        mapView = TiledMapView(getCurrentMap(), false, smoothing)
            .addTo(container, callback)
        return mapView!!
    }

    fun globalXYToTileXY(x: Double, y: Double): XY {
        return Point((x / currentMap!!.tilewidth).toIntFloor(),
            (y / currentMap!!.tileheight).toIntFloor())
    }

    fun isTileEmpty(x: Int, y: Int): Boolean {
        if (currentMap === null) return true

        val tile = currentMap?.tileLayers?.get(0)?.get(x, y)
        Log().info { "tile find ${tile}"}
        if (tile === null) return true
        if (tile > 0) return false

        val tileBounds = Rectangle(
            x * currentMap!!.tilewidth,
            y * currentMap!!.tileheight,
            currentMap!!.tilewidth,
            currentMap!!.tileheight
        )

        getCurrentMapView().fastForEachChild {
            if (it is AcidSlime) {
                if (it.getCurrentBounds().intersects(tileBounds)) {
                    return true
                }
            }
        }
        return false
    }
}
