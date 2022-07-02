package com.example.code

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

class HelloApplication: Application() {
    var stage: Stage? = null
    var score = 0
    private val gameScreen = GameScreen(this)

    fun setGameOverScreen(){
        stage?.scene = Scene(GameOverScreen())
        gameScreen.statusBar.score
    }

    override fun start(stage: Stage?) {
        this.stage = stage
        if (stage != null) {
            gameScreen.start(stage, score, 1)
        }
    }
}