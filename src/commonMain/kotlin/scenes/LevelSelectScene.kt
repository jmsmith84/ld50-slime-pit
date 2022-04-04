package scenes

import com.soywiz.korev.Key
import com.soywiz.korge.input.keys
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.async.launchImmediately
import program.AssetManager
import program.LevelManager

enum class LevelMenuOptions {
    MAP1,
    MAP2,
    MAP3,
    MAP4,
    BACK
}

open class LevelSelectScene(private val fontsize: Double = 24.0) : Scene() {
    private lateinit var assets: AssetManager
    private lateinit var levelManager: LevelManager

    private var selection: LevelMenuOptions = LevelMenuOptions.MAP1
    private val unselectedColor = Colors.DARKGRAY
    private val selectedColor = Colors.WHITE

    override suspend fun Container.sceneInit() {
        assets = injector.get()
        levelManager = injector.get()

        val uiFont = assets.defaultFont

        text(levelManager.getLevelName(1u)) {
            textSize = fontsize
            font = uiFont
            centerOnStage()
            y -= 70
            addUpdater {
                color = when (selection) {
                    LevelMenuOptions.MAP1 -> selectedColor
                    else -> unselectedColor
                }
            }
        }
        text(levelManager.getLevelName(2u)) {
            textSize = fontsize
            font = uiFont
            centerOnStage()
            y -= 40
            addUpdater {
                color = when (selection) {
                    LevelMenuOptions.MAP2 -> selectedColor
                    else -> unselectedColor
                }
            }
        }
        text(levelManager.getLevelName(3u)) {
            textSize = fontsize
            font = uiFont
            centerOnStage()
            y -= 10
            addUpdater {
                color = when (selection) {
                    LevelMenuOptions.MAP3 -> selectedColor
                    else -> unselectedColor
                }
            }
        }
        text(levelManager.getLevelName(4u)) {
            textSize = fontsize
            font = uiFont
            centerOnStage()
            y += 20
            addUpdater {
                color = when (selection) {
                    LevelMenuOptions.MAP4 -> selectedColor
                    else -> unselectedColor
                }
            }
        }
        text("BACK") {
            textSize = fontsize
            font = uiFont
            centerOnStage()
            y += 50
            addUpdater {
                color = when (selection) {
                    LevelMenuOptions.BACK -> selectedColor
                    else -> unselectedColor
                }
            }
        }

        keys.down {
            when (it.key) {
                Key.ENTER -> chooseOption()
                Key.UP -> cursorUp()
                Key.DOWN -> cursorDown()
                else -> Unit
            }
        }
    }

    private fun chooseOption() {
        when (selection) {
            LevelMenuOptions.MAP1 -> startLevel(1u)
            LevelMenuOptions.MAP2 -> startLevel(2u)
            LevelMenuOptions.MAP3 -> startLevel(3u)
            LevelMenuOptions.MAP4 -> startLevel(4u)
            LevelMenuOptions.BACK -> back()
        }
    }

    private fun cursorUp() {
        selection = when (selection) {
            LevelMenuOptions.MAP3 -> LevelMenuOptions.MAP2
            LevelMenuOptions.MAP4 -> LevelMenuOptions.MAP3
            LevelMenuOptions.BACK -> LevelMenuOptions.MAP4
            LevelMenuOptions.MAP1 -> LevelMenuOptions.BACK
            LevelMenuOptions.MAP2 -> LevelMenuOptions.MAP1
        }
    }

    private fun cursorDown() {
        selection = when (selection) {
            LevelMenuOptions.MAP1 -> LevelMenuOptions.MAP2
            LevelMenuOptions.MAP2 -> LevelMenuOptions.MAP3
            LevelMenuOptions.MAP3 -> LevelMenuOptions.MAP4
            LevelMenuOptions.MAP4 -> LevelMenuOptions.BACK
            LevelMenuOptions.BACK -> LevelMenuOptions.MAP1
        }
    }

    private fun startLevel(level: UShort) {
        levelManager.setLevel(level)
        launchImmediately {
            sceneContainer.changeTo<GameScene>()
        }
    }

    private fun back() {
        launchImmediately {
            sceneContainer.changeTo<MenuScene>()
        }
    }
}
