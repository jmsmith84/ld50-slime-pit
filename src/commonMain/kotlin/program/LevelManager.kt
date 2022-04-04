package program

import com.soywiz.kmem.toIntFloor
import com.soywiz.korge.tiled.TiledMap
import com.soywiz.korge.tiled.TiledMapView
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korma.geom.IPoint
import com.soywiz.korma.geom.Point
import utility.recreateTileLayers

@Suppress("MemberVisibilityCanBePrivate")
class LevelManager(private val assets: AssetManager) {
    private var mapView: TiledMapView? = null
    private var currentLevel: UShort = 0u
    private var currentMap: TiledMap? = null
    private val emptyTileId = 0

    var smoothing = false

    fun setNewMap(level: UShort, container: Container, callback: TiledMapView.() -> Unit = {}): TiledMapView {
        currentLevel = level
        if (assets.levels[level] === null) throw RuntimeException("Selected level not found in assets")
        currentMap = TiledMap(assets.levels[level]!!.clone(), assets.tileSets)
        return createMapView(currentMap!!, container, callback)
    }

    fun getLevel(): UShort {
        return currentLevel
    }

    fun getLevelName(): String {
        return when (currentLevel) {
            1.toUShort() -> "WOT A DODDLE"
            2.toUShort() -> "E.Z. WEEZY"
            else -> { "LEVEL ??" }
        }
    }

    fun getCurrentMap(): TiledMap {
        if (currentMap === null) throw RuntimeException("Trying to get current level map when there isn't one")
        return currentMap!!
    }

    fun getCurrentMapView(): TiledMapView {
        if (mapView === null) throw RuntimeException("Trying to get current map view when there isn't one")
        return mapView!!
    }

    fun getCurrentMapObjects(): TiledMap.Layer.Objects {
        if (currentMap === null) throw RuntimeException("Trying to get current level map when there isn't one")
        return currentMap!!.objectLayers.first()
    }

    private fun createMapView(
        map: TiledMap,
        container: Container,
        callback: TiledMapView.() -> Unit = {}
    ): TiledMapView {
        mapView?.removeFromParent()
        mapView = TiledMapView(map, false, smoothing)
            .addTo(container, callback)
        return mapView!!
    }

    fun globalXYToTileXY(x: Double, y: Double): IPoint {
        return Point(
            (x / currentMap!!.tilewidth).toIntFloor(),
            (y / currentMap!!.tileheight).toIntFloor()
        )
    }

    fun globalXYToTileXY(xy: IPoint): IPoint {
        return globalXYToTileXY(xy.x, xy.y)
    }

    fun tileXYToGlobalXY(x: Int, y: Int): IPoint {
        return Point(
            (x * currentMap!!.tilewidth),
            (y * currentMap!!.tileheight)
        )
    }

    fun tileXYToGlobalXY(xy: IPoint): IPoint {
        return tileXYToGlobalXY(xy.x.toInt(), xy.y.toInt())
    }

    fun getTileIdAt(x: Int, y: Int): Int? {
        if (x < 0 || y < 0 || x >= currentMap?.tileLayers?.get(0)?.width!! || y >= currentMap?.tileLayers?.get(0)?.height!!) {
            return null
        }
        return currentMap?.tileLayers?.get(0)?.get(x, y)
    }

    fun setTileIdAt(x: Int, y: Int, tileId: Int) {
        if (currentMap?.tileLayers?.get(0)?.get(x, y) == tileId) return
        if (x < 0 || y < 0 || x >= currentMap?.tileLayers?.get(0)?.width!! || y >= currentMap?.tileLayers?.get(0)?.height!!) {
            throw RuntimeException("Trying to set map tile $x,$y is out of bounds")
        }

        currentMap?.tileLayers?.get(0)?.set(x, y, tileId)
        mapView?.recreateTileLayers(false)
    }

    fun isTileEmpty(x: Int, y: Int): Boolean {
        if (currentMap === null) return true

        val tile = getTileIdAt(x, y)
        Log().debug { "tile find ID:$tile" }
        if (tile === null) return false
        return (tile == emptyTileId)
    }

    fun isTileEmpty(xy: IPoint): Boolean {
        return isTileEmpty(xy.x.toInt(), xy.y.toInt())
    }

}
