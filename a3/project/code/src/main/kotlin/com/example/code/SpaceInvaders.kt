package com.example.code

import javafx.application.Application
import javafx.stage.Stage

class SpaceInvaders: Application() {
    private var stage: Stage? = null
    private var score = 0
    private var level = 1
    private var gameScreen = GameScreen(this)
    private var initialScreen = InitialScreen(this)
    private var gameOverScreen = GameOverScreen(this)

    fun setGameOverScreen() {
        score = gameScreen.statusBar.score
        gameOverScreen = GameOverScreen(this)
        stage!!.scene = gameOverScreen.start(score, "Game Over")
        stage!!.show()
    }

    private fun setGameWonScreen(){
        score = gameScreen.statusBar.score
        gameOverScreen = GameOverScreen(this)
        stage!!.scene = gameOverScreen.start(score, "Game Won")
        stage!!.show()
    }

    fun setInitialScreen(){
        initialScreen = InitialScreen(this)
        stage!!.scene = initialScreen.start()
        stage!!.show()
    }

    fun nextLevel(){
        level = gameScreen.statusBar.level
        score = gameScreen.statusBar.score

        if (level == 3){
            setGameWonScreen()
        }
        else{
            setGameScreen(score, level + 1)
        }
    }

    fun setGameScreen(score: Int, level: Int){
        gameScreen.spaceInvaders = null
        gameScreen.playerManager.spaceInvaders = null
        gameScreen.collisionHandler.spaceInvaders = null
        gameScreen = GameScreen(this)
        stage!!.scene = gameScreen.start(score, level)
        stage!!.isResizable = false
        stage!!.show()
    }

    override fun start(stage: Stage?) {
        this.stage = stage
        setInitialScreen()
    }
}