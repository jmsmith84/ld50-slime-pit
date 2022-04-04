package scenes

import com.soywiz.korev.Key
import com.soywiz.korge.input.keys
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.async.launchImmediately
import program.AssetManager
import program.LevelManager

enum class MainMenuOptions {
    START_GAME,
    QUIT
}

open class MenuScene(private val title: String, private val fontsize: Double = 24.0) : Scene() {
    private lateinit var assets: AssetManager

    private var selection: MainMenuOptions = MainMenuOptions.START_GAME
    private val unselectedColor = Colors.DARKGRAY
    private val selectedColor = Colors.WHITE

    override suspend fun Container.sceneInit() {
        assets = injector.get()

        val uiFont = assets.defaultFont

        text(title) {
            font = uiFont
            y = 24.0
            centerXOnStage()
        }
        text("START") {
            textSize = fontsize
            font = uiFont
            centerOnStage()
            y -= 50
            addUpdater {
                color = when (selection) {
                    MainMenuOptions.START_GAME -> selectedColor
                    else -> unselectedColor
                }
            }
        }
        text("QUIT") {
            textSize = fontsize
            font = uiFont
            centerOnStage()
            y += 50
            addUpdater {
                color = when (selection) {
                    MainMenuOptions.QUIT -> selectedColor
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
            MainMenuOptions.START_GAME -> launchImmediately {
                    sceneContainer.changeTo<LevelSelectScene>()
            }
            MainMenuOptions.QUIT -> exitGame()
        }
    }

    private fun cursorDown() {
        selection = when (selection) {
            MainMenuOptions.QUIT -> MainMenuOptions.START_GAME
            MainMenuOptions.START_GAME -> MainMenuOptions.QUIT
        }
    }

    private fun cursorUp() {
        cursorDown()
    }

    private fun exitGame() {
        views.gameWindow.close()
    }
}
